package org.gucha.ratelimiter.core.framework.env;

import com.google.common.annotations.VisibleForTesting;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.framework.env.loader.*;
import org.gucha.ratelimiter.core.framework.extension.OrderComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午3:26
 */
@Data
public class RateLimiterConfig {
    private List<PropertySourceLoader> sourceLoaders;

    private RedisConfig redisConfig = new RedisConfig();
    private ZookeeperConfig zookeeperConfig = new ZookeeperConfig();

    private String ruleConfigParseType = "yaml";

    private String ruleConfigSourceType = "file";

    private String type = "memory";

    private AtomicBoolean isInitialized = new AtomicBoolean(false);

    private static final class RateLimiterConfigHolder {
        public static final RateLimiterConfig INSTANCE = new RateLimiterConfig();
    }

    public static final RateLimiterConfig instance() {
        return RateLimiterConfigHolder.INSTANCE;
    }

    private RateLimiterConfig() {
        sourceLoaders = new ArrayList<>();
        sourceLoaders.add(new ClassPathPropertySourceLoader());
        sourceLoaders.add(new FileSystemPropertySourceLoader());
        sourceLoaders.add(new SystemPropertySourceLoader());
        sourceLoaders.add(new JvmPropertySourceLoader());
    }

    @VisibleForTesting
    protected RateLimiterConfig(List<PropertySourceLoader> propertySourceLoaders) {
        this.sourceLoaders = propertySourceLoaders;
    }

    public void load() {
        // 已经加载
        if (!isInitialized.compareAndSet(false, true)) {
            return;
        }
        PropertySource source = new PropertySource();
        Collections.sort(sourceLoaders, OrderComparator.INSTANCE);
        for (int i = sourceLoaders.size() - 1; i >= 0; --i) {
            PropertySourceLoader loader = sourceLoaders.get(i);
            if (loader != null) {
                source.combinePropertySource(loader.load());
            }
        }
        // map 映射成配置
        mapPropertiesToConfigs(source);
    }

    private void mapPropertiesToConfigs(PropertySource propertySource) {
        String parseType = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_RULE_CONFIG_PARSER);
        if (StringUtils.isNotBlank(parseType)) {
            this.ruleConfigParseType = parseType;
        }
        String source = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_RULE_CONFIG_SOURCE);
        if (StringUtils.isNotBlank(source)) {
            this.ruleConfigSourceType = source;
        }
        String type = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_TYPE);
        if (StringUtils.isNotBlank(type)) {
            this.type = type;
        }
        redisConfig.buildFromProperties(propertySource);
        zookeeperConfig.buildFromProperties(propertySource);
    }
}
