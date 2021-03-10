package org.gucha.ratelimiter.core.util;

import org.gucha.ratelimiter.captcha.utils.CaptchaUtil;
import org.gucha.ratelimiter.core.module.VerifyImageDTO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 验证码工具
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:07
 */
public class VerifyImageUtils {
    private static final String VERIFY_CODE_KEY = "rate-limiter_verify_code_";

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 生成验证码
     *
     * @return
     */
    public VerifyImageDTO generateVerifyImg() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = CaptchaUtil.out(outputStream);
        // 图片流, 可用于页面直接显示
        String base64Image = "data:image/jpeg:base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        // 使用uuid做唯一验证id
        String verifyId = UUID.randomUUID().toString();
        return new VerifyImageDTO(verifyId, null, base64Image, result);
    }

    /**
     * 保存验证码信息到外部存储中, 用于验证
     *
     * @param verifyImageDTO
     */
    public void saveVerifyCodeToExternalStorage(VerifyImageDTO verifyImageDTO) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY.concat(verifyImageDTO.getVerifyId()));
        rBucket.set(verifyImageDTO.getResult(), 60, TimeUnit.SECONDS);
    }

    /**
     * 删除验证码信息
     * @param verifyId
     */
    public void deleteVerifyCodeFromExternalStorage(String verifyId) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY.concat(verifyId));
        rBucket.delete();
    }

    /**
     * 获取验证码实际结果
     * @param verifyId
     * @return
     */
    public String getVerifyCodeFromExternalStorage(String verifyId) {
        RBucket<String> rBucket = redissonClient.getBucket(VERIFY_CODE_KEY.concat(verifyId));
        return rBucket.get();
    }
}
