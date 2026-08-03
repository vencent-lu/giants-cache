package com.giants.cache.config.properties.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * GiantsCacheConfigPropertiesBootConfiguration TODO
 * date time: 2024/12/17 10:23
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({GiantsCacheConfigProperties.class})
@Import(GiantsCacheConfigBootSpringBeansConfiguration.class)
public class GiantsCacheConfigBootConfiguration {
}
