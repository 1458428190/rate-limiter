package org.gucha.ratelimiter.common.exception;

import org.gucha.ratelimiter.common.response.ResponseStatus;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/25 上午11:33
 */
public class RateLimitException extends RuntimeException {

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    private ResponseStatus responseStatus;

    public RateLimitException() {
    }

    public RateLimitException(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public RateLimitException(String message, ResponseStatus responseStatus) {
        super(message);
        this.responseStatus = responseStatus;
    }

    public RateLimitException(String message, Throwable cause, ResponseStatus responseStatus) {
        super(message, cause);
        this.responseStatus = responseStatus;
    }

    public RateLimitException(Throwable cause, ResponseStatus responseStatus) {
        super(cause);
        this.responseStatus = responseStatus;
    }

    public RateLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ResponseStatus responseStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseStatus = responseStatus;
    }

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public RateLimitException(Throwable cause) {
        super(cause);
    }

    public RateLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
