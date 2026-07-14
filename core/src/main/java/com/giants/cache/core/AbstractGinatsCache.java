/**
 * 
 */
package com.giants.cache.core;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.config.CacheConfigBuilder;


/**
 * @author vencent.lu
 *
 */
public abstract class AbstractGinatsCache implements GiantsCache {
	
	private static final long serialVersionUID = -2272273784522332848L;
	
	private final CacheConfigBuilder cacheConfigBuilder;

	public AbstractGinatsCache(CacheConfigBuilder cacheConfigBuilder) {
		this.cacheConfigBuilder = cacheConfigBuilder;
	}

	@Override
	public CacheConfigBuilder getCacheConfigBuilder() {
		return cacheConfigBuilder;
	}
}
