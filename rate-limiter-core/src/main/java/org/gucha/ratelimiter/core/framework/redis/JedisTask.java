package org.gucha.ratelimiter.core.framework.redis;

import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:57
 */
public interface JedisTask<T> {
     T run(Jedis jedis);
}
