package org.gucha.ratelimiter.core.framework.env;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Description:
 * @Author : laichengfeng
 * @Date : 2021/03/26 下午12:16
 */
@NoArgsConstructor
@Getter
public class PropertySource {
    private final Map<String, Object> properties = new LinkedHashMap<>();

    public PropertySource(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    public void addProperties(Map<String, Object> properties) {
        this.properties.putAll(properties);
    }

    /**
     * 聚合环境配置源. 这里可以存在多个环境配置
     *
     * @param propertySource
     */
    public void combinePropertySource(PropertySource propertySource) {
        properties.putAll(Optional.ofNullable(propertySource).map(PropertySource::getProperties).orElse(Maps.newLinkedHashMap()));
    }

    public Object getPropertyValue(String name) {
        return properties.get(name);
    }

    public String[] getPropertyNames() {
        return Optional.ofNullable(properties.keySet())
                .map(keySet -> keySet.toArray(new String[keySet.size()])).orElse(null);
    }

    public String getPropertyStringValue(String name) {
        return Optional.ofNullable(getPropertyValue(name)).map(String::valueOf).orElse(null);
    }

    public Integer getPropertyIntValue(String name) {
        return Optional.ofNullable(getPropertyStringValue(name)).map(Integer::valueOf).orElse(null);
    }

    public Long getPropertyLongValue(String name) {
        return Optional.ofNullable(getPropertyStringValue(name)).map(Long::valueOf).orElse(null);
    }

    public Boolean getPropertyBooleanValue(String name) {
        return Optional.ofNullable(getPropertyStringValue(name)).map(Boolean::valueOf).orElse(null);
    }
}
