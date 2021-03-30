package org.gucha.ratelimiter.core.framework.env;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.gucha.ratelimiter.core.framework.redis.DefaultJedisPoolConfig;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午4:07
 */
@Data
public class RedisConfig {
    public static final int DEFAULT_TIMEOUT = 10;

    public static final int DEFAULT_PORT = 6379;

    private String address;

    private int timeout = DEFAULT_TIMEOUT;

    private GenericObjectPoolConfig poolConfig = new DefaultJedisPoolConfig();

    public void buildFromProperties(PropertySource propertySource) {
        if (propertySource == null) {
            return;
        }
        String addr = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_REDIS_ADDRESS);
        if (StringUtils.isNotBlank(addr)) {
            this.address = addr;
        }

        Integer timeout = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_TIMEOUT);
        if (timeout != null) {
            this.timeout = timeout;
        }

        Integer maxTotal =
                propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_TOTAL);
        if (maxTotal != null) {
            this.poolConfig.setMaxTotal(maxTotal);
        }

        Integer maxIdle = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_IDLE);
        if (maxIdle != null) {
            this.poolConfig.setMaxIdle(maxIdle);
        }

        Integer minIdle = propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MIN_IDLE);
        if (minIdle != null) {
            this.poolConfig.setMinIdle(minIdle);
        }

        Integer maxWaitMillis =
                propertySource.getPropertyIntValue(PropertyConstants.PROPERTY_REDIS_MAX_WAIT_MILLIS);
        if (maxWaitMillis != null) {
            this.poolConfig.setMaxWaitMillis(maxWaitMillis);
        }

        Boolean testOnBorrow =
                propertySource.getPropertyBooleanValue(PropertyConstants.PROPERTY_REDIS_TEST_ON_BORROW);
        if (testOnBorrow != null) {
            this.poolConfig.setTestOnBorrow(testOnBorrow);
        }
    }
}
