package org.gucha.ratelimiter.core.config;

import org.gucha.ratelimiter.core.controller.CaptchaController;
import org.gucha.ratelimiter.core.handle.GlobalExceptionHandler;
import org.gucha.ratelimiter.core.interceptor.RateLimiterInterceptor;
import org.gucha.ratelimiter.core.rule.IpRateLimiterRule;
import org.gucha.ratelimiter.core.rule.RateLimiterRule;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.gucha.ratelimiter.core.service.CaptchaService;
import org.gucha.ratelimiter.core.util.VerifyImageUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/08 下午7:55
 */
@Configuration
@EnableConfigurationProperties(RateLimiterProperties.class)
@ConditionalOnProperty(prefix = "rate-limiter", value = "enabled", havingValue = "true")
@Import({RedissonAutoConfig.class, WebMvcConfig.class, CaptchaController.class, GlobalExceptionHandler.class})
public class RateLimiterAutoConfig {

    @Bean
    @ConditionalOnProperty(prefix = "rate-limiter.ip-rule", value = "enabled", havingValue = "true", matchIfMissing = true)
    public IpRateLimiterRule ipRateLimiterRule() {
        return new IpRateLimiterRule();
    }

    @Bean
    public VerifyImageUtils verifyImageUtils() {
        return new VerifyImageUtils();
    }

    @Bean
    public RateLimiterRuleActuator rateLimiterRuleActuator(final List<RateLimiterRule> rules) {
        final List<RateLimiterRule> rateLimiterRules = rules.stream()
                .sorted(Comparator.comparingInt(RateLimiterRule::getOrder)).collect(Collectors.toList());
        return new RateLimiterRuleActuator(rateLimiterRules);
    }

    @Bean
    public CaptchaService validateFormService() {
        return new CaptchaService();
    }

    @Bean
    public RateLimiterInterceptor rateLimiterInterceptor() {
        return new RateLimiterInterceptor();
    }
}
