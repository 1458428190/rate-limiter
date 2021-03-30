package org.gucha.ratelimiter.core.framework.monitor;

import lombok.extern.slf4j.Slf4j;
import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

import java.text.DecimalFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:32
 */
@Slf4j
public class MonitorManager {
    public static final int STATISTIC_PERIOD = 60;
    private static final MetricsCounter metricsCounter = new MetricsCounter();

    private static final ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(1,
            r -> new Thread(r, "ratelimiter-monitor-thread"));

    static {
        scheduledExecutor.scheduleAtFixedRate(() -> writeLog(), STATISTIC_PERIOD, STATISTIC_PERIOD, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!scheduledExecutor.isShutdown()) {
                scheduledExecutor.shutdown();
            }
        }));
    }

    public static void collect(String appId, String url, ApiLimit apiLimit, long duration, boolean result, Exception ex) {
        metricsCounter.increment(MetricType.TOTAL);
        if (result) {
            metricsCounter.increment(MetricType.PASSED);
        } else if (ex == null) {
            metricsCounter.increment(MetricType.LIMITED);
        }
        if (ex != null) {
            metricsCounter.increment(MetricType.EXCEPTION);
        }
        metricsCounter.add(MetricType.DURATION, duration);
    }

    public static void writeLog() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        long total = metricsCounter.sumAndReset(MetricType.TOTAL);
        if (total == 0) {
            return;
        }
        long passed = metricsCounter.sumAndReset(MetricType.PASSED);
        long limited = metricsCounter.sumAndReset(MetricType.LIMITED);
        long exception = metricsCounter.sumAndReset(MetricType.EXCEPTION);
        float duration = metricsCounter.sumAndReset(MetricType.DURATION) / 1000f;
        float avgDuration = duration / total;
        log.info("[ratelimiter statistics] total:{}, passed:{}, limited:{}, exception:{}, avg duration:{}",
                total, passed, limited, exception, decimalFormat.format(avgDuration));
    }
}
