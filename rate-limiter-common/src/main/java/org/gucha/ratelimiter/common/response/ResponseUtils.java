package org.gucha.ratelimiter.common.response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: laichengfeng
 * @Description: 响应工具类
 * @Date: 2019/08/20 11:34
 */
public class ResponseUtils {

    /**
     * 设置跨域头
     *
     * @param response
     */
    public static void setCrosHeader(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "18000L");
        response.setHeader("Access-Control-Allow-Headers",
                "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN,token,username,client");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    /**
     * 返回json字符串, 支持JSONP
     *
     * @param request  request
     * @param response response
     * @param json     响应json串
     * @throws IOException
     */
    public static void response(HttpServletRequest request, HttpServletResponse response, String json)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        setNoCache(response);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.flush();
    }

    /**
     * 设置不缓存
     *
     * @param response
     */
    private static void setNoCache(HttpServletResponse response) {
        response.addHeader("pragma", "NO-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addDateHeader("Expires", 0L);
    }

    /**
     * 跳转301
     *
     * @param response response
     * @param toUrl    目标url
     */
    public static void to301(HttpServletResponse response, String toUrl) {
        response.setStatus(301);
        response.setHeader("Location", toUrl);
        response.setHeader("Connection", "close");
    }
}

