package org.gucha.ratelimiter.core.framework.rule;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.common.exception.ConfigurationException;
import org.gucha.ratelimiter.core.framework.utils.UrlUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午5:59
 */
@Slf4j
public class AppUrlRateLimitRule {

    private Node root;

    public AppUrlRateLimitRule() {
        root = new Node("/");
    }

    public void addLimitInfo(ApiLimit apiLimit) {
        String urlPath = apiLimit.getApi();
        // 路径不合法
        if (!urlPath.startsWith("/")) {
            throw new ConfigurationException("invalid url,the api is invalid: " + urlPath);
        }
        if (urlPath.equals("/")) {
            root.setApiLimit(apiLimit);
            return;
        }

        List<String> pathDirs = UrlUtils.tokenizeUrlPath(apiLimit.getApi());
        if (CollectionUtils.isEmpty(pathDirs)) {
            log.warn("url [{}] is parsed to be empty pathDirs.", apiLimit.getApi());
            return;
        }

        Node p = root;
        for (String pathDir : pathDirs) {
            ConcurrentHashMap<String, Node> children = p.getEdges();
            String pathDirPattern = pathDir;
            boolean isPattern = false;
            if (isUrlTemplateVariable(pathDir)) {
                pathDirPattern = getPathDirPattern(pathDir);
                isPattern = true;
            }
            Node newNode = new Node(pathDirPattern, isPattern);
            Node existedNode = children.putIfAbsent(pathDirPattern, newNode);
            if (existedNode != null) {
                p = existedNode;
            } else {
                p = newNode;
            }
        }
        p.setApiLimit(apiLimit);
        log.info("url [{}] set limit info {}.", apiLimit.getApi(), JSON.toJSON(apiLimit));
    }

    public void addLimitInfos(Collection<ApiLimit> apiLimits) {
        for (ApiLimit apiLimit : apiLimits) {
            addLimitInfo(apiLimit);
        }
    }

    public ApiLimit getLimitInfo(String urlPath) {
        if (StringUtils.isBlank(urlPath)) {
            return null;
        }
        if (urlPath.equals("/")) {
            return root.getApiLimit();
        }

        List<String> pathDirs = UrlUtils.tokenizeUrlPath(urlPath);
        if (CollectionUtils.isEmpty(pathDirs)) {
            log.warn("url [{}] is parsed to be empty pathDirs.", urlPath);
            return null;
        }
        Node p = root;
        ApiLimit currentLimit = null;
        if (p.getApiLimit() != null) {
            currentLimit = p.getApiLimit();
        }
        for (String pathDir : pathDirs) {
            ConcurrentHashMap<String, Node> children = p.getEdges();
            Node matchedNode = children.get(pathDir);
            if (matchedNode == null) {
                // 走正则
                for (Map.Entry<String, Node> entry : children.entrySet()) {
                    Node n = entry.getValue();
                    if (n.isPattern()) {
                        boolean matched = Pattern.matches(n.getPathDir(), pathDir);
                        if (matched) {
                            matchedNode = n;
                        }
                    }
                }
            }
            if (matchedNode != null) {
                p = matchedNode;
                if (matchedNode.getApiLimit() != null) {
                    currentLimit = matchedNode.getApiLimit();
                }
            } else {
                break;
            }
        }
        log.info("url [{}] get limit info: {}.", urlPath, currentLimit);
        return currentLimit;
    }

    private boolean isUrlTemplateVariable(String pathDir) {
        return pathDir.startsWith("{") && pathDir.endsWith("}");
    }

    private String getPathDirPattern(String pathDir) {
        StringBuilder patternBuilder = new StringBuilder();
        int colonIdx = pathDir.indexOf(':');
        if (colonIdx == -1) {
            patternBuilder.append("(^[0-9]*$)");
        } else {
            String variablePattern = pathDir.substring(colonIdx + 1, pathDir.length() - 1);
            patternBuilder.append("(");
            patternBuilder.append(variablePattern);
            patternBuilder.append(")");
        }
        return patternBuilder.toString();
    }

    @NoArgsConstructor
    @Data
    public static final class Node {
        private String pathDir;
        private boolean isPattern;

        private ConcurrentHashMap<String, Node> edges = new ConcurrentHashMap<>();

        private ApiLimit apiLimit;

        public Node(String pathDir) {
            this(pathDir, false);
        }

        public Node(String pathDir, boolean isPattern) {
            this.pathDir = pathDir;
            this.isPattern = isPattern;
        }

    }
}
