package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * ExclusionMethod TODO
 * date time: 2024/12/17 11:05
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class ExclusionMethod implements com.giants.cache.config.elements.ExclusionMethod{
    private static final long serialVersionUID = -8338835720087411293L;

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
        ExclusionMethod that = (ExclusionMethod) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
