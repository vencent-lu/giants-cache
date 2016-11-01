/**
 * 
 */
package com.giants.cache.ehcache.impl;

import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.core.session.AbstractGiantsSessionCache;

/**
 * @author vencent.lu
 *
 */
public class GiantsSessionEhcacheImpl extends AbstractGiantsSessionCache {

	private static final long serialVersionUID = -1593267354085944178L;
		
	private String cacheModel;
	
	private CacheManager cacheManager = CacheManager.getInstance();
	
	private Cache getCache() throws UndefinedCacheModelException {
		Cache cache = this.cacheManager.getCache(this.cacheModel);
		if (cache == null) {
			throw new UndefinedCacheModelException(this.cacheModel);
		}
		return cache;
	}
	
	

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#putSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void putSession(HttpSession session)
			throws UndefinedCacheModelException {
		Element element = new Element(this.buildCacheKey(session.getId()),
				session);
		element.setTimeToIdle(session.getMaxInactiveInterval() == -1 ? 0
				: session.getMaxInactiveInterval());
		this.getCache().put(element);

	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#getSession(java.lang.String)
	 */
	@Override
	public HttpSession getSession(String sessionId) throws UndefinedCacheModelException {
		Element element = this.getCache().get(this.buildCacheKey(sessionId));
		if (element == null) {
			return null;
		}
		return (HttpSession)element.getObjectValue();
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#removeSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void removeSession(HttpSession session) throws UndefinedCacheModelException {
		this.getCache().remove(this.buildCacheKey(session.getId()));
	}

	public void setCacheModel(String cacheModel) {
		this.cacheModel = cacheModel;
	}

}
