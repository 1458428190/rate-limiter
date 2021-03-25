package org.gucha.ratelimiter.core.framework.algorithm;

import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/25 上午11:22
 */
public class FixedTimeWindowRateLimiter implements RateLimiter{

    /**
     * timeout for {@code Lock.tryLock() }.
     */
    private static final long TRY_LOCK_TIMEOUT = 200L;

    private StopWatch stopWatch;

    private AtomicInteger currentCount = new AtomicInteger(0);

    /* the max permitted access count persecond */
    private final int limit;

    private Lock lock = new ReentrantLock();

    public FixedTimeWindowRateLimiter(int limit) {
        this(limit, StopWatch.createStarted());
    }

    public FixedTimeWindowRateLimiter(int limit, StopWatch stopWatch) {
        this.limit = limit;
        this.stopWatch = stopWatch;
    }

    @Override
    public boolean tryAcquire() {
        return false;
    }
}
