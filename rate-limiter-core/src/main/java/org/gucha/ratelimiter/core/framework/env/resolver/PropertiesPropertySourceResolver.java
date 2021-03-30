package org.gucha.ratelimiter.core.framework.env.resolver;

import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.env.PropertyConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Description: propreties格式
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午2:25
 */
public class PropertiesPropertySourceResolver extends AbstractPropertySourceResolver {

    private final static String[] SUPPORTED_FILE_EXTENSIONS = {"properties"};

    @Override
    public String[] getSupportedFileExtensions() {
        return SUPPORTED_FILE_EXTENSIONS;
    }

    @Override
    public Map<String, Object> resolve(InputStream in) throws ConfigurationException {
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            throw new ConfigurationException("parse properties configuration failed.", e);
        }
        Map<String, Object> propertiesMap = new HashMap<>();
        properties.stringPropertyNames().stream().filter(name->name.startsWith(PropertyConstants.PROPERTY_KEY_PREFIX))
                .forEach(name->propertiesMap.put(name, properties.get(name)));
        return propertiesMap;
    }
}
