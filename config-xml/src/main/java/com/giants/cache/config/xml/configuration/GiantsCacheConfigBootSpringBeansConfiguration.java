package com.giants.cache.config.xml.configuration;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.CacheConfigBuilder;
import com.giants.cache.config.xml.CacheConfigXmlBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GiantsCacheConfigBootSpringBeansConfiguration TODO
 * date time: 2026/07/31 15:03
 * Copyright 2026 github.com/vencent-lu Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
@Configuration
public class GiantsCacheConfigBootSpringBeansConfiguration {

    @Bean
    public CacheConfigBuilder createCacheConfigBuilder(GiantsCacheConfigProperties giantsCacheConfigProperties) {
        if (giantsCacheConfigProperties == null) {
            return new CacheConfigXmlBuilder();
        } else {
            String cacheConfigKey = StringUtils.isNotEmpty(giantsCacheConfigProperties.getCacheConfigKey()) ?
                    giantsCacheConfigProperties.getCacheConfigKey() : CacheConstants.DEFAULT_CONFIG_KEY;
            String cacheConfigXmlFilePath = StringUtils.isNotEmpty(giantsCacheConfigProperties.getCacheConfigXmlFilePath()) ?
                    giantsCacheConfigProperties.getCacheConfigXmlFilePath() : CacheConstants.DEFAULT_CONFIG_FILE_PATH;
            return new CacheConfigXmlBuilder(cacheConfigKey, cacheConfigXmlFilePath);
        }
    }
}
