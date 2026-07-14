/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.List;

/**
 * @author vencent.lu
 *
 */
public interface MethodCacheModel extends CacheModel {

	ClearCache getClearCache(String clearCacheName);

	//List<CacheElement> getCacheElements();

	//List<ClearCache> getClearCaches();

}
