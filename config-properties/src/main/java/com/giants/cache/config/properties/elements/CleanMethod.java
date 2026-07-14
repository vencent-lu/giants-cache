package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * CleanMethod TODO
 * date time: 2024/12/17 13:38
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CleanMethod implements com.giants.cache.config.elements.CleanMethod{
    private static final long serialVersionUID = -3355819502309691424L;

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
        CleanMethod that = (CleanMethod) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
