package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * ClearElement TODO
 * date time: 2024/12/17 13:55
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.0
 */
public class ClearElement implements com.giants.cache.config.elements.ClearElement{
    private static final long serialVersionUID = -1159318491779008439L;

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
        ClearElement that = (ClearElement) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
