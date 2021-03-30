package org.gucha.ratelimiter.core.framework.rule.source;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gucha.ratelimiter.core.framework.rule.ApiLimit;

import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:48
 */
@Data
public class UniformRuleConfigMapping {
    private List<UniformRuleConfig> configs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UniformRuleConfig {
        private String appId;
        private List<ApiLimit> limits;
    }
}
