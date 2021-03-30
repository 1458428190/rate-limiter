package org.gucha.ratelimiter.core.framework.monitor;

import java.util.concurrent.atomic.LongAdder;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:20
 */

public class MetricsCounter {
    /** LongAdder适合统计 */
    private final LongAdder[] counters;

    public MetricsCounter() {
        int length = MetricType.values().length;
        counters = new LongAdder[length];
        for (int i = 0; i < length; i++) {
            counters[i] = new LongAdder();
        }
    }

    public void increment(MetricType... types) {
        for (MetricType type : types) {
            getCounter(type).increment();
        }
    }

    public void add(MetricType type, long value) {
        getCounter(type).add(value);
    }

    public long sumAndReset(MetricType type) {
        return getCounter(type).sumThenReset();
    }

    private LongAdder getCounter(MetricType type) {
        return counters[type.ordinal()];
    }
}
