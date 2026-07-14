package com.giants.cache.config.properties;

import com.giants.cache.config.elements.CacheModel;
import com.giants.cache.config.properties.elements.MethodCacheModel;
import com.giants.cache.config.properties.elements.ServletCacheModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CacheConfigProperties TODO
 * date time: 2024/12/17 10:55
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CacheConfig implements com.giants.cache.config.CacheConfig {

    private static final long serialVersionUID = -2006592608238378579L;

    private String name;
    private List<MethodCacheModel> methodCacheModels;
    private Map<String, MethodCacheModel> methodCacheModelMap;
    private List<ServletCacheModel> servletCacheModels;
    private Map<String, ServletCacheModel> servletCacheModelMap;

    @Override
    public CacheModel getCacheModel(String modelName) {
        CacheModel cacheModel = null;
        if (this.methodCacheModelMap == null) {
            if (this.servletCacheModelMap != null) {
                cacheModel = this.servletCacheModelMap.get(modelName);
            }
        } else {
            cacheModel = this.methodCacheModelMap.get(modelName);
            if (cacheModel == null && this.servletCacheModelMap != null) {
                cacheModel = this.servletCacheModelMap.get(modelName);
            }
        }
        return cacheModel;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MethodCacheModel> getMethodCacheModels() {
        return methodCacheModels;
    }

    public void setMethodCacheModels(List<MethodCacheModel> methodCacheModels) {
        this.methodCacheModels = methodCacheModels;
        if (CollectionUtils.isNotEmpty(this.methodCacheModels)) {
            this.methodCacheModelMap = new HashMap<>();
            for (MethodCacheModel cacheModel : this.methodCacheModels) {
                this.methodCacheModelMap.put(cacheModel.getName(), cacheModel);
            }
        }
    }

    public List<ServletCacheModel> getServletCacheModels() {
        return servletCacheModels;
    }

    public void setServletCacheModels(List<ServletCacheModel> servletCacheModels) {
        this.servletCacheModels = servletCacheModels;
        if (CollectionUtils.isNotEmpty(this.servletCacheModels)) {
            this.servletCacheModelMap = new HashMap<>();
            for (ServletCacheModel cacheModel : this.servletCacheModels) {
                this.servletCacheModelMap.put(cacheModel.getName(), cacheModel);
            }
        }
    }
}
