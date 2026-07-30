/**
 * 
 */
package com.giants.cache.config.elements;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public interface CacheModel extends Serializable {

	CacheElement createCacheElement(String name);
	CacheElement getCacheElement(String elementName);

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the defaultCache
	 */
	Boolean isDefaultCache();

	/**
	 * @return the defaultTimeToLive
	 */
	Long getDefaultTimeToLive();

}
