package org.gucha.ratelimiter.core.framework.env.loader;

import org.gucha.ratelimiter.core.framework.env.PropertyConstants;
import org.gucha.ratelimiter.core.framework.env.PropertySource;
import org.gucha.ratelimiter.core.framework.extension.Order;

import java.util.*;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午12:14
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
public class JvmPropertySourceLoader implements PropertySourceLoader {

    @Override
    public PropertySource load() {
        // 从jvm中取配置
        Properties properties = System.getProperties();
        Map<String, Object> rateLimiterProperties = new HashMap<>();
        Set<String> names = properties.stringPropertyNames();
        names.stream().filter(name -> name.startsWith(PropertyConstants.PROPERTY_KEY_PREFIX))
                .forEach(name -> rateLimiterProperties.put(name, properties.get(name)));
        PropertySource source = new PropertySource();
        source.addProperties(rateLimiterProperties);
        return source;
    }
}
