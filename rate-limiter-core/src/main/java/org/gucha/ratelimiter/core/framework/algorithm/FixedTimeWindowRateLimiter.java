package org.gucha.ratelimiter.core.framework.algorithm;

import com.google.common.base.Stopwatch;
import org.gucha.ratelimiter.common.exception.InternalErrorException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/25 上午11:22
 */
public class FixedTimeWindowRateLimiter implements RateLimiter {

    /**
     * timeout for {@code Lock.tryLock() }.
     */
    private static final long TRY_LOCK_TIMEOUT = 200L;

    private Stopwatch stopwatch;

    /**
     * 使用tomicInteger做原子操作
     */
    private AtomicInteger currentCount = new AtomicInteger(0);

    /* the max permitted access count persecond */
    private final int limit;

    private Lock lock = new ReentrantLock();

    public FixedTimeWindowRateLimiter(int limit) {
        this(limit, Stopwatch.createStarted());
    }

    public FixedTimeWindowRateLimiter(int limit, Stopwatch stopwatch) {
        this.limit = limit;
        this.stopwatch = stopwatch;
    }

    /**
     * try to acquire an access token.
     * TODO 时间窗口支持可配置.
     * 问题: 无法处理聚集请求,比如每秒阈值是3, 那在2.9s到3.1s之前是可以连续请求6次, 因为他们不在一个时间单位里.
     * 时间间隔: 首次以启动时间开始计时,后续以次数触发到limit+1的请求开始计时.
     *
     * @return true if get an access token successfully, otherwise, return false.
     */
    @Override
    public boolean tryAcquire() throws InternalErrorException {
        // 优先判断次数是否超过阈值,没超过就直接通过
        int updatedCount = currentCount.incrementAndGet();
        if (updatedCount <= limit) {
            return true;
        }
        // 次数超过阈值,判断是否为一个有效时间窗口内
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                // 运行时长已经超过1s
                if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(1)) {
                    currentCount.set(0);
                    stopwatch.reset();
                }
                updatedCount = currentCount.incrementAndGet();
                return updatedCount <= limit;
            } else {
                throw new InternalErrorException("tryAcquire() wait lock too long:" + TRY_LOCK_TIMEOUT + "ms");
            }
        } catch (InterruptedException e) {
            throw new InternalErrorException("tryAcquire() is interrupted by lock-time-out.", e);
        }
    }
}
