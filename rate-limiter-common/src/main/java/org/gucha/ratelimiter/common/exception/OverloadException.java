package org.gucha.ratelimiter.common.exception;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/30 下午4:27
 */
public class OverloadException extends Exception {

    public OverloadException(String message) {
        super(message);
    }

    public OverloadException(String message, Throwable e) {
        super(message, e);
    }
}
