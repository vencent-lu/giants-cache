/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.List;

/**
 * @author vencent.lu
 *
 */
public interface ServletCacheModel extends CacheModel {

	/**
	 * @return the cacheElements
	 */
	List<ServletCacheElement> getCacheElements();

	/**
	 * @return the purgeServletCache
	 */
	PurgeServletCache getPurgeServletCache();

}
