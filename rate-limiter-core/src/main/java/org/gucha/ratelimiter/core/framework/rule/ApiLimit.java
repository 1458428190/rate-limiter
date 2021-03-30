package org.gucha.ratelimiter.core.framework.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiLimit {
    /**
     * 默认时间单位 1s
     */
    private static final int DEFAULT_TIME_UNIT = 1;

    private String api;

    private int limit;

    private int unit = DEFAULT_TIME_UNIT;

    public ApiLimit(String api, int limit) {
        this(api, limit, DEFAULT_TIME_UNIT);
    }
}
