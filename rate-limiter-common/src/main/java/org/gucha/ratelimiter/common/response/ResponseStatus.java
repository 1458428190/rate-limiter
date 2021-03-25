package org.gucha.ratelimiter.common.response;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/12 下午5:23
 */
public interface ResponseStatus {

    /**
     * 状态码
     * @return
     */
    int getCode();

    /**
     * 描述
     * @return
     */
    String getDesc();

    /**
     * 错误信息
     * @return
     */
    String getErrorMsg();
}
