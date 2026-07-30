package com.giants.cache.config.properties.elements;

import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * CacheElement TODO
 * date time: 2024/12/17 13:41
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CacheElement implements com.giants.cache.config.elements.CacheElement{
    private static final long serialVersionUID = -7939556060161101321L;

    private String name;
    private Long timeToLive = 300L;
    private List<ExclusionMethod> exclusionMethods;
    private Map<String, ExclusionMethod> exclusionMethodMap;
    public List<CleanMethod> cleanMethods;
    private Map<String, CleanMethod> cleanMethodMap;

    public CacheElement() {
    }

    public CacheElement(String name, Long timeToLive) {
        this.name = name;
        this.timeToLive = timeToLive;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getTimeToLive() {
        return this.timeToLive;
    }

    public void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    @Override
    public boolean isExclusionMethod(String methodName) {
        if (this.exclusionMethodMap == null) {
            return false;
        }
        return this.exclusionMethodMap.get(methodName) != null;
    }

    public List<ExclusionMethod> getExclusionMethods() {
        return exclusionMethods;
    }

    public void setExclusionMethods(List<ExclusionMethod> exclusionMethods) {
        this.exclusionMethods = exclusionMethods;
        if (CollectionUtils.isNotEmpty(this.exclusionMethods)) {
            this.exclusionMethodMap = new HashMap<>();
            for (ExclusionMethod exclusionMethod : exclusionMethods) {
                this.exclusionMethodMap.put(exclusionMethod.getName(), exclusionMethod);
            }
        }
    }

    @Override
    public boolean isCleanMethod(String methodName) {
        if (this.cleanMethodMap == null) {
            return false;
        }
        return this.cleanMethodMap.get(methodName) != null;
    }

    public List<CleanMethod> getCleanMethods() {
        return cleanMethods;
    }

    public void setCleanMethods(List<CleanMethod> cleanMethods) {
        this.cleanMethods = cleanMethods;
        if (CollectionUtils.isNotEmpty(this.cleanMethods)) {
            this.cleanMethodMap = new HashMap<>();
            for (CleanMethod cleanMethod : cleanMethods) {
                this.cleanMethodMap.put(cleanMethod.getName(), cleanMethod);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheElement that = (CacheElement) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
