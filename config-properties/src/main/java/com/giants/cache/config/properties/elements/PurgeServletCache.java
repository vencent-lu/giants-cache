package com.giants.cache.config.properties.elements;

import java.util.List;
import java.util.Objects;

/**
 * PurgeServletCache TODO
 * date time: 2024/12/17 14:47
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class PurgeServletCache implements com.giants.cache.config.elements.PurgeServletCache{
    private static final long serialVersionUID = -7586547210856540329L;

    private String name;
    private String purgeURIPrefix;
    private List<PurgeIP> purgeIPs;

    @Override
    public boolean allowPurge(String ip) {
        if (this.purgeIPs == null) {
            return false;
        }
        for (com.giants.cache.config.elements.PurgeIP purgeIP : this.purgeIPs) {
            if (purgeIP.getValue().equals(ip)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPurgeURIPrefix() {
        return this.purgeURIPrefix;
    }

    public void setPurgeURIPrefix(String purgeURIPrefix) {
        this.purgeURIPrefix = purgeURIPrefix;
    }

    public void setPurgeIPs(List<PurgeIP> purgeIPs) {
        this.purgeIPs = purgeIPs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurgeServletCache that = (PurgeServletCache) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
