package com.giants.cache.config.xml.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * GiantsCacheConfigBootConfiguration TODO
 * date time: 2026/07/31 15:11
 * Copyright 2026 github.com/vencent-lu Inc. All rights reserved.
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
