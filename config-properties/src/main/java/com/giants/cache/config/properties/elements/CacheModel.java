package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * CacheModel TODO
 * date time: 2024/12/17 14:17
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public abstract class CacheModel implements com.giants.cache.config.elements.CacheModel{
    private static final long serialVersionUID = 3567273361310820704L;

    protected String name;
    private Boolean defaultCache = false;
    private Long defaultTimeToLive = 300L;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Boolean isDefaultCache() {
        return defaultCache;
    }

    public void setDefaultCache(Boolean defaultCache) {
        this.defaultCache = defaultCache;
    }

    @Override
    public Long getDefaultTimeToLive() {
        return defaultTimeToLive;
    }

    public void setDefaultTimeToLive(Long defaultTimeToLive) {
        this.defaultTimeToLive = defaultTimeToLive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheModel that = (CacheModel) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
