package org.gucha.ratelimiter.core.framework.env;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:10
 */
public interface PropertyConstants {

    /**
     * 配置前缀
     */
    String PROPERTY_KEY_PREFIX = "ratelimiter";

    /**
     * 规则配置解析
     */
    String PROPERTY_RULE_CONFIG_PARSER = PROPERTY_KEY_PREFIX + ".rule.config.parser";

    /**
     * 规则配置源
     */
    String PROPERTY_RULE_CONFIG_SOURCE = PROPERTY_KEY_PREFIX + ".rule.config.source";

    /**
     * 限流类型. 单机内存|分布式
     */
    String PROPERTY_TYPE = ".type";

    /**
     * TODO 优化, 支持SpringBoot的自动注入
     */
    String PROPERTY_REDIS_ADDRESS = PROPERTY_KEY_PREFIX + ".redis.address";

    String PROPERTY_REDIS_MAX_TOTAL = PROPERTY_KEY_PREFIX + ".redis.maxTotal";

    String PROPERTY_REDIS_MAX_IDLE = PROPERTY_KEY_PREFIX + ".redis.maxIdle";

    String PROPERTY_REDIS_MIN_IDLE = PROPERTY_KEY_PREFIX + ".redis.minIdle";

    String PROPERTY_REDIS_MAX_WAIT_MILLIS = PROPERTY_KEY_PREFIX + ".redis.maxWaitMillis";

    String PROPERTY_REDIS_TEST_ON_BORROW = PROPERTY_KEY_PREFIX + ".redis.testOnBorrow";

    String PROPERTY_REDIS_TIMEOUT = PROPERTY_KEY_PREFIX + ".redis.timeout";

    String PROPERTY_ZOOKEEPER_ADDRESS = PROPERTY_KEY_PREFIX + ".zookeeper.address";

    String PROPERTY_ZOOKEEPER_RULE_PATH = PROPERTY_KEY_PREFIX + ".zookeeper.rule.path";

}
