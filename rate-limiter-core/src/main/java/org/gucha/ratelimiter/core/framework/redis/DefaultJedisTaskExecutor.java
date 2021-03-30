package org.gucha.ratelimiter.core.framework.redis;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.env.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Description: TODO key可以指定为项目名前缀,支持redisTemplate
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:59
 */
@AllArgsConstructor
public class DefaultJedisTaskExecutor implements JedisTaskExecutor {

    public static final String DEFAULT_REDIS_KEY_PREFIX = "rt:";

    private JedisPool pool;

    private String redisKeyPrefix = DEFAULT_REDIS_KEY_PREFIX;

    public DefaultJedisTaskExecutor(JedisPool jedisPool) {
        this(jedisPool, DEFAULT_REDIS_KEY_PREFIX);
    }

    public DefaultJedisTaskExecutor(String address, int timeout, GenericObjectPoolConfig poolConfig) {
        this(address, timeout, poolConfig, DEFAULT_REDIS_KEY_PREFIX);
    }

    public DefaultJedisTaskExecutor(String address, int timeout, GenericObjectPoolConfig poolConfig, String prefix) {
        if (StringUtils.isBlank(address)) {
            throw new ConfigurationException("redis address is empty.");
        }
        String[] ipAndPort = address.split(":");
        String host = ipAndPort[0];
        int port = RedisConfig.DEFAULT_PORT;
        if (ipAndPort.length >= 2) {
            try {
                port = Integer.parseInt(ipAndPort[1]);
            } catch (NumberFormatException e) {
            }
        }
        if (poolConfig == null) {
            poolConfig = new DefaultJedisPoolConfig();
        }
        this.pool = new JedisPool(poolConfig, host, port, timeout);
        this.redisKeyPrefix = prefix;
    }

    @Override
    public Object eval(String luaScript) {
        return execute(jedis -> jedis.eval(luaScript));
    }

    @Override
    public Object eval(String luaScript, String key, String params) {
        return execute(jedis -> jedis.eval(luaScript, Lists.newArrayList(redisKeyPrefix + key),
                Lists.newArrayList(params)));
    }

    @Override
    public Object evalsha(String sha1, String key, String params) {
        return execute(jedis -> jedis.evalsha(sha1, Lists.newArrayList(redisKeyPrefix + key),
                Lists.newArrayList(params)));
    }

    @Override
    public String set(String key, String value) {
        return execute(jedis -> jedis.set(key, value));
    }

    private <T> T execute(JedisTask<T> task) {
        T result;
        try (Jedis jedis = pool.getResource()) {
            result = task.run(jedis);
        }
        return result;
    }
}
