package org.gucha.ratelimiter.core.framework.interceptor;

import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

/**
 * @Description: 简化实现类的实现
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:11
 */
public abstract class RateLimiterInterceptorAdapter implements RateLimiterInterceptor {

    @Override
    public void beforeLimit(String appId, String api) {

    }

    @Override
    public void afterLimiter(String appId, String api, ApiLimit apiLimit, boolean result, Exception e) {

    }
}
