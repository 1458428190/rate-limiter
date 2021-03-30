package org.gucha.ratelimiter.core.framework.rule.source;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:46
 */
public interface RuleConfigSource {

    UniformRuleConfigMapping load();
}
