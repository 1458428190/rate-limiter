package org.gucha.ratelimiter.core.framework;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.gucha.ratelimiter.common.exception.InternalErrorException;
import org.gucha.ratelimiter.common.exception.InvalidUrlException;
import org.gucha.ratelimiter.common.exception.OverloadException;
import org.gucha.ratelimiter.core.framework.algorithm.RateLimiter;
import org.gucha.ratelimiter.core.framework.context.RateLimiterBeansFactory;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptor;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptorChain;
import org.gucha.ratelimiter.core.framework.rule.ApiLimit;
import org.gucha.ratelimiter.core.framework.rule.RateLimitRule;
import org.gucha.ratelimiter.core.framework.rule.source.RuleConfigSource;
import org.gucha.ratelimiter.core.framework.utils.UrlUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/30 下午4:13
 */
@Slf4j
public abstract class AbstractUrlRateLimiter implements UrlRateLimiter {

    /**
     * 自己管key
     */
    private final ConcurrentHashMap<String, RateLimiter> counters = new ConcurrentHashMap<>(256);

    private final RateLimitRule rateLimitRule;

    private RateLimiterInterceptorChain interceptorChain;

    public AbstractUrlRateLimiter() {
        this((RateLimitRule) null);
    }

    public AbstractUrlRateLimiter(RuleConfigSource source) {
        this.rateLimitRule = RateLimiterBeansFactory.BEANS_CONTEXT.obtainUrlRateLimitRule(null);
        source = RateLimiterBeansFactory.BEANS_CONTEXT.obtainRuleConfigSource(source);
        this.rateLimitRule.addRule(source.load());
        this.interceptorChain = RateLimiterBeansFactory.BEANS_CONTEXT.obtainInterceptorChain(null);
    }

    public AbstractUrlRateLimiter(RateLimitRule rateLimitRule) {
        this(rateLimitRule, null);
    }

    public AbstractUrlRateLimiter(RateLimitRule rateLimitRule, RateLimiterInterceptorChain interceptorChain) {
        this.rateLimitRule = RateLimiterBeansFactory.BEANS_CONTEXT.obtainUrlRateLimitRule(rateLimitRule);
        if (rateLimitRule == null) {
            RuleConfigSource source = RateLimiterBeansFactory.BEANS_CONTEXT.obtainRuleConfigSource(null);
            this.rateLimitRule.addRule(source.load());
        }
        this.interceptorChain = RateLimiterBeansFactory.BEANS_CONTEXT.obtainInterceptorChain(interceptorChain);
    }

    @Override
    public void limit(String appId, String url) throws InternalErrorException, InvalidUrlException, OverloadException {
        // 前置处理
        interceptorChain.doBeforeLimit(appId, url);
        ApiLimit apiLimit = null;
        boolean passed = false;
        Exception exception = null;
        try {
            String urlPath = UrlUtils.getUrlPath(url);
            apiLimit = rateLimitRule.getLimit(appId, urlPath);
            // 没有配置相应的限流规则
            if (apiLimit == null) {
                log.info("no rate limit rule for appId:{}, api: {}", appId, urlPath);
                return;
            }
            // 获取限流器
            RateLimiter rateLimiter = getRateLimiterAlgorithm(appId, urlPath, apiLimit.getLimit());
            passed = rateLimiter.tryAcquire();
            if (!passed) {
                throw new OverloadException(appId + ":" + url + " has exceeded max tps limit: " + apiLimit.getLimit());
            }
        } catch (InternalErrorException | InvalidUrlException e) {
            exception = e;
            throw e;
        } catch (OverloadException e) {
            passed = false;
            throw e;
        } catch (Exception e) {
            InternalErrorException re = new InternalErrorException("RateLimiter internal error.", e);
            exception = re;
            throw re;
        } finally {
            // 后置处理
            interceptorChain.doAfterLimit(appId, url, apiLimit, passed, exception);
        }
    }

    protected abstract RateLimiter createRateLimitAlgorithm(String limitKey, int limit);

    public RateLimiter getRateLimiterAlgorithm(String appId, String api, int limit) {
        String limitKey = generateUrlKey(appId, api);
        RateLimiter rateLimiter = counters.get(limitKey);
        if (rateLimiter == null) {
            RateLimiter newRateLimiter = createRateLimitAlgorithm(limitKey, limit);
            rateLimiter = counters.putIfAbsent(limitKey, newRateLimiter);
            if (rateLimiter == null) {
                rateLimiter = newRateLimiter;
            }
        }
        return rateLimiter;
    }

    private String generateUrlKey(String appId, String api) {
        StringBuilder builder = new StringBuilder();
        builder.append(appId).append(":").append(api);
        return builder.toString();
    }

    @Override
    public void addInterceptor(RateLimiterInterceptor interceptor) {
        if (interceptor != null) {
            this.interceptorChain.addInterceptor(interceptor);
        }
    }

    @Override
    public void addInterceptors(List<RateLimiterInterceptor> interceptors) {
        if (CollectionUtils.isNotEmpty(interceptors)) {
            this.interceptorChain.addInterceptors(interceptors);
        }
    }
}
