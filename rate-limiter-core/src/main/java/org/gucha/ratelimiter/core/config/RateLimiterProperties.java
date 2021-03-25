package org.gucha.ratelimiter.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/08 下午7:55
 */
@ConfigurationProperties(prefix = "rate-limiter")
@Data
public class RateLimiterProperties {
    /**
     * 是否启用限流
     */
    private boolean enabled;

    /**
     * 是否启用全局拦截, 默认为false
     */
    private boolean globalFilterMode = false;

    /**
     * 非全局拦截下, 需要限流的接口列表
     */
    private List<String> urls = new ArrayList<>();

    /**
     * 基于请求IP的限流规则
     */
    private IpRule ipRule = new IpRule();

    @Data
    public static class IpRule {
        /**
         * 是否启用IP rule: 默认启用
         */
        private boolean enabled = true;

        /**
         * 时间窗口, 默认5000ms
         */
        private int expirationTime = 5000;

        /**
         * 最大请求数, 默认20
         */
        private int requestMaxSize = 20;

        /**
         * 命中规则后, 锁定期限, 默认1天, 单位: 秒(s)
         */
        private long lockExpire = TimeUnit.DAYS.toSeconds(1);

        /**
         * IP白名单, 支持后缀'*', 以','分隔
         */
        private List<String> ignoreIps;
    }
}
