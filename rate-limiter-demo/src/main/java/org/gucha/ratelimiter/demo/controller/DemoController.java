package org.gucha.ratelimiter.demo.controller;

import org.gucha.ratelimiter.core.annotation.RateLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/12 上午10:40
 */
@RestController
public class DemoController {

    @GetMapping("/demo")
    @RateLimiter
    public String demo() {
        return "hello world!";
    }
}
