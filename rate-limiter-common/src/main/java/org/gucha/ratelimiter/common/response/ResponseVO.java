package org.gucha.ratelimiter.common.response;

import lombok.Data;

import java.util.HashMap;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/12 下午2:57
 */
@Data
public class ResponseVO {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态信息
     */
    private String desc;

    /**
     * 结果数据
     */
    private Object result;

    /**
     * 错误信息
     */
    private String errorMsg;

    public ResponseVO(ResponseStatus responseStatus, Object result) {
        this.code = responseStatus.getCode();
        this.desc = responseStatus.getDesc();
        this.errorMsg = responseStatus.getErrorMsg();
        this.result = result;
    }

    public ResponseVO(ResponseStatus responseStatus) {
        this.code = responseStatus.getCode();
        this.desc = responseStatus.getDesc();
        this.errorMsg = responseStatus.getErrorMsg();
        // 规范默认{}
        this.result = new HashMap<>(1);
    }

}
