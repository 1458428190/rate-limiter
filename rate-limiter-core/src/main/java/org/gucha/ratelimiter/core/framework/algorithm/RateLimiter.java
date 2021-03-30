package org.gucha.ratelimiter.core.framework.algorithm;

import org.gucha.ratelimiter.common.exception.InternalErrorException;

/**
 * @Description: 限流算法接口, 可扩展
 * @Author : laichengfeng
 * @Date : 2021/03/25 上午11:21
 */
public interface RateLimiter {

    /**
     * try to acquire an access token
     * @return
     */
    boolean tryAcquire() throws InternalErrorException;

}
