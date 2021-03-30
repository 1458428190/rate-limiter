package org.gucha.ratelimiter.core.framework.algorithm;

import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gucha.ratelimiter.common.exception.InternalErrorException;
import org.gucha.ratelimiter.core.framework.redis.JedisTaskExecutor;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.exceptions.JedisNoScriptException;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午5:44
 */
@Slf4j
@AllArgsConstructor
public class DistributedFixedTimeWindowRateLimiter implements RateLimiter {

    private final String key;
    private final int limit;

    private JedisTaskExecutor jedisTaskExecutor;

    public static final String REDIS_LIMIT_SCRIPT =
            "local key = KEYS[1] " +
                    "local limit = tonumber(ARGV[1]) " +
                    "local current = tonumber(redis.call('incr', key)) " +
                    "if current > limit then " +
                    "  return 0 " +
                    "elseif current == 1 then " +
                    "  redis.call('expire', key, '1') " +
                    "end " +
                    "return 1 ";

    /**
     * redis cache for lua script.
     */
    public static final String REDIS_LIMIT_SCRIPT_SHA1 = SecureUtil.sha1(REDIS_LIMIT_SCRIPT);

    /**
     * try to acquire an access token.
     * evalsha 传入给定的sha1校验码，执行缓存在服务器中的脚本,如果没有缓存.会返回JedisNoScriptException
     * <p>
     * 找不到缓存再执行eval.
     * 好处: 节省带宽, 不需要每次都传入脚本主体
     *
     * @return
     * @throws InternalErrorException
     */
    @Override
    public boolean tryAcquire() throws InternalErrorException {
        long result;
        try {
            result = (long) jedisTaskExecutor.evalsha(REDIS_LIMIT_SCRIPT_SHA1, key, String.valueOf(limit));
            return 1 == result;
        } catch (JedisNoScriptException e) {
            // reids 中没有找到lua脚本的sha1缓存
            log.warn("no lua script cache on redis server.", e);
        } catch (JedisException e) {
            throw new InternalErrorException("Read redis error.", e);
        }
        try {
            result = (long) jedisTaskExecutor.eval(REDIS_LIMIT_SCRIPT, key, String.valueOf(limit));
        } catch (JedisException e) {
            throw new InternalErrorException("Read redis error.", e);
        }
        return 1 == result;
    }
}
