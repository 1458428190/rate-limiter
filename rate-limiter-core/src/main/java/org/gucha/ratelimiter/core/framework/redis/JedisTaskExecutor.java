package org.gucha.ratelimiter.core.framework.redis;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:58
 */
public interface JedisTaskExecutor {
    Object eval(String luaScript);

    Object eval(String luaScript, String key, String params);

    Object evalsha(String sha1, String key, String params);

    String set(String key, String value);
}
