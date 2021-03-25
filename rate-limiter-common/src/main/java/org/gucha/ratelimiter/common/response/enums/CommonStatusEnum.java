package org.gucha.ratelimiter.common.response.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.gucha.ratelimiter.common.response.ResponseStatus;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/12 下午5:26
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements ResponseStatus {

    /**
     * 各状态码
     */
    SUCCESS(200, "SUCCESS"),

    OPERATE_FREQUENT(10001, "OPERATE_FREQUENT", "操作太频繁,请稍后再试"),

    CAPTCHA_ERROR(10002, "CAPTCHA_ERROR", "验证码错误,请重新输入");

    private int code;

    private String desc;

    private String errorMsg;

    CommonStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
