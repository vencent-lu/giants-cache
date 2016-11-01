/**
 * 
 */
package com.giants.cache.core.session;

import javax.servlet.http.HttpSession;

import com.giants.cache.core.GiantsCacheManager;
import com.giants.cache.core.exception.UndefinedCacheModelException;


/**
 * @author vencent.lu
 *
 */
public abstract class AbstractGiantsSessionCache implements GiantsSessionCache {

	private static final long serialVersionUID = -3226266397155049944L;
	
	private String cacheKeyPrefix;
		
	public AbstractGiantsSessionCache() {
		super();
		GiantsCacheManager.setGiantsSessionCache(this);
	}

	protected String buildCacheKey(String sessionId) {
		return new StringBuffer(this.cacheKeyPrefix).append(':')
				.append(sessionId).toString();
	}

	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}
	
	@Override
	public void expireSession(HttpSession session)
			throws UndefinedCacheModelException {		
	}

}
