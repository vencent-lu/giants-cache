package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * ExclusionQueryParam TODO
 * date time: 2024/12/17 16:31
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class ExclusionQueryParam implements com.giants.cache.config.elements.ExclusionQueryParam{
    private static final long serialVersionUID = -5699945983633324027L;

    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExclusionQueryParam that = (ExclusionQueryParam) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
