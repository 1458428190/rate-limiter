package org.gucha.ratelimiter.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * HTTP请求的IP信息提取工具
 *
 * @author DIAO
 */
public class IpUtils {

    /**
     * 获取用户IP，若用户经过代理，则IP包含代理地址：clientIp,proxyIp1,proxyIp2...
     *
     * @param httpRequest request
     * @return 请求ip
     */
    public static String getRequestIP(HttpServletRequest httpRequest) {
        if (null == httpRequest) {
            throw new IllegalArgumentException("request could not be null");
        }
        String ip = httpRequest.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = httpRequest.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 从包含N个代理服务器地址的IP字符串中找出真实用户的ip,verboseIp里每个IP间以英文逗号","隔开
     *
     * @param request request
     * @return
     */
    public static String[] getIpSet(HttpServletRequest request) {
        String verboseIps = getRequestIP(request);
        if (StringUtils.isBlank(verboseIps)) {
            return null;
        }

        String[] ips = verboseIps.split(",");
        return ips;
    }

    public static String getRealIp(HttpServletRequest request) {
        String[] ips = getIpSet(request);
        if (null != ips && ips.length > 0) {
            return ips[0];
        }
        return null;
    }

    /**
     * 从IP地址到2进制字符串的转换（32位）
     *
     * @param ip ip
     * @return
     */
    public static String getBinaryIP(String ip) {
        long lip = 0;
        String[] ipa = ip.split("\\.");
        if (4 != ipa.length) {
            return null;
        }
        try {
            lip = (long) ((Integer.parseInt(ipa[0]) << 24)
                    + (Integer.parseInt(ipa[1]) << 16)
                    + (Integer.parseInt(ipa[2]) << 8) + Integer
                    .parseInt(ipa[3]));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String rs = Long.toBinaryString(lip);
        if (rs.length() < 32) {
            int di = 32 - rs.length();
            for (int i = 0; i < di; i++) {
                rs = "0" + rs;
            }
        }
        return rs;
    }

    /**
     * 若ip为完整ip地址，则返回ip本身；若ip为不完整ip地址，如172.18.60，则填充为172.18.60.0
     *
     * @param ip
     * @return
     */
    public static String fillIp(String ip) {
        String rs = ip;
        String[] ips = ip.split("[.]");
        if (ips.length != 4) {
            int len = ips.length;
            for (int i = 0; i < 4 - len; i++) {
                rs += ".0";
            }
        }
        return rs;
    }

    private static final long BEGIN_TYPE_A = toLong("10.0.0.0");

    private static final long END_TYPE_A = toLong("10.255.255.255");

    private static final long BEGIN_TYPE_B = toLong("172.16.0.0");

    private static final long END_TYPE_B = toLong("172.31.255.255");

    private static final long BEGIN_TYPE_C = toLong("192.168.0.0");

    private static final long END_TYPE_C = toLong("192.168.255.255");

    private static final long LOOP_BACK = toLong("127.0.0.1");

    public static boolean isInnerIP(String ipAddress) {
        long ipInLong = toLong(ipAddress);
        return (BEGIN_TYPE_C <= ipInLong && ipInLong <= END_TYPE_C)
                || (BEGIN_TYPE_B <= ipInLong && ipInLong <= END_TYPE_B)
                || (BEGIN_TYPE_A <= ipInLong && ipInLong <= END_TYPE_A)
                || LOOP_BACK == ipInLong;
    }

    public static long toLong(String ip) {
        String[] ipa = ip.split("\\.");
        long res = 0;
        for (int i = 0, d = 24; i < ipa.length; i++, d -= 8) {
            res += Integer.parseInt(ipa[i]) << d;
        }
        return res;
    }
}
