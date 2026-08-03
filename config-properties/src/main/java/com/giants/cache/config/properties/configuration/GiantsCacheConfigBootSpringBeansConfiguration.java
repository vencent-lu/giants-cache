package com.giants.cache.config.properties.configuration;

import com.giants.cache.config.CacheConfigBuilder;
import com.giants.cache.config.properties.CacheConfigPropertiesBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GiantsCacheConfigPropertiesBootSpringBeansConfiguration TODO
 * date time: 2024/12/18 15:04
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
@Configuration
public class GiantsCacheConfigBootSpringBeansConfiguration {

    @Bean
    public CacheConfigBuilder createCacheConfigBuilder(GiantsCacheConfigProperties giantsCacheConfigProperties) {
        return new CacheConfigPropertiesBuilder(giantsCacheConfigProperties);
    }

}
