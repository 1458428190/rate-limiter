package org.gucha.ratelimiter.core.config;

import org.gucha.ratelimiter.core.rule.IpRateLimiterRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/08 下午7:55
 */
@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
@ConditionalOnProperty(prefix = "rate-limiter", value = "enable", havingValue = "true")
@Import({RedissonAutoConfig.class, WebMvcConfig.class})
public class RateLimiterAutoConfig {

    @Bean
    @ConditionalOnProperty(prefix = "rate-limiter.ip-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
    public IpRateLimiterRule ipRateLimiterRule() {
        return new IpRateLimiterRule();
    }


}
