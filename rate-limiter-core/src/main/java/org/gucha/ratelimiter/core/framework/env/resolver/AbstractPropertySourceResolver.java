package org.gucha.ratelimiter.core.framework.env.resolver;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午2:19
 */
public abstract class AbstractPropertySourceResolver implements PropertySourceResolver {

    @Override
    public boolean canResolvedExtension(String fileExtension) {
        if (StringUtils.isEmpty(fileExtension)) {
            return false;
        }
        return Arrays.stream(getSupportedFileExtensions()).anyMatch(fileExtension::equalsIgnoreCase);
    }
}
