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

    private String verifyImgStr;

    /**
     * 验证码真实 码/答案
     */
    private String result;
}
