package org.gucha.ratelimiter.core.framework.utils;

import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.common.exception.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;
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

    public static String getUrlPath(String url) throws InvalidUrlException {
        if(StringUtils.isBlank(url)) {
            return null;
        }
        URI urlObject;
        try {
            urlObject = new URI(url);
        } catch (URISyntaxException e) {
            throw new InvalidUrlException("Get url path error: " + url, e);
        }
        String path = urlObject.getPath();
        if(StringUtil.isEmpty(path)) {
            return "/";
        }
        return path;
    }
}
