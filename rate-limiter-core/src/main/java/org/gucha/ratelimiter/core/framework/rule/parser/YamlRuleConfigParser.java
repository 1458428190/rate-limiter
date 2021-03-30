package org.gucha.ratelimiter.core.framework.rule.parser;

import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.rule.source.UniformRuleConfigMapping;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:00
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
public class YamlRuleConfigParser implements RuleConfigParser {

    @Override
    public UniformRuleConfigMapping parse(String configurationText) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(configurationText, UniformRuleConfigMapping.class);
    }

    @Override
    public UniformRuleConfigMapping parse(InputStream in) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(in, UniformRuleConfigMapping.class);
    }
}
