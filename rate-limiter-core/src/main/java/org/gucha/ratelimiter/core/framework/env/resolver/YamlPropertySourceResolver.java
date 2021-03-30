package org.gucha.ratelimiter.core.framework.env.resolver;

import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.env.PropertyConstants;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午2:40
 */
public class YamlPropertySourceResolver extends AbstractPropertySourceResolver {

    private static final String[] SUPPORT_FILE_EXTENSIONS = {"yaml", "yml"};

    @Override
    public String[] getSupportedFileExtensions() {
        return SUPPORT_FILE_EXTENSIONS;
    }

    @Override
    public Map<String, Object> resolve(InputStream in) throws ConfigurationException {
        Yaml yaml = new Yaml();
        Map<String, Object> configMap = yaml.loadAs(in, Map.class);
        Map<String, Object> propertiesMap = new HashMap<>();
        configMap.entrySet().stream().filter(env -> env.getKey().startsWith(PropertyConstants.PROPERTY_KEY_PREFIX))
                .forEach(env -> propertiesMap.put(env.getKey(), env.getValue()));
        return propertiesMap;
    }
}
