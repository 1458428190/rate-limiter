package org.gucha.ratelimiter.common.exception;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 上午11:36
 */
public class InternalErrorException extends Exception{

    public InternalErrorException(String message) {
        super(message);
    }

    public InternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
