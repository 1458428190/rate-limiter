package org.gucha.ratelimiter.core.framework.algorithm;

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
    boolean tryAcquire();

}
