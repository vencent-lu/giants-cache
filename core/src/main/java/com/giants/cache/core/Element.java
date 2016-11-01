/**
 * 
 */
package com.giants.cache.core;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public class Element implements Serializable, Cloneable {

	private static final long serialVersionUID = 6386324855497173609L;
	
	private CacheKey key;
	private Object value;
	private Long beginCacheTime;
	private Long timeToLive;

	/**
	 * @param cacheModelName
	 * @param key
	 * @param value
	 * @param timeToLive
	 */
	public Element(String cacheModelName, CacheKey key, Object value,
			Long timeToLive) {
		super();
		this.key = key;
		this.value = value;
		this.beginCacheTime = System.currentTimeMillis();
		this.timeToLive = timeToLive;
	}
	
	public boolean isExpired() {
		if (this.timeToLive == -1) {
			return false;
		}
		return System.currentTimeMillis() - beginCacheTime > timeToLive * 1000;
	}
	
	public void update(Object value) {
		this.value = value;
		this.beginCacheTime = System.currentTimeMillis();
	}
	
	public void update(Object value, Long timeToLive) {
		this.update(value);
		this.timeToLive = timeToLive;
	}

	public CacheKey getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

	public Long getBeginCacheTime() {
		return beginCacheTime;
	}

	public Long getTimeToLive() {
		return timeToLive;
	}
	
}
