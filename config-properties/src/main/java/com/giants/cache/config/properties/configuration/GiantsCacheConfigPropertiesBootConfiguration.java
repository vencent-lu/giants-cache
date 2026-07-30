package com.giants.cache.config.properties.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * GiantsCacheConfigPropertiesBootConfiguration TODO
 * date time: 2024/12/17 10:23
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
@Configuration
@EnableConfigurationProperties({GiantsCacheConfigProperties.class})
@Import(GiantsCacheConfigPropertiesBootSpringBeansConfiguration.class)
public class GiantsCacheConfigPropertiesBootConfiguration {
}
