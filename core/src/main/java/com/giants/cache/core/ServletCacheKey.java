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
	private Map<String, Object> sessionMap;
	
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
		this.sessionMap = new HashMap<String, Object>();
		this.addArgument(this.parameterMap);
		this.addArgument(this.sessionMap);
	}
	
	public void setParameter(String key, String value) {
		this.parameterMap.put(key, value);
	}
	
	public String getParameter(String key) {
		return this.parameterMap.get(key);
	}
	
	public void setSession(String key, Object value) {
		this.sessionMap.put(key, value);
	}
	
	public Object getSession(String key) {
		return this.sessionMap.get(key);
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

	/**
	 * @return the sessionMap
	 */
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	/**
	 * @param sessionMap the sessionMap to set
	 */
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

}
