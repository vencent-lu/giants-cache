package com.giants.cache.config.properties.configuration;

import com.giants.cache.config.properties.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.Map;

/**
 * GiantsCacheConfigProperties TODO
 * date time: 2024/12/17 17:36
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
@ConfigurationProperties(prefix = "giants.cache")
@RefreshScope
public class GiantsCacheConfigProperties {

    private String name;

    private Map<String, CacheConfig> configMap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, CacheConfig> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, CacheConfig> configMap) {
        this.configMap = configMap;
    }
}
