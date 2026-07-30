package com.giants.cache.config.properties;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.CacheConfig;
import com.giants.cache.config.CacheConfigBuilder;
import com.giants.cache.config.properties.configuration.GiantsCacheConfigProperties;
import org.apache.commons.lang.StringUtils;

/**
 * CacheConfigPropertiesBuilder TODO
 * date time: 2024/12/18 14:09
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CacheConfigPropertiesBuilder implements CacheConfigBuilder {

    private final String cacheConfigKey;
    private final GiantsCacheConfigProperties giantsCacheConfigProperties;

    public CacheConfigPropertiesBuilder(GiantsCacheConfigProperties giantsCacheConfigProperties) {
        this.cacheConfigKey = CacheConstants.DEFAULT_CONFIG_KEY;
        this.giantsCacheConfigProperties = giantsCacheConfigProperties;
    }

    public CacheConfigPropertiesBuilder(String cacheConfigKey, GiantsCacheConfigProperties giantsCacheConfigProperties) {
        this.cacheConfigKey = cacheConfigKey;
        this.giantsCacheConfigProperties = giantsCacheConfigProperties;
    }

    @Override
    public String getCacheConfigKey() {
        return this.cacheConfigKey;
    }

    @Override
    public CacheConfig build() {
        if (StringUtils.isNotEmpty(this.cacheConfigKey) && this.giantsCacheConfigProperties != null) {
            if (this.giantsCacheConfigProperties.getConfigMap() == null) {
                return null;
            }
            return this.giantsCacheConfigProperties.getConfigMap().get(this.cacheConfigKey);
        }
        return null;
    }
}
