package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.CookieName;
import com.giants.cache.config.elements.ExclusionQueryParam;
import com.giants.cache.config.elements.ServletCacheElement;
import com.giants.common.collections.CollectionUtils;
import com.giants.common.regex.Pattern;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="servletCacheElement")
public class ServletCacheElementXml extends CacheElementXml implements ServletCacheElement {

	private static final long serialVersionUID = -3644313733390730398L;

	@XmlAttribute
	private String regex;

	private Pattern uriPattern;

	@XmlAttribute
	private boolean queryParam = true;

	@XmlAttribute
	private boolean cookie = false;

	@XmlManyElement
	private List<ExclusionQueryParamXml> exclusionQueryParams;

	private List<String> exclusionQueryParamList;

	@XmlManyElement
	private List<CookieNameXml> cookieNames;

	private List<String> cookieNameList;

	public ServletCacheElementXml() {
		super();
	}

	/**
	 * @param name name
	 * @param timeToLive timeToLive
	 */
	public ServletCacheElementXml(String name, Long timeToLive) {
		super(name, timeToLive);
		// TODO Auto-generated constructor stub
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

	/**
	 * @return the regex
	 */
	//@Override
	public String getRegex() {
		return regex;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		this.uriPattern = Pattern.compile(this.regex);
	}

	@Override
	public boolean isCookie() {
		return cookie;
	}

	public void setCookie(boolean cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the queryParam
	 */
	@Override
	public boolean isQueryParam() {
		return queryParam;
	}

	/**
	 * @param queryParam the queryParam to set
	 */
	public void setQueryParam(boolean queryParam) {
		this.queryParam = queryParam;
	}

	/**
	 * @param exclusionQueryParams the exclusionQueryParams to set
	 */
	public void setExclusionQueryParams(
			List<ExclusionQueryParamXml> exclusionQueryParams) {
		this.exclusionQueryParams = exclusionQueryParams;
		if (CollectionUtils.isNotEmpty(this.exclusionQueryParams)) {
			this.exclusionQueryParamList = new ArrayList<>();
			for (ExclusionQueryParam exclusionQueryParam : this.exclusionQueryParams) {
				this.exclusionQueryParamList.add(exclusionQueryParam.getName());
			}
		}
	}

	public void setCookieNames(List<CookieNameXml> cookieNames) {
		this.cookieNames = cookieNames;
		if (CollectionUtils.isNotEmpty(this.cookieNames)) {
			this.cookieNameList = new ArrayList<>();
			for (CookieName cookieName : this.cookieNames) {
				this.cookieNameList.add(cookieName.getName());
			}
		}
	}
}
