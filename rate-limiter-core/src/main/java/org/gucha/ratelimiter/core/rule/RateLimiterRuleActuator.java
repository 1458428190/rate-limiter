package org.gucha.ratelimiter.core.rule;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/10 上午11:00
 */
@Data
@AllArgsConstructor
public class RateLimiterRuleActuator {

    private List<RateLimiterRule> ruleList;

    /**
     * 是否允许通过请求
     *
     * @param request
     * @param response
     * @return
     */
    public boolean isAllowed(HttpServletRequest request, HttpServletResponse response) {
        for (RateLimiterRule rateLimiterRule : ruleList) {
            if (rateLimiterRule.execute(request, response)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 重置记录的规则
     * @param request
     * @param realRequestUrl
     */
    public void reset(HttpServletRequest request, String realRequestUrl) {
        ruleList.forEach(rule -> rule.reset(request, realRequestUrl));
    }
}
