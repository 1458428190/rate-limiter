package org.gucha.ratelimiter.core.framework.rule.source;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.rule.parser.JsonRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.RuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.YamlRuleConfigParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO 支持自定义文件.
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午4:54
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class FileRuleConfigSource implements RuleConfigSource {

    public static final String API_LIMIT_CONFIG_NAME = "ratelimiter-rule";
    public static final String JSON_EXTENSION = "json";
    public static final String YML_EXTENSION = "yml";
    public static final String YAML_EXTENSION = "yaml";
    private String ruleConfigFile;

    private static final String[] SUPPORT_EXTENSIONS = new String[]{YAML_EXTENSION, YML_EXTENSION, JSON_EXTENSION};
    private static final Map<String, RuleConfigParser> PARSER_MAP = new HashMap<>();

    static {
        PARSER_MAP.put(YAML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(YML_EXTENSION, new YamlRuleConfigParser());
        PARSER_MAP.put(JSON_EXTENSION, new JsonRuleConfigParser());
    }

    public FileRuleConfigSource(String ruleConfigFile) {
        this.ruleConfigFile = ruleConfigFile;
    }

    /**
     *DistributedUrlRateLimiterBuilder
     * @return
     */
    @Override
    public UniformRuleConfigMapping load() {
        for (String extension : SUPPORT_EXTENSIONS) {
            InputStream in = null;
            try {
                in = this.getClass().getResourceAsStream("/" + API_LIMIT_CONFIG_NAME + "." + extension);
                if (in != null) {
                    RuleConfigParser ruleConfigParser = PARSER_MAP.get(extension);
                    return ruleConfigParser.parse(in);
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("close file error:{}", e);
                    }
                }
            }
        }
        // 指定规则配置文件
        if (StringUtils.isNotBlank(ruleConfigFile)) {
            InputStream in = null;
            try {
                in = this.getClass().getResourceAsStream("/" + ruleConfigFile);
                if (in != null) {
                    RuleConfigParser ruleConfigParser = PARSER_MAP.get(getExtension(ruleConfigFile));
                    return ruleConfigParser.parse(in);
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("close file error:{}", e);
                    }
                }
            }
        }
        return null;
    }

    private String getExtension(String path) {
        int pos = path.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }
        String extension = path.substring(pos + 1);
        if (StringUtils.isBlank(extension)) {
            return null;
        }
        return extension;
    }
}
