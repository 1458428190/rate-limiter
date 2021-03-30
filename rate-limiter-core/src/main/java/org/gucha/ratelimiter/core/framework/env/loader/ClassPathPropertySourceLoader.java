package org.gucha.ratelimiter.core.framework.env.loader;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午3:18
 */
public class ClassPathPropertySourceLoader extends AbstractFilePropertySourceLoader{

    private static final String[] DEFAULT_CONFIG_FILES = new String[] {
            "classpath:ratelimiter-env.yaml",
            "classpath:ratelimiter-env.yml",
            "classpath:ratelimiter-env.properties"
    };

    @Override
    public String[] getAllMatchedConfigFiles() {
        return DEFAULT_CONFIG_FILES;
    }
}
