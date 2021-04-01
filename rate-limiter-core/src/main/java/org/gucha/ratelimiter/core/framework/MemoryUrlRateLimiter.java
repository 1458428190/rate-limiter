package org.gucha.ratelimiter.core.framework;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.framework.algorithm.FixedTimeWindowRateLimiter;
import org.gucha.ratelimiter.core.framework.algorithm.RateLimiter;
import org.gucha.ratelimiter.core.framework.env.ZookeeperConfig;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptor;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptorChain;
import org.gucha.ratelimiter.core.framework.rule.RateLimitRule;
import org.gucha.ratelimiter.core.framework.rule.parser.JsonRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.RuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.YamlRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.source.FileRuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.RuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.ZookeeperRuleConfigSource;

import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/30 下午5:03
 */
public class MemoryUrlRateLimiter extends AbstractUrlRateLimiter {

    public MemoryUrlRateLimiter() {
        this((RuleConfigSource) null);
    }

    public MemoryUrlRateLimiter(RuleConfigSource source) {
        super(source);
    }

    public MemoryUrlRateLimiter(RateLimitRule rule) {
        super(rule);
    }

    public MemoryUrlRateLimiter(RateLimitRule rule, RateLimiterInterceptorChain chain) {
        super(rule, chain);
    }

    @Override
    protected RateLimiter createRateLimitAlgorithm(String limitKey, int limit) {
        return new FixedTimeWindowRateLimiter(limit);
    }

    public static MemoryUrlRateLimiterBuilder builder = new MemoryUrlRateLimiterBuilder();

    public static class MemoryUrlRateLimiterBuilder {
        private ZookeeperConfig zookeeperConfig;
        private List<RateLimiterInterceptor> interceptors;
        private String ruleParserType = "yaml";
        private String ruleSourceType = "file";

        public MemoryUrlRateLimiterBuilder() {

        }

        public MemoryUrlRateLimiterBuilder setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
            this.zookeeperConfig = zookeeperConfig;
            return this;
        }

        public MemoryUrlRateLimiterBuilder setInterceptors(List<RateLimiterInterceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public MemoryUrlRateLimiterBuilder setRuleParserType(String ruleParserType) {
            if (StringUtils.isNotBlank(ruleParserType)) {
                this.ruleParserType = ruleParserType;
            }
            return this;
        }

        public MemoryUrlRateLimiterBuilder setRuleSourceType(String ruleSourceType) {
            if (StringUtils.isNotBlank(ruleSourceType)) {
                this.ruleSourceType = ruleSourceType;
            }
            return this;
        }

        /**
         * 构造内存版 限流器
         *
         * @return
         */
        public MemoryUrlRateLimiter build() {
            RuleConfigParser parser;
            if (StringUtils.equals(this.ruleParserType, "yaml")) {
                parser = new YamlRuleConfigParser();
            } else if (StringUtils.equals(this.ruleParserType, "json")) {
                parser = new JsonRuleConfigParser();
            } else {
                throw new RuntimeException("Do not support the rule parser type:" + this.ruleParserType);
            }

            RuleConfigSource configSource;
            if (StringUtils.equals(this.ruleSourceType, "file")) {
                configSource = new FileRuleConfigSource();
            } else if (StringUtils.equals(this.ruleSourceType, "zookeeper")) {
                if (zookeeperConfig != null && StringUtils.isNoneBlank(zookeeperConfig.getAddress())
                        && StringUtils.isNoneBlank(zookeeperConfig.getPath())) {
                    configSource = new ZookeeperRuleConfigSource(zookeeperConfig.getAddress(),
                            zookeeperConfig.getPath(), parser);
                } else {
                    throw new RuntimeException("some zookeeper configuration is empty.");
                }
            } else {
                throw new RuntimeException("Do not support the rule source type: " + this.ruleSourceType);
            }
            MemoryUrlRateLimiter rateLimiter = new MemoryUrlRateLimiter(configSource);
            if (CollectionUtils.isNotEmpty(interceptors)) {
                rateLimiter.addInterceptors(interceptors);
            }
            return rateLimiter;
        }
    }

}
