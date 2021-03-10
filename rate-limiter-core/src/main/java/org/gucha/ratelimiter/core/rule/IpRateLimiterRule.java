package org.gucha.ratelimiter.core.rule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.gucha.ratelimiter.common.util.TimeUtils;
import org.gucha.ratelimiter.core.config.RateLimiterProperties;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:02
 */
@Slf4j
public class IpRateLimiterRule extends AbstractRule {

    /**
     * 限流计数器请求数
     */
    private static final String RATE_LIMITER_COUNT_PREFIX = "rate-limiter_request_count";
    /**
     * 限流器时间窗口
     */
    private static final String RATE_LIMITER_EXPIRATION_TIME_PREFIX = "rate-limiter_expiration_time";
    /**
     * 限流器 限制中的ip
     */
    private static final String RATE_LIMITER_HIT_IP = "rate-limiter_hit_ip";

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RateLimiterProperties properties;

    /**
     * TODO 待验证
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean doExecute(HttpServletRequest request, HttpServletResponse response) {
        String ipAddress = getIpAddr(request);
        RateLimiterProperties.IpRule ipRule = properties.getIpRule();
        List<String> ignoreIpList = ipRule.getIgnoreIps();
        if (CollectionUtils.isNotEmpty(ignoreIpList)) {
            for (String ignoreIp : ignoreIpList) {
                if (ignoreIp.endsWith("*")) {
                    ignoreIp = ignoreIp.substring(0, ignoreIp.length() - 1);
                }
                if (ipAddress.startsWith(ignoreIp)) {
                    return false;
                }
            }
        }
        String requestUrl = request.getRequestURI();
        int expirationTime = ipRule.getExpirationTime();
        int requestMaxSize = ipRule.getRequestMaxSize();
        RAtomicLong rRequestCount = redissonClient.getAtomicLong(RATE_LIMITER_COUNT_PREFIX.concat(requestUrl).concat(ipAddress));
        RAtomicLong rExpirationTime = redissonClient.getAtomicLong(RATE_LIMITER_EXPIRATION_TIME_PREFIX.concat(requestUrl).concat(ipAddress));
        // 时间窗口已经失效, 重新计数
        if (!rExpirationTime.isExists()) {
            rRequestCount.set(0L);
            rExpirationTime.set(0L);
            rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
        } else {
            // 还在时间窗口内
            RMap rHitMap = redissonClient.getMap(RATE_LIMITER_HIT_IP);
            if (rRequestCount.incrementAndGet() > requestMaxSize || rHitMap.containsKey(ipAddress)) {
                // 触发限流策略, 默认1天后可重新访问
                long lockExpire = ipRule.getLockExpire();
                rExpirationTime.expire(lockExpire, TimeUnit.SECONDS);
                // 记录限制的ip, 该ip将短期内无法访问所有限流接口
                rHitMap.put(ipAddress, requestUrl);
                log.warn("[op: doExecute, request limit, uri:{}, ip:{}, request:{}, times in {} ms. automatically unfreeze until {}]",
                        requestUrl, ipAddress, requestMaxSize, expirationTime, TimeUtils.format(System.currentTimeMillis() + lockExpire * 1000));
                return true;
            }
        }
        return false;
    }

    /**
     * 重置已记录的规则
     *
     * @param request
     * @param realRequestUrl
     * @return
     */
    @Override
    public boolean reset(HttpServletRequest request, String realRequestUrl) {
        String ipAddress = getIpAddr(request);
        String requestUrl = realRequestUrl;
        // 重新计数
        int expirationTime = properties.getIpRule().getExpirationTime();
        RAtomicLong rRequestCount = redissonClient.getAtomicLong(RATE_LIMITER_COUNT_PREFIX.concat(requestUrl).concat(ipAddress));
        RAtomicLong rExpirationTime = redissonClient.getAtomicLong(RATE_LIMITER_EXPIRATION_TIME_PREFIX.concat(requestUrl).concat(ipAddress));
        rRequestCount.set(0L);
        rExpirationTime.set(0L);
        rExpirationTime.expire(expirationTime, TimeUnit.MILLISECONDS);
        // 清除ip限制记录
        RMap rHitMap = redissonClient.getMap(RATE_LIMITER_HIT_IP);
        rHitMap.remove(ipAddress);
        return true;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
