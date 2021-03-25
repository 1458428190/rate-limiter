package org.gucha.ratelimiter.core.controller;

import org.gucha.ratelimiter.common.response.ResponseVO;
import org.gucha.ratelimiter.common.response.enums.CommonStatusEnum;
import org.gucha.ratelimiter.core.module.VerifyImageVO;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.gucha.ratelimiter.core.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/12 下午2:20
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private RateLimiterRuleActuator rateLimiterRuleActuator;

    /**
     * 获取/刷新 图片验证码
     *
     * @return
     */
    @GetMapping(value = "/refresh", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] refresh(HttpServletRequest request) {
        VerifyImageVO refresh = captchaService.refresh(request);
        return refresh.getVerifyImg();
    }

    /**
     * 验证图片验证码
     *
     * @return
     */
    @PostMapping("/verify")
    public ResponseVO verify(HttpServletRequest request, String id, String result, String realRequestUrl) {
        boolean verifyResult = captchaService.verify(id, result);
        // 如果验证通过, 进行重置
        if (verifyResult) {
            rateLimiterRuleActuator.reset(request, realRequestUrl);
        }
        return new ResponseVO(CommonStatusEnum.SUCCESS, verifyResult);
    }
}
