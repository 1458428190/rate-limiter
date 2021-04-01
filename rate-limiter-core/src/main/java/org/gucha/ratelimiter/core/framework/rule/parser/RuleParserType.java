package org.gucha.ratelimiter.core.framework.rule.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/04/01 下午4:04
 */
@AllArgsConstructor
@Getter
public enum RuleParserType {

    /**
     * json
     */
    JSON("json"),

    /**
     * yaml
     */
    YAML("yaml");

    private String alia;

    private static final Map<String, RuleParserType> map = new HashMap<>();

    static {
        for (RuleParserType ruleParserType : values()) {
            map.put(ruleParserType.getAlia(), ruleParserType);
        }
        // 兼容yml
        map.put("yml", RuleParserType.YAML);
    }

    public static RuleParserType of(String alias) {
        if (StringUtils.isBlank(alias)) {
            return null;
        }
        return map.get(alias.toLowerCase());
    }

}
