package org.gucha.ratelimiter.core.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午10:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyImageVO {
    private String verifyId;

    private String verifyType;

    private String verifyImgStr;
}
