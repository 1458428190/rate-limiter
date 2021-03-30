package org.gucha.ratelimiter.core.framework.env.resolver;

import org.gucha.ratelimiter.common.exception.ConfigurationException;

import java.io.InputStream;
import java.util.Map;

/**
 * @Description: the resolver is used to parse the configurations of different formats, such as yaml, properties
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午3:43
 */
public interface PropertySourceResolver {
    String[] getSupportedFileExtensions();

    boolean canResolvedExtension(String fileExtension);

    Map<String, Object> resolve(InputStream in) throws ConfigurationException;
}
