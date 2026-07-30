package com.giants.cache.config.properties.elements;

import java.util.Objects;

/**
 * PurgeIP TODO
 * date time: 2024/12/17 10:55
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class PurgeIP implements com.giants.cache.config.elements.PurgeIP{
    private static final long serialVersionUID = -7360907362211566457L;

    private String value;

    @Override
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurgeIP purgeIP = (PurgeIP) o;
        return Objects.equals(getValue(), purgeIP.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
