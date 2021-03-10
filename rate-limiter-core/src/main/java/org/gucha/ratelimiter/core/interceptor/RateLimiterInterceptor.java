package org.gucha.ratelimiter.core.interceptor;

import org.gucha.ratelimiter.core.annotation.RateLimiter;
import org.gucha.ratelimiter.core.config.RateLimiterProperties;
import org.gucha.ratelimiter.core.module.VerifyImageDTO;
import org.gucha.ratelimiter.core.rule.RateLimiterRuleActuator;
import org.gucha.ratelimiter.core.util.CrosUtils;
import org.gucha.ratelimiter.core.util.VerifyImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:59
 */
@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter {

    private String rateLimiterForm;

    @Autowired
    private RateLimiterRuleActuator actuator;

    @Autowired
    private RateLimiterProperties properties;

    @Autowired
    private VerifyImageUtils verifyImageUtils;

    private AtomicBoolean initialized = new AtomicBoolean(false);

    public void init() {
        // 加载通用的验证码页面
        ClassPathResource classPathResource = new ClassPathResource("verify/index.html");
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            this.rateLimiterForm = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!initialized.get()) {
            init();
            initialized.set(true);
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 有RateLimiter注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        boolean isRateLimiterAnnotation = rateLimiter != null;
        String requestUrl = request.getRequestURI();
        if (isIntercept(requestUrl, isRateLimiterAnnotation) && !actuator.isAllowed(request, response)) {
            CrosUtils.setCrosHeader(response);
            response.setStatus(509);
            // 生成验证码图片
            VerifyImageDTO verifyImageDTO = verifyImageUtils.generateVerifyImg();
            verifyImageUtils.saveVerifyCodeToExternalStorage(verifyImageDTO);
            String str1 = this.rateLimiterForm.replaceAll("verifyId_value", verifyImageDTO.getVerifyId());
            String str2 = str1.replaceAll("verifyImg_value", verifyImageDTO.getVerifyImgStr());
            String str3 = str2.replaceAll("realRequestUri_value", requestUrl);
            response.getWriter().write(str3);
            response.getWriter().close();
            return false;
        }
        return true;
    }

    public boolean isIntercept(String requestUrl, boolean isRateLimiterAnnotation) {
        // 全局 || 注解 || url包含
        if (this.properties.isGlobalFilterMode() || isRateLimiterAnnotation || properties.getUrls().contains(requestUrl)) {
            return true;
        }
        // 正则
        for (String url : properties.getUrls()) {
            if (Pattern.matches(url, requestUrl)) {
                return true;
            }
        }
        return false;
    }
}

