/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.ArrayList;
import java.util.List;

import com.giants.common.collections.CollectionUtils;
import com.giants.common.regex.Pattern;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class ServletCacheElement extends CacheElement {

	private static final long serialVersionUID = -3644313733390730398L;
	
	@XmlAttribute
	private String regex;
	
	private Pattern uriPattern;
	
	@XmlAttribute
	private boolean queryParam = true;
	
	@XmlAttribute
	private boolean cookie = false;
	
	@XmlManyElement
	private List<ExclusionQueryParam> exclusionQueryParams;

	private List<String> exclusionQueryParamList;
	
	@XmlManyElement
	private List<CookieName> cookieNames;

	private List<String> cookieNameList;

	public ServletCacheElement() {
		super();
	}

	/**
	 * @param name name
	 * @param timeToLive timeToLive
	 */
	public ServletCacheElement(String name, Long timeToLive) {
		super(name, timeToLive);
		// TODO Auto-generated constructor stub
	}
	
	public boolean isAllowAccordingParam(String paramName) {
		if (CollectionUtils.isEmpty(this.exclusionQueryParams)) {
			return true;
		}
		return !this.exclusionQueryParamList.contains(paramName);
	}
	
	public boolean isAllowCookieName(String cookieName) {
		if (CollectionUtils.isEmpty(this.cookieNameList)) {
			return false;
		}
		return this.cookieNameList.contains(cookieName);
	}
	
	public Pattern getURIPattern() {
		return this.uriPattern;
	}

	/**
	 * @return the regex
	 */
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

	public boolean isCookie() {
		return cookie;
	}

	public void setCookie(boolean cookie) {
		this.cookie = cookie;
	}

	/**
	 * @return the queryParam
	 */
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
			List<ExclusionQueryParam> exclusionQueryParams) {
		this.exclusionQueryParams = exclusionQueryParams;
		if (CollectionUtils.isNotEmpty(this.exclusionQueryParams)) {
			this.exclusionQueryParamList = new ArrayList<String>();
			for (ExclusionQueryParam exclusionQueryParam : this.exclusionQueryParams) {
				this.exclusionQueryParamList.add(exclusionQueryParam.getName());
			}
		}
	}

	public void setCookieNames(List<CookieName> cookieNames) {
		this.cookieNames = cookieNames;
		if (CollectionUtils.isNotEmpty(this.cookieNames)) {
			this.cookieNameList = new ArrayList<String>();
			for (CookieName cookieName : this.cookieNames) {
				this.cookieNameList.add(cookieName.getName());
			}
		}
	}
}
