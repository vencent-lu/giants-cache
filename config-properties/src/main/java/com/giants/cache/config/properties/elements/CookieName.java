package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * CookieName TODO
 * date time: 2024/12/17 16:36
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class CookieName implements com.giants.cache.config.elements.CookieName{
    private static final long serialVersionUID = -3794870436063998137L;

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
        CookieName that = (CookieName) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
