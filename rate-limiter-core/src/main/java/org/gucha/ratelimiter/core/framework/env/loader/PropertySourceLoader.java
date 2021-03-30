package org.gucha.ratelimiter.core.framework.env.loader;

import org.gucha.ratelimiter.core.framework.env.PropertySource;

/**
 * @Description: the interface represents the environment configuration loaders.
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午12:14
 */
public interface PropertySourceLoader {
    /**
     * load property source
     * @return
     */
    PropertySource load();
}
