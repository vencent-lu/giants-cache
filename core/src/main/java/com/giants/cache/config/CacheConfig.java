/**
 * 
 */
package com.giants.cache.config;

import java.io.Serializable;

import com.giants.cache.config.elements.CacheModel;

/**
 * @author vencent.lu
 *
 */
public interface CacheConfig extends Serializable {

	CacheModel getCacheModel(String modelName);

	String getName();

	//List<MethodCacheModel> getMethodCacheModels();

	/**
	 * @return the servletCacheModels
	 */
	//List<ServletCacheModel> getServletCacheModels();

}
