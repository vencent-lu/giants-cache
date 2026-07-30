package com.giants.cache.config.properties.elements;

import com.giants.cache.config.elements.ClearElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ClearCache TODO
 * date time: 2024/12/17 14:11
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class ClearCache implements com.giants.cache.config.elements.ClearCache{
    private static final long serialVersionUID = 3098562413678980541L;

    private String name;
    private List<com.giants.cache.config.properties.elements.ClearElement> clearElements;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<ClearElement> getClearElements() {
        return new ArrayList<ClearElement>(this.clearElements);
    }

    public void setClearElements(List<com.giants.cache.config.properties.elements.ClearElement> clearElements) {
        this.clearElements = clearElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClearCache that = (ClearCache) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
