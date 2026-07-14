package com.giants.cache.config.properties.elements;

import com.giants.common.collections.CollectionUtils;
import com.giants.common.regex.Pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * ServletCacheElement TODO
 * date time: 2024/12/17 14:53
 * Copyright 2024 github.com/vencent-lu/giants-cache Inc. All rights reserved.
 *
 * @author vencent-lu
 * @since 1.2
 */
public class ServletCacheElement extends CacheElement implements com.giants.cache.config.elements.ServletCacheElement {
    private static final long serialVersionUID = -4771267726653301443L;

    private String regex;
    private Pattern uriPattern;
    private boolean queryParam = true;
    private boolean cookie = false;
    private List<ExclusionQueryParam> exclusionQueryParams;
    private List<String> exclusionQueryParamList;
    private List<CookieName> cookieNames;
    private List<String> cookieNameList;

    public ServletCacheElement() {
    }

    public ServletCacheElement(String name, Long timeToLive) {
        super(name, timeToLive);
    }

    @Override
    public boolean isAllowAccordingParam(String paramName) {
        if (CollectionUtils.isEmpty(this.exclusionQueryParamList)) {
            return true;
        }
        return !this.exclusionQueryParamList.contains(paramName);
    }

    @Override
    public boolean isAllowCookieName(String cookieName) {
        if (CollectionUtils.isEmpty(this.cookieNameList)) {
            return false;
        }
        return this.cookieNameList.contains(cookieName);
    }

    @Override
    public Pattern getURIPattern() {
        return this.uriPattern;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
        this.uriPattern = Pattern.compile(this.regex);
    }

    @Override
    public boolean isCookie() {
        return this.cookie;
    }

    public void setCookie(boolean cookie) {
        this.cookie = cookie;
    }

    @Override
    public boolean isQueryParam() {
        return this.queryParam;
    }

    public void setQueryParam(boolean queryParam) {
        this.queryParam = queryParam;
    }

    public List<ExclusionQueryParam> getExclusionQueryParams() {
        return exclusionQueryParams;
    }

    public List<CookieName> getCookieNames() {
        return cookieNames;
    }

    public void setExclusionQueryParams(List<ExclusionQueryParam> exclusionQueryParams) {
        this.exclusionQueryParams = exclusionQueryParams;
        if (CollectionUtils.isNotEmpty(this.exclusionQueryParams)) {
            this.exclusionQueryParamList = new ArrayList<>();
            for (com.giants.cache.config.elements.ExclusionQueryParam exclusionQueryParam : this.exclusionQueryParams) {
                this.exclusionQueryParamList.add(exclusionQueryParam.getName());
            }
        }
    }

    public void setCookieNames(List<CookieName> cookieNames) {
        this.cookieNames = cookieNames;
        if (CollectionUtils.isNotEmpty(this.cookieNames)) {
            this.cookieNameList = new ArrayList<>();
            for (com.giants.cache.config.elements.CookieName cookieName : this.cookieNames) {
                this.cookieNameList.add(cookieName.getName());
            }
        }
    }
}
