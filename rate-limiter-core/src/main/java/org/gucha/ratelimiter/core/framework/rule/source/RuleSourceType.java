package org.gucha.ratelimiter.core.framework.rule.source;

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
@Getter
@AllArgsConstructor
public enum RuleSourceType {
    /**
     * file
     */
    FILE("file"),

    /**
     * zookeeper
     */
    ZOOKEEPER("zookeeper"),

    /**
     * TODO db
     */
    ;

    private String source;

    private static final Map<String, RuleSourceType> map = new HashMap<>();

    static {
        for (RuleSourceType ruleSourceType : values()) {
            map.put(ruleSourceType.getSource(), ruleSourceType);
        }
    }

    public static RuleSourceType of(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return map.get(source.toLowerCase());
    }

}
