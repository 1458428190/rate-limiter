package org.gucha.ratelimiter.core.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.common.exception.ConfigurationException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午7:15
 */
public class UrlUtils {

    public static List<String> tokenizeUrlPath(String urlPath) {
        if (StringUtils.isBlank(urlPath)) {
            return Collections.EMPTY_LIST;
        }

        if (!urlPath.startsWith("/")) {
            throw new ConfigurationException("urlParser tokenize error, invalid urls: " + urlPath);
        }
        String[] dirs = urlPath.split("/");
        List<String> dirList = new ArrayList<>();
        for (int i = 0; i < dirs.length; i++) {
            if (StringUtils.containsAny(dirs[i], ".", "?", "*") && !(dirs[i].startsWith("{")
                    && dirs[i].endsWith("}"))) {
                throw new ConfigurationException("urlParser tokenize error, invalid urls: " + urlPath);
            }
            if (!StringUtils.isEmpty(dirs[i])) {
                dirList.add(dirs[i]);
            }
        }
        return dirList;
    }
}
