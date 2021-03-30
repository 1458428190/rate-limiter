package org.gucha.ratelimiter.core.framework.env;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: TODO 使用SpringBoot的方式整合zookeeper
 * @Author : laichengfeng
 * @Date : 2021/03/29 下午3:20
 */
@Data
public class ZookeeperConfig {
    public static final String DEFAULT_PATH = "/org/gucha/ratelimiter";
    private String address;
    private String path = DEFAULT_PATH;

    public void buildFromProperties(PropertySource propertySource) {
        if (propertySource == null) {
            return;
        }
        this.address = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_ZOOKEEPER_ADDRESS);
        String path = propertySource.getPropertyStringValue(PropertyConstants.PROPERTY_ZOOKEEPER_RULE_PATH);
        if (StringUtils.isNotBlank(path)) {
            this.path = path;
        }
    }
}
