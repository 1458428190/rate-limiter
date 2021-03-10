package org.gucha.ratelimiter.core.rule;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:00
 */
public abstract class AbstractRule implements RateLimiterRule{

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) {
        return doExecute(request, response);
    }

    protected abstract boolean doExecute(HttpServletRequest request, HttpServletResponse response);

}
