package org.gucha.ratelimiter.core.framework.interceptor;

import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.gucha.ratelimiter.core.framework.extension.OrderComparator;
import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:12
 */
@Getter
public class RateLimiterInterceptorChain {
    private final List<RateLimiterInterceptor> interceptors;

    public RateLimiterInterceptorChain() {
        this.interceptors = new ArrayList<>();
    }

    public void doBeforeLimit(String appId, String api) {
        for (int i = 0; i < interceptors.size(); i++) {
            interceptors.get(i).beforeLimit(appId, api);
        }
    }

    public void doAfterLimit(String appId, String url, ApiLimit apiLimit, boolean result, Exception ex) {
        for (int i = 0; i < interceptors.size(); i++) {
            interceptors.get(i).afterLimiter(appId, url, apiLimit, result, ex);
        }
    }

    public void addInterceptor(RateLimiterInterceptor interceptor) {
        this.interceptors.add(interceptor);
        Collections.sort(this.interceptors, OrderComparator.INSTANCE);
    }

    public void addInterceptors(List<RateLimiterInterceptor> interceptors) {
        this.interceptors.addAll(interceptors);
        Collections.sort(this.interceptors, OrderComparator.INSTANCE);
    }

    public void clear() {
        this.interceptors.clear();
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(interceptors);
    }

}
