package org.gucha.ratelimiter.core.framework.context;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.gucha.ratelimiter.core.framework.env.RateLimiterConfig;
import org.gucha.ratelimiter.core.framework.env.RedisConfig;
import org.gucha.ratelimiter.core.framework.extension.ExtensionLoader;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptor;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptorChain;
import org.gucha.ratelimiter.core.framework.redis.DefaultJedisTaskExecutor;
import org.gucha.ratelimiter.core.framework.redis.JedisTaskExecutor;
import org.gucha.ratelimiter.core.framework.rule.RateLimitRule;
import org.gucha.ratelimiter.core.framework.rule.UrlRateLimitRule;
import org.gucha.ratelimiter.core.framework.rule.parser.JsonRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.RuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.YamlRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.source.FileRuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.RuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.ZookeeperRuleConfigSource;

import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午5:27
 */
public class RateLimiterBeansFactory {
    public static final RateLimiterBeansFactory BEANS_CONTEXT = new RateLimiterBeansFactory();

    private RateLimiterBeansFactory() {
        RateLimiterConfig.instance().load();
    }

    public RateLimiterInterceptorChain obtainInterceptorChain(RateLimiterInterceptorChain chain) {
        if (chain == null) {
            chain = new RateLimiterInterceptorChain();
            List<RateLimiterInterceptor> interceptors = obtainLimiterInterceptors(null);
            chain.addInterceptors(interceptors);
        }
        return chain;
    }

    public List<RateLimiterInterceptor> obtainLimiterInterceptors(List<RateLimiterInterceptor> interceptors) {
        if (interceptors == null) {
            interceptors = ExtensionLoader.getExtensionList(RateLimiterInterceptor.class, false);
        }
        if (interceptors == null) {
            interceptors = Collections.EMPTY_LIST;
        }
        return interceptors;
    }

    public RuleConfigSource obtainRuleConfigSource(RuleConfigSource ruleConfigSource) {
        if (ruleConfigSource == null) {
            ruleConfigSource = ExtensionLoader.getExtension(RuleConfigSource.class, false);
        }
        if (ruleConfigSource == null) {
            String sourceType = RateLimiterConfig.instance().getRuleConfigSourceType();
            if (sourceType.equals("zookeeper")) {
                ruleConfigSource = new ZookeeperRuleConfigSource();
            } else if (sourceType.equals("file")) {
                ruleConfigSource = new FileRuleConfigSource();
            }
        }
        if (ruleConfigSource == null) {
            ruleConfigSource = new FileRuleConfigSource();
        }
        return ruleConfigSource;
    }

    public RuleConfigParser obtainRuleConfigParser(RuleConfigParser ruleConfigParser) {
        if (ruleConfigParser == null) {
            ruleConfigParser = ExtensionLoader.getExtension(RuleConfigParser.class, false);
        }

        if (ruleConfigParser == null) {
            String parseType = RateLimiterConfig.instance().getRuleConfigParseType();
            if (parseType.equals("yaml")) {
                ruleConfigParser = new YamlRuleConfigParser();
            } else if (parseType.equals("json")) {
                ruleConfigParser = new JsonRuleConfigParser();
            }
        }
        if (ruleConfigParser == null) {
            ruleConfigParser = new YamlRuleConfigParser();
        }
        return ruleConfigParser;
    }

    public JedisTaskExecutor obtainJedisTaskExecutor(JedisTaskExecutor jedisTaskExecutor) {
        if (jedisTaskExecutor != null) {
            return jedisTaskExecutor;
        }
        RedisConfig redisConfig = RateLimiterConfig.instance().getRedisConfig();
        GenericObjectPoolConfig poolConfig = redisConfig.getPoolConfig();
        String address = redisConfig.getAddress();
        int timeout = redisConfig.getTimeout();
        return new DefaultJedisTaskExecutor(address, timeout, poolConfig);
    }

    public RateLimitRule obtainUrlRateLimitRule(RateLimitRule rateLimitRule) {
        if (rateLimitRule != null) {
            return rateLimitRule;
        }
        rateLimitRule = new UrlRateLimitRule();
        return rateLimitRule;
    }

}
