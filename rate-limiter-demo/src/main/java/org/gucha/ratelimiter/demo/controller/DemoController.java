package org.gucha.ratelimiter.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.gucha.ratelimiter.common.exception.InternalErrorException;
import org.gucha.ratelimiter.common.exception.InvalidUrlException;
import org.gucha.ratelimiter.common.exception.OverloadException;
import org.gucha.ratelimiter.core.annotation.RateLimiter;
import org.gucha.ratelimiter.core.framework.MemoryUrlRateLimiter;
import org.gucha.ratelimiter.core.framework.UrlRateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 限流器相关案例demo
 * @Author : laichengfeng
 * @Date : 2021/03/12 上午10:40
 */
@RestController
@Slf4j
public class DemoController {


    private static UrlRateLimiter urlRateLimiter = new MemoryUrlRateLimiter();

    @GetMapping("/demo")
    @RateLimiter
    public String demo() {
        return "hello world!";
    }

    @GetMapping("/rateLimit/demo")
    public String rateLimitDemo() {
        try {
            urlRateLimiter.limit("app-1", "http://127.0.0.1:8080/rateLimit/demo");
        } catch (InternalErrorException e) {
            log.error("internal error ", e);
            return "error";
        } catch (OverloadException e) {
            return "overload";
        } catch (InvalidUrlException e) {
            return "invalid url";
        }
        return "pass";
    }
}
