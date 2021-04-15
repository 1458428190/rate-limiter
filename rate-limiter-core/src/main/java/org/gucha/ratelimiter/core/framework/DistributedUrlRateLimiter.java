package org.gucha.ratelimiter.core.framework;

import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.framework.algorithm.DistributedFixedTimeWindowRateLimiter;
import org.gucha.ratelimiter.core.framework.algorithm.RateLimiter;
import org.gucha.ratelimiter.core.framework.env.RedisConfig;
import org.gucha.ratelimiter.core.framework.env.ZookeeperConfig;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptor;
import org.gucha.ratelimiter.core.framework.interceptor.RateLimiterInterceptorChain;
import org.gucha.ratelimiter.core.framework.redis.DefaultJedisTaskExecutor;
import org.gucha.ratelimiter.core.framework.redis.JedisTaskExecutor;
import org.gucha.ratelimiter.core.framework.rule.RateLimitRule;
import org.gucha.ratelimiter.core.framework.rule.parser.JsonRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.RuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.parser.YamlRuleConfigParser;
import org.gucha.ratelimiter.core.framework.rule.source.FileRuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.RuleConfigSource;
import org.gucha.ratelimiter.core.framework.rule.source.ZookeeperRuleConfigSource;

import java.util.List;

import static org.gucha.ratelimiter.core.framework.context.RateLimiterBeansFactory.BEANS_CONTEXT;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/30 下午5:24
 */
public class DistributedUrlRateLimiter extends AbstractUrlRateLimiter {

    private JedisTaskExecutor jedisTaskExecutor;

    public DistributedUrlRateLimiter() {
        this(null, (RateLimitRule) null);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor) {
        this(jedisTaskExecutor, (RateLimitRule) null);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RuleConfigSource source) {
        super(source);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RateLimitRule rule) {
        super(rule);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    public DistributedUrlRateLimiter(JedisTaskExecutor jedisTaskExecutor, RateLimitRule rule,
                                     RateLimiterInterceptorChain chain) {
        super(rule, chain);
        this.jedisTaskExecutor = BEANS_CONTEXT.obtainJedisTaskExecutor(jedisTaskExecutor);
    }

    @Override
    protected RateLimiter createRateLimitAlgorithm(String limitKey, int limit) {
        return new DistributedFixedTimeWindowRateLimiter(limitKey, limit, jedisTaskExecutor);
    }

    public static DistributedUrlRateLimiterBuilder builder = new DistributedUrlRateLimiterBuilder();

    public static class DistributedUrlRateLimiterBuilder {
        /* redis configuratoin */
        private RedisConfig redisConfig;

        /* zookeeper configuration */
        private ZookeeperConfig zookeeperConfig;

        /* interceptors */
        private List<RateLimiterInterceptor> interceptors;

        /* rule configuration parser: yaml or json */
        private String ruleParserType = "yaml";

        /* source type: file or zookeeper */
        private String ruleSourceType = "file";

        private String ruleConfigFile;

        public DistributedUrlRateLimiterBuilder() {
        }

        public DistributedUrlRateLimiterBuilder setRedisConfig(RedisConfig redisConfig) {
            this.redisConfig = redisConfig;
            return this;
        }

        public DistributedUrlRateLimiterBuilder setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
            this.zookeeperConfig = zookeeperConfig;
            return this;
        }

        public DistributedUrlRateLimiterBuilder setInterceptors(
                List<RateLimiterInterceptor> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        public DistributedUrlRateLimiterBuilder setRuleParserType(String ruleParserType) {
            if (StringUtils.isNotBlank(ruleParserType)) {
                this.ruleParserType = ruleParserType;
            }
            return this;
        }

        public DistributedUrlRateLimiterBuilder setRuleSourceType(String ruleSourceType) {
            if (StringUtils.isNotBlank(ruleSourceType)) {
                this.ruleSourceType = ruleSourceType;
            }
            return this;
        }

        public DistributedUrlRateLimiterBuilder setRuleConfigFile(String ruleConfigFile) {
            this.ruleConfigFile = ruleConfigFile;
            return this;
        }

        public DistributedUrlRateLimiter build() {
            // 初始化相关对象
            JedisTaskExecutor executor = new DefaultJedisTaskExecutor(redisConfig.getAddress(),
                    redisConfig.getTimeout(), redisConfig.getPoolConfig());

            RuleConfigParser parser;
            if (this.ruleParserType.equals("yaml")) {
                parser = new YamlRuleConfigParser();
            } else if (this.ruleParserType.equals("json")) {
                parser = new JsonRuleConfigParser();
            } else {
                throw new RuntimeException("Do not support the rule paser type: " + this.ruleParserType);
            }

            RuleConfigSource source;
            if (this.ruleSourceType.equals("file")) {
                source = new FileRuleConfigSource(ruleConfigFile);
            } else if (this.ruleSourceType.equals("zookeeper")) {
                if (zookeeperConfig != null && StringUtils.isNoneBlank(zookeeperConfig.getAddress())
                        && StringUtils.isNoneBlank(zookeeperConfig.getPath())) {
                    source = new ZookeeperRuleConfigSource(zookeeperConfig.getAddress(),
                            zookeeperConfig.getPath(), parser);
                } else {
                    throw new RuntimeException("some zookeeper configuration is empty.");
                }
            } else {
                throw new RuntimeException("Do not support the rule source type: " + this.ruleSourceType);
            }

            DistributedUrlRateLimiter ratelimiter = new DistributedUrlRateLimiter(executor, source);
            if (this.interceptors != null && !this.interceptors.isEmpty()) {
                ratelimiter.addInterceptors(interceptors);
            }
            return ratelimiter;
        }
    }
}
