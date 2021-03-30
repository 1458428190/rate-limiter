package org.gucha.ratelimiter.core.framework.env.io;

/**
 * @Description: the interface represent a resource loader to load resource from specified location.
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午12:11
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";
    String FILE_URL_PREFIX = "file:";

    Resource getResource(String location);
}
