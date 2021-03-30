package org.gucha.ratelimiter.core.framework.interceptor;

import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:47
 */
public interface RateLimiterInterceptor {
    /**
     * 限流器前置处理
     * @param appId
     * @param api
     */
    void beforeLimit(String appId, String api);

    /**
     * 限流器后置处理
     * @param appId
     * @param api
     * @param apiLimit
     * @param result
     * @param e
     */
    void afterLimiter(String appId, String api, ApiLimit apiLimit, boolean result, Exception e);
}
