package org.gucha.ratelimiter.core.framework.rule;

import org.gucha.ratelimiter.core.framework.rule.source.UniformRuleConfigMapping;

import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:48
 */
public interface RateLimitRule {

    ApiLimit getLimit(String appId, String api);

    void addLimit(String appId, ApiLimit apiLimit);

    void addLimits(String appId, List<ApiLimit> limits);

    void rebuildRule(UniformRuleConfigMapping uniformRuleConfigMapping);

    void addRule(UniformRuleConfigMapping uniformRuleConfigMapping);
}
