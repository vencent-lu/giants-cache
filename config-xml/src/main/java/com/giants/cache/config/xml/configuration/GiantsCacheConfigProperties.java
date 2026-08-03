package com.giants.cache.config.xml.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
   * GiantsCacheConfigProperties TODO
   * date time: 2026/07/31 14:32
   * Copyright 2026 github.com/vencent-lu Inc. All rights reserved.
   *
   * @author vencent-lu
   * @since 1.2
   */
@ConfigurationProperties(prefix = "giants.cache")
public class GiantsCacheConfigProperties {
    private String cacheConfigKey;
    private String cacheConfigXmlFilePath;

    public String getCacheConfigKey() {
        return cacheConfigKey;
    }

    public void setCacheConfigKey(String cacheConfigKey) {
        this.cacheConfigKey = cacheConfigKey;
    }

    public String getCacheConfigXmlFilePath() {
        return cacheConfigXmlFilePath;
    }

    public void setCacheConfigXmlFilePath(String cacheConfigXmlFilePath) {
        this.cacheConfigXmlFilePath = cacheConfigXmlFilePath;
    }
}
