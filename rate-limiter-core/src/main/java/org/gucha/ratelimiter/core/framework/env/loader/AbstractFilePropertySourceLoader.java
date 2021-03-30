package org.gucha.ratelimiter.core.framework.env.loader;

import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.env.PropertySource;
import org.gucha.ratelimiter.core.framework.env.io.DefaultResourceLoader;
import org.gucha.ratelimiter.core.framework.env.io.Resource;
import org.gucha.ratelimiter.core.framework.env.io.ResourceLoader;
import org.gucha.ratelimiter.core.framework.env.resolver.PropertiesPropertySourceResolver;
import org.gucha.ratelimiter.core.framework.env.resolver.PropertySourceResolver;
import org.gucha.ratelimiter.core.framework.env.resolver.YamlPropertySourceResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午2:13
 */
public abstract class AbstractFilePropertySourceLoader implements PropertySourceLoader {
    private ResourceLoader resourceLoader;
    private List<PropertySourceResolver> resolvers;

    public AbstractFilePropertySourceLoader() {
        this(null);
    }

    public AbstractFilePropertySourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader == null ? new DefaultResourceLoader() : resourceLoader;
        resolvers = new ArrayList<>();
        resolvers.add(new YamlPropertySourceResolver());
        resolvers.add(new PropertiesPropertySourceResolver());
    }

    @Override
    public PropertySource load() {
        Map<String, Object> propertyMap = new HashMap<>();
        // 支持多个文件, 会互相覆盖
        for (String file : getAllMatchedConfigFiles()) {
            // 根据文件名获取resource
            Resource resource = resourceLoader.getResource(file);
            if (!resource.exists()) {
                continue;
            }
            // 根据后缀选用对应的格式, 如yaml, properties.
            for (PropertySourceResolver resolver : resolvers) {
                if (resolver.canResolvedExtension(resource.getExtension())) {
                    try {
                        propertyMap.putAll(resolver.resolve(resource.getInputStream()));
                    } catch (IOException e) {
                        throw new ConfigurationException("load environment configuration failed.", e);
                    }
                }
            }
        }
        PropertySource source = new PropertySource();
        source.addProperties(propertyMap);
        return source;
    }

    public abstract String[] getAllMatchedConfigFiles();

}
