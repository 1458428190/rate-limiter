package org.gucha.ratelimiter.core.framework.rule;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.gucha.ratelimiter.core.framework.extension.Order;
import org.gucha.ratelimiter.core.framework.rule.source.UniformRuleConfigMapping;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午6:00
 */
@Order(Order.HIGHEST_PRECEDENCE + 10)
@NoArgsConstructor
public class UrlRateLimitRule implements RateLimitRule {

    private volatile ConcurrentHashMap<String, AppUrlRateLimitRule> limitRules = new ConcurrentHashMap<>();

    @Override
    public void addRule(UniformRuleConfigMapping uniformRuleConfigMapping) {
        if (uniformRuleConfigMapping == null) {
            return;
        }
        uniformRuleConfigMapping.getConfigs().forEach(uniformRuleConfig ->
                addLimits(uniformRuleConfig.getAppId(), uniformRuleConfig.getLimits()));
    }

    @Override
    public ApiLimit getLimit(String appId, String api) {
        if (StringUtils.isAnyEmpty(appId, api)) {
            return null;
        }
        AppUrlRateLimitRule appUrlRateLimitRule = limitRules.get(appId);
        if (appUrlRateLimitRule == null) {
            return null;
        }
        return appUrlRateLimitRule.getLimitInfo(api);
    }

    @Override
    public void addLimit(String appId, ApiLimit apiLimit) {
        if (StringUtils.isEmpty(appId) || apiLimit == null) {
            return;
        }
        AppUrlRateLimitRule newTrie = new AppUrlRateLimitRule();
        AppUrlRateLimitRule trie = limitRules.putIfAbsent(appId, newTrie);
        if (trie == null) {
            trie = newTrie;
        }
        trie.addLimitInfo(apiLimit);
    }

    @Override
    public void addLimits(String appId, List<ApiLimit> limits) {
        AppUrlRateLimitRule newTrie = new AppUrlRateLimitRule();
        AppUrlRateLimitRule trie = limitRules.putIfAbsent(appId, newTrie);
        if (trie == null) {
            trie = newTrie;
        }
        for (ApiLimit apiLimit : limits) {
            trie.addLimitInfo(apiLimit);
        }
    }

    @Override
    public void rebuildRule(UniformRuleConfigMapping uniformRuleConfigMapping) {
        ConcurrentHashMap<String, AppUrlRateLimitRule> newLimitRules = new ConcurrentHashMap<>();
        uniformRuleConfigMapping.getConfigs().forEach(uniformRuleConfig -> {
            AppUrlRateLimitRule appUrlRateLimitRule = new AppUrlRateLimitRule();
            newLimitRules.put(uniformRuleConfig.getAppId(), appUrlRateLimitRule);
            uniformRuleConfig.getLimits().forEach(apiLimit -> appUrlRateLimitRule.addLimitInfo(apiLimit));
        });
        // 替换
        this.limitRules = newLimitRules;
    }

}
