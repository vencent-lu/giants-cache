package com.giants.cache.config.properties.elements;

import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.PurgeServletCache;
import com.giants.cache.config.elements.ServletCacheElement;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ServletCacheModel TODO
 * date time: 2024/12/17 16:54
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class ServletCacheModel extends CacheModel implements com.giants.cache.config.elements.ServletCacheModel{
    private static final long serialVersionUID = 8061514172403204216L;

    private com.giants.cache.config.properties.elements.PurgeServletCache purgeServletCache;
    private List<com.giants.cache.config.properties.elements.ServletCacheElement> cacheElements;
    private Map<String, com.giants.cache.config.properties.elements.ServletCacheElement> cacheElementMap;

    @Override
    public CacheElement createCacheElement(String name) {
        return new com.giants.cache.config.properties.elements.ServletCacheElement(name, this.getDefaultTimeToLive());
    }

    @Override
    public CacheElement getCacheElement(String elementName) {
        if (this.cacheElementMap == null) {
            return null;
        }
        return this.cacheElementMap.get(elementName);
    }

    @Override
    public List<ServletCacheElement> getCacheElements() {
        return new ArrayList<ServletCacheElement>(this.cacheElements);
    }

    public void setCacheElements(List<com.giants.cache.config.properties.elements.ServletCacheElement> cacheElements) {
        this.cacheElements = cacheElements;
        if (CollectionUtils.isNotEmpty(this.cacheElements)) {
            this.cacheElementMap = new HashMap<>();
            for (com.giants.cache.config.properties.elements.ServletCacheElement cacheElement : cacheElements) {
                this.cacheElementMap.put(cacheElement.getName(), cacheElement);
            }
        }
    }

    @Override
    public PurgeServletCache getPurgeServletCache() {
        return this.purgeServletCache;
    }

    public void setPurgeServletCache(com.giants.cache.config.properties.elements.PurgeServletCache purgeServletCache) {
        this.purgeServletCache = purgeServletCache;
    }
}
