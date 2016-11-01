/**
 * 
 */
package com.giants.cache.core;

import java.io.Serializable;

import com.giants.cache.core.exception.UndefinedCacheModelException;

/**
 * @author vencent.lu
 *
 */
public interface GiantsCache  extends Serializable {
	
	void setCacheConfigFilePath(String cacheConfigFilePath);
	String getCacheConfigFilePath();
	Element get(CacheKey key) throws UndefinedCacheModelException;
	void put(Element element) throws UndefinedCacheModelException;
	void remove(Element element) throws UndefinedCacheModelException;
	void removeAll(String cacheModelName,String elementConfName) throws UndefinedCacheModelException;

}
