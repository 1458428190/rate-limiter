package org.gucha.ratelimiter.core.framework.rule.parser;

import org.gucha.ratelimiter.core.framework.rule.source.UniformRuleConfigMapping;

import java.io.InputStream;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:45
 */
public interface RuleConfigParser {
    UniformRuleConfigMapping parse(String configurationText);

    UniformRuleConfigMapping parse(InputStream in);
}
