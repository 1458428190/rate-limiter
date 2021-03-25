package org.gucha.ratelimiter.core.interceptor;

import org.gucha.ratelimiter.common.exception.RateLimitException;
import org.gucha.ratelimiter.common.response.enums.CommonStatusEnum;
import org.gucha.ratelimiter.core.annotation.RateLimiter;
import org.gucha.ratelimiter.core.config.RateLimiterProperties;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:59
 */
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RateLimiterRuleActuator actuator;

    @Autowired
    private RateLimiterProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        Method method = ((HandlerMethod) handler).getMethod();
        // 有RateLimiter注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        boolean isRateLimiterAnnotation = rateLimiter != null;
        String requestUrl = request.getRequestURI();
        if (isIntercept(requestUrl, isRateLimiterAnnotation) && !actuator.isAllowed(request, response)) {
            throw new RateLimitException(CommonStatusEnum.OPERATE_FREQUENT);
        }
        return true;
    }

    public boolean isIntercept(String requestUrl, boolean isRateLimiterAnnotation) {
        // 全局 || 注解 || url包含
        if (this.properties.isGlobalFilterMode() || isRateLimiterAnnotation || properties.getUrls().contains(requestUrl)) {
            return true;
        }
        // 正则
        for (String url : properties.getUrls()) {
            if (Pattern.matches(url, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}

