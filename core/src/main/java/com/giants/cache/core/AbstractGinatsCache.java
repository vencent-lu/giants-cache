/**
 * 
 */
package com.giants.cache.core;

import com.giants.cache.common.CacheConstants;


/**
 * @author vencent.lu
 *
 */
public abstract class AbstractGinatsCache implements GiantsCache {
	
	private static final long serialVersionUID = -2272273784522332848L;
	
	private String cacheConfigFilePath = CacheConstants.DEFAULT_CONFIG_FILE_PATH;
	
	public String getCacheConfigFilePath() {
		return cacheConfigFilePath;
	}

	public void setCacheConfigFilePath(String cacheConfigFilePath) {
		this.cacheConfigFilePath = cacheConfigFilePath;
	}

}
