/**
 * 
 */
package com.giants.cache.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
public class ServletCacheKey extends CacheKey {

	private static final long serialVersionUID = 1707060737974930312L;
	
	private Map<String, String> parameterMap;
	private Map<String, String> cookiesMap;
	
	public ServletCacheKey() {
		super();
		this.init();
	}

	/**
	 * @param cacheModelName cacheModelName
	 * @param keyName keyName
	 */
	public ServletCacheKey(String cacheModelName, String keyName) {
		super(cacheModelName, keyName);
		this.init();
	}

	/**
	 * @param cacheModelName cacheModelName
	 * @param keyName keyName
	 * @param arguments arguments
	 */
	public ServletCacheKey(String cacheModelName, String keyName,
			List<Object> arguments) {
		super(cacheModelName, keyName, arguments);
		this.init();
	}
	
	private void init() {
		this.parameterMap = new HashMap<String, String>();
		this.cookiesMap = new HashMap<String, String>();
		this.addArgument(this.parameterMap);
		this.addArgument(this.cookiesMap);
	}
	
	public void setParameter(String key, String value) {
		this.parameterMap.put(key, value);
	}
	
	public String getParameter(String key) {
		return this.parameterMap.get(key);
	}
	
	public void setCookie(String key, String value) {
		this.cookiesMap.put(key, value);
	}
	
	public String getCookie(String key) {
		return this.cookiesMap.get(key);
	}

	/**
	 * @return the parameterMap
	 */
	public Map<String, String> getParameterMap() {
		return parameterMap;
	}

	/**
	 * @param parameterMap the parameterMap to set
	 */
	public void setParameterMap(Map<String, String> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public Map<String, String> getCookiesMap() {
		return cookiesMap;
	}

	public void setCookiesMap(Map<String, String> cookiesMap) {
		this.cookiesMap = cookiesMap;
	}
}
