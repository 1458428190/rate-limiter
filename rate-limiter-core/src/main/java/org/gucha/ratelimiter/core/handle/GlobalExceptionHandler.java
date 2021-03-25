package org.gucha.ratelimiter.core.handle;

import org.gucha.ratelimiter.common.exception.RateLimitException;
import org.gucha.ratelimiter.common.response.ResponseVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = RateLimitException.class)
	public ResponseVO exceptionHandler(RateLimitException e){
		return new ResponseVO(e.getResponseStatus());
    }
}