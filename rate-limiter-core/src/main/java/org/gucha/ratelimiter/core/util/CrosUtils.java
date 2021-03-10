package org.gucha.ratelimiter.core.util;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 跨域工具
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:04
 */
public class CrosUtils {

    public static void setCrosHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "18000L");
        response.setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
