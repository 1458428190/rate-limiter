package org.gucha.ratelimiter.core.framework.env.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: the interface represent a resource;
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午12:01
 */
public interface Resource {
    /**
     * get the resource absolute path
     * @return
     */
    String getPath();

    /**
     * get the resource extension
     * @return
     */
    String getExtension();

    /**
     * get a description for the resource, to be used for error output when working with the resource.
     * @return
     */
    String getDescription();

    /**
     * check if the resource exists or not
     * @return
     */
    boolean exists();

    /**
     * open an {@link InputStream} for the resource
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;

}
