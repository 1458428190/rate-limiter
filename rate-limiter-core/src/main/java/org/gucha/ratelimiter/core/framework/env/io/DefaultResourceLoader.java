package org.gucha.ratelimiter.core.framework.env.io;

import cn.hutool.core.util.ClassUtil;
import lombok.AllArgsConstructor;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 上午9:55
 */
@AllArgsConstructor
public class DefaultResourceLoader implements ResourceLoader{

    private ClassLoader classLoader;

    public DefaultResourceLoader() {
        this.classLoader = ClassUtil.getClassLoader();;
    }

    @Override
    public Resource getResource(String location) {
        if(location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), this.classLoader);
        } else if(location.startsWith(FILE_URL_PREFIX)) {
            return new FileSystemResource(location.substring(FILE_URL_PREFIX.length()));
        } else {
            // 默认使用fileSystemResource, 且不带前缀
            return new FileSystemResource(location);
        }
    }
}
