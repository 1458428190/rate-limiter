package org.gucha.ratelimiter.common.util;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:51
 */
public class TimeUtils {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final long MINUTE_IN_MS = 60 * 1000;

    public static final long HOUR_IN_MS = 60 * MINUTE_IN_MS;

    public static final long DAY_IN_MS = 24 * HOUR_IN_MS;

    public static String format(long timestamp) {
        return DateFormatUtils.format(timestamp, DEFAULT_PATTERN);
    }

    public static String format(long timestamp, String pattern) {
        return DateFormatUtils.format(timestamp, pattern);
    }

    @SneakyThrows
    public static long getTime(String timeStr) {
        return DateUtils.parseDate(timeStr, DEFAULT_PATTERN).getTime();
    }

    @SneakyThrows
    public static long getTime(String timeStr, String pattern) {
        return DateUtils.parseDate(timeStr, pattern).getTime();
    }

    /**
     * 获取指定时间的倒计时 **天**小时**分
     *
     * @param timestamp 时间戳 毫秒单位
     * @return
     */
    public static String getCountDown(long timestamp) {
        long nowTime = System.currentTimeMillis();
        if (timestamp < nowTime) {
            return "0分";
        }
        long countDown = timestamp - nowTime;
        long day = countDown / DAY_IN_MS;
        long hour = countDown % DAY_IN_MS / HOUR_IN_MS;
        long minute = countDown % HOUR_IN_MS / MINUTE_IN_MS;
        // 全是0, 计做1分
        if (day == 0 && hour == 0 && minute == 0) {
            minute = 1;
        }
        return day + "天" + hour + "小时" + minute + "分";
    }

    /**
     * 获取指定时间的前n小时整点的开始和结束时间
     */
    public static long[] getStartEndNHourTime(long timestamp, int nHour) {
        Calendar caleEnd = Calendar.getInstance();
        caleEnd.setTimeInMillis(timestamp);
        caleEnd.set(Calendar.MINUTE, 0);
        caleEnd.set(Calendar.SECOND, 0);
        caleEnd.set(Calendar.MILLISECOND, 0);
        long[] result = new long[2];
        result[1] = caleEnd.getTimeInMillis();
        result[0] = result[1] - nHour * 3600000;
        return result;
    }

    /**
     * 获取指定时间戳的当天开始和结束时间
     *
     * @param timestamp
     * @return
     */
    public static long[] getStartEndDayTime(long timestamp) {
        Calendar caleStart = Calendar.getInstance();
        caleStart.setTimeInMillis(timestamp);
        caleStart.set(Calendar.HOUR_OF_DAY, 0);
        caleStart.set(Calendar.MINUTE, 0);
        caleStart.set(Calendar.SECOND, 0);
        caleStart.set(Calendar.MILLISECOND, 0);

        Calendar caleEnd = Calendar.getInstance();
        caleEnd.setTimeInMillis(timestamp);
        caleEnd.set(Calendar.HOUR_OF_DAY, 23);
        caleEnd.set(Calendar.MINUTE, 59);
        caleEnd.set(Calendar.SECOND, 59);
        caleEnd.set(Calendar.MILLISECOND, 999);
        long[] result = new long[2];
        result[0] = caleStart.getTimeInMillis();
        result[1] = caleEnd.getTimeInMillis();
        return result;
    }

    public static int getMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前月第一天开始时间
     *
     * @return
     */
    public static Date getThisMonthFirstDayStartTime() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 获取当前月最后一天结束时间
     *
     * @return
     */
    public static Date getThisMonthLastDayEndTime() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        cale.set(Calendar.SECOND, 59);
        cale.set(Calendar.MILLISECOND, 999);
        return cale.getTime();
    }

    /**
     * 获取最近的5分钟开始时间
     *
     * @param
     */
    public static Date getStartFiveMinute() {
        Calendar cale = Calendar.getInstance();
        int minute = cale.get(Calendar.MINUTE);
        minute = minute / 5 * 5;
        cale.set(Calendar.MINUTE, minute);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 获取今天开始时间
     */
    public static Date getStartTimeOnToday() {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, 0);
        cale.set(Calendar.MINUTE, 0);
        cale.set(Calendar.SECOND, 0);
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    /**
     * 获取今天结束时间
     */
    public static Date getEndTimeOnToday() {
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        cale.set(Calendar.SECOND, 59);
        cale.set(Calendar.MILLISECOND, 999);
        return cale.getTime();
    }

    /**
     * 获取当前时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNowTime() {
        return format(System.currentTimeMillis());
    }

    /**
     * 获取格式化时间
     * example1: timePattern=yyyy-MM-dd 09:00:00  return 当日9点
     * example2: timePattern=yyyy-MM-01 09:00:00 return 当月1日，9点
     * example3: timePattern=yyyy-MM-dd HH:mm:ss return 当前时间
     *
     * @param timePattern
     * @return
     */
    public static long getFormatTime(String timePattern) {
        if (StringUtils.isBlank(timePattern)) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String timeStr = timePattern.replace("yyyy", String.valueOf(year))
                .replace("MM", String.valueOf(month))
                .replace("dd", String.valueOf(day))
                .replace("HH", String.valueOf(hour))
                .replace("mm", String.valueOf(minute))
                .replace("ss", String.valueOf(second));
        long result = getTime(timeStr);
        return result > System.currentTimeMillis() ? result : 0;
    }
}
