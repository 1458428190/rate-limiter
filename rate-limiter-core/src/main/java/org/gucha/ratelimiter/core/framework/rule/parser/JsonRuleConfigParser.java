package org.gucha.ratelimiter.core.framework.rule.parser;

import com.alibaba.fastjson.JSON;
import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.rule.source.UniformRuleConfigMapping;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午3:59
 */
@Order(Order.HIGHEST_PRECEDENCE + 20)
public class JsonRuleConfigParser implements RuleConfigParser {

    @Override
    public UniformRuleConfigMapping parse(String configurationText) {
        return JSON.parseObject(configurationText, UniformRuleConfigMapping.class);
    }

    @Override
    public UniformRuleConfigMapping parse(InputStream in) {
        try {
            return JSON.parseObject(in, UniformRuleConfigMapping.class);
        } catch (IOException e) {
            throw new ConfigurationException("parse json failed.", e);
        }
    }
}
