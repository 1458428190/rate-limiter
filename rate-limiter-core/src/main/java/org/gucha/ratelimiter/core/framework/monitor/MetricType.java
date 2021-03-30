package org.gucha.ratelimiter.core.framework.monitor;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:22
 */
public enum MetricType {
    TOTAL,
    PASSED,
    LIMITED,
    TIMEOUT,
    EXCEPTION,
    DURATION
}
