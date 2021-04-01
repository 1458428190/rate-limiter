package org.gucha.ratelimiter.core.framework.interceptor;

import com.google.auto.service.AutoService;
import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.monitor.MonitorManager;
import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/9 下午4:09
 */
@Order(Order.LOWEST_PRECEDENCE)
@AutoService(RateLimiterInterceptor.class)
public class MonitorInterceptor extends RateLimiterInterceptorAdapter {
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public void beforeLimit(String appId, String api) {
        startTime.set(System.nanoTime());
    }

    @Override
    public void afterLimiter(String appId, String api, ApiLimit apiLimit, boolean result, Exception ex) {
        long startNano = startTime.get();
        startTime.remove();
        long duration = (System.nanoTime() - startNano) / 1000;
        MonitorManager.collect(appId, api, apiLimit, duration, result, ex);
    }
}
