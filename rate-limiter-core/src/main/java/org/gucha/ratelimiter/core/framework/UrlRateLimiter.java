package org.gucha.ratelimiter.core.framework;

import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.common.exception.InternalErrorException;
import org.gucha.ratelimiter.common.exception.InvalidUrlException;
import org.gucha.ratelimiter.common.exception.OverloadException;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptor;

import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:52
 */
public interface UrlRateLimiter {

    /**
     * check if the url request of the specified app exceds the max hit limit.
     * @param appId
     * @param url
     * @throws ConfigurationException
     */
    void limit(String appId, String url) throws InternalErrorException, OverloadException, InvalidUrlException;

    /**
     * add interceptor into the default interceptor chain. the interceptor will do some work
     * before/after the {@code UrlRateLimiter.limit} method.
     * @param interceptor
     */
    void addInterceptor(RateLimiterInterceptor interceptor);

    void addInterceptors(List<RateLimiterInterceptor> interceptors);

}
