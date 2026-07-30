package com.giants.cache.config;

/**
 * CacheConfigBuilder 缓存配置构建器接口定义
 * date time: 2024/12/16 15:45
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public interface CacheConfigBuilder {

    /**
     * 缓存配置的key
     * @return
     */
    String getCacheConfigKey();

    /**
     * 构建缓存配置
     * @return
     */
    CacheConfig build();
}
