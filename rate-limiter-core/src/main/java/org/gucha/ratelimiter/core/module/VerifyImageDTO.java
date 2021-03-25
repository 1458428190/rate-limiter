package org.gucha.ratelimiter.core.module;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:14
 */
@Data
@AllArgsConstructor
public class VerifyImageDTO {

    private String verifyId;

    private String verifyType;

    private byte[] verifyImg;

    /**
     * 验证码 结果
     */
    private String result;
}
