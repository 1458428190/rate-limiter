package org.gucha.ratelimiter.core.framework.env.loader;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午2:13
 */
public class FileSystemPropertySourceLoader extends AbstractFilePropertySourceLoader {

    private static final String[] DEFAULT_CONFIG_FILES = new String[] {
            "file:ratelimiter-env.yaml",
            "file:ratelimiter-env.yml",
            "file:ratelimiter-env.properties"
    };

    @Override
    public String[] getAllMatchedConfigFiles() {
        return DEFAULT_CONFIG_FILES;
    }
}
