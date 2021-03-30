package org.gucha.ratelimiter.core.framework.env.loader;

import org.gucha.ratelimiter.core.framework.env.PropertyConstants;
import org.gucha.ratelimiter.core.framework.env.PropertySource;
import org.gucha.ratelimiter.core.framework.extension.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 上午10:15
 */
@Order(Order.HIGHEST_PRECEDENCE + 20)
public class SystemPropertySourceLoader implements PropertySourceLoader {

    @Override
    public PropertySource load() {
        Map<String, Object> ratelimiterProperties = new HashMap<>();
        Map<String, String> envs = getEnv();
        envs.entrySet().stream().filter(env -> env.getKey().startsWith(PropertyConstants.PROPERTY_KEY_PREFIX))
                .forEach(env -> ratelimiterProperties.put(env.getKey(), env.getValue()));
        PropertySource source = new PropertySource();
        source.addProperties(ratelimiterProperties);
        return source;
    }

    protected Map<String, String> getEnv() {
        return System.getenv();
    }
}
