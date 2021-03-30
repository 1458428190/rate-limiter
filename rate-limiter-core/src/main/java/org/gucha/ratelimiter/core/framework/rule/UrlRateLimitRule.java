package org.gucha.ratelimiter.core.framework.rule;

import org.gucha.ratelimiter.core.framework.extension.Order;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午6:00
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
public class UrlRateLimitRule implements RateLimitRule {

    private volatile ConcurrentHashMap<String, AppUrlRateLimitRule> limitRules = new ConcurrentHashMap<>();

}
