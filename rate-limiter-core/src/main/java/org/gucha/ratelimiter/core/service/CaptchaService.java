package org.gucha.ratelimiter.core.service;

import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.module.VerifyImageDTO;
import org.gucha.ratelimiter.core.module.VerifyImageVO;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.gucha.ratelimiter.core.util.VerifyImageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:09
 */
public class CaptchaService {

    @Autowired
    private RateLimiterRuleActuator rateLimiterRuleActuator;

    @Autowired
    private VerifyImageUtils verifyImageUtils;

    /**
     * 验证码 验证
     *
     * @return
     */
    public boolean verify(String id, String result) {
        String actualResult = verifyImageUtils.getVerifyCodeFromExternalStorage(id);
        return StringUtils.equalsIgnoreCase(actualResult, result);
    }

    /**
     * 刷新验证码
     *
     * @param request
     * @return
     */
    public VerifyImageVO refresh(HttpServletRequest request) {
        String verifyId = request.getParameter("verifyId");
        if (StringUtils.isNotBlank(verifyId)) {
            verifyImageUtils.deleteVerifyCodeFromExternalStorage(verifyId);
        }
        // 生成验证码
        VerifyImageDTO verifyImageDTO = verifyImageUtils.generateVerifyImg();
        verifyImageUtils.saveVerifyCodeToExternalStorage(verifyImageDTO);
        VerifyImageVO verifyImageVO = new VerifyImageVO();
        BeanUtils.copyProperties(verifyImageDTO, verifyImageVO);
        return verifyImageVO;
    }
}
