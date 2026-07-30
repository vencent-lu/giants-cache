package com.giants.cache.config.properties.elements;

import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.ClearCache;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MethodCacheModel TODO
 * date time: 2024/12/17 14:16
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class MethodCacheModel extends CacheModel implements com.giants.cache.config.elements.MethodCacheModel {
    private static final long serialVersionUID = 8955965626590043170L;

    private List<com.giants.cache.config.properties.elements.CacheElement> cacheElements;
    private Map<String,com.giants.cache.config.properties.elements.CacheElement> cacheElementMap;
    private List<com.giants.cache.config.properties.elements.ClearCache> clearCaches;
    private Map<String,com.giants.cache.config.properties.elements.ClearCache> clearCacheMap;

    @Override
    public CacheElement createCacheElement(String name) {
        return new com.giants.cache.config.properties.elements.CacheElement(name, this.getDefaultTimeToLive());
    }

    @Override
    public CacheElement getCacheElement(String elementName) {
        if (this.cacheElementMap == null) {
            return null;
        }
        return this.cacheElementMap.get(elementName);
    }

    @Override
    public ClearCache getClearCache(String clearCacheName) {
        if (this.clearCacheMap == null) {
            return null;
        }
        return this.clearCacheMap.get(clearCacheName);
    }

    public List<com.giants.cache.config.properties.elements.CacheElement> getCacheElements() {
        return cacheElements;
    }

    public void setCacheElements(List<com.giants.cache.config.properties.elements.CacheElement> cacheElements) {
        this.cacheElements = cacheElements;
        if (CollectionUtils.isNotEmpty(this.cacheElements)) {
            this.cacheElementMap = new HashMap<>();
            for (com.giants.cache.config.properties.elements.CacheElement cacheElement : cacheElements) {
                this.cacheElementMap.put(cacheElement.getName(), cacheElement);
            }
        }
    }

    public List<com.giants.cache.config.properties.elements.ClearCache> getClearCaches() {
        return clearCaches;
    }

    public void setClearCaches(List<com.giants.cache.config.properties.elements.ClearCache> clearCaches) {
        this.clearCaches = clearCaches;
        if (CollectionUtils.isNotEmpty(this.clearCaches)) {
            this.clearCacheMap = new HashMap<>();
            for (com.giants.cache.config.properties.elements.ClearCache clearCache : clearCaches) {
                this.clearCacheMap.put(clearCache.getName(), clearCache);
            }
        }
    }
}
