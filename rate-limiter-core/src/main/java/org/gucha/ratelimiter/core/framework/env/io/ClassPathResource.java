package org.gucha.ratelimiter.core.framework.env.io;

import cn.hutool.core.util.ClassUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午5:59
 */
public class ClassPathResource implements Resource {

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        String pathToUsed = path;
        if (pathToUsed.startsWith("/")) {
            pathToUsed = pathToUsed.substring(1);
        }
        this.path = pathToUsed;
        this.classLoader = Optional.ofNullable(classLoader).orElse(ClassUtil.getClassLoader());
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getExtension() {
        int pos = path.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }
        String extension = path.substring(pos + 1);
        if (StringUtils.isBlank(extension)) {
            return null;
        }
        return extension;
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder("class path resource [");
        String pathToUse = path;
        if (pathToUse.startsWith("/")) {
            pathToUse = pathToUse.substring(1);
        }
        builder.append(pathToUse).append("]");
        return builder.toString();
    }

    @Override
    public boolean exists() {
        return resolveURL() != null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }
        return is;
    }

    private URL resolveURL() {
        return classLoader.getResource(path);
    }
}
