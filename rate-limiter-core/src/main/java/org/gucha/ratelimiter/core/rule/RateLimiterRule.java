package org.gucha.ratelimiter.core.rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:00
 */
public interface RateLimiterRule {

    /**
     * 限流规则具体实现
     */
    boolean execute(HttpServletRequest request, HttpServletResponse response);

    /**
     * 重置已记录规则
     */
    boolean reset(HttpServletRequest request, String realRequestUrl);

    /**
     * 规则优先级
     */
    int getOrder();
}
