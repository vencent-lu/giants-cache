/**
 * 
 */
package com.giants.cache.memcached.impl;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.core.session.AbstractGiantsSessionCache;
import com.giants.cache.core.session.GiantsSession;
import com.giants.cache.memcached.MemcachedClient;
import com.giants.cache.memcached.MemcachedManager;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.exception.XMLParseException;
import com.giants.xmlmapping.exception.XmlDataException;

/**
 * @author vencent.lu
 *
 */
public class GiantsSessionMemcachedImpl extends AbstractGiantsSessionCache {

	private static final long serialVersionUID = -4680917182046027167L;
	
	private MemcachedClient memcachedClient;
	
	public GiantsSessionMemcachedImpl() throws XmlMapException,
			XmlDataException, XMLParseException {
		super();
		MemcachedManager.initialize();
		this.memcachedClient = MemcachedManager.getMemcachedClient("SESSION");
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#putSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void putSession(HttpSession session)
			throws UndefinedCacheModelException {
		String key = this.buildCacheKey(session.getId());
		if (session.getMaxInactiveInterval() != -1) {
			this.memcachedClient.set(key, session,
					new Date(session.getMaxInactiveInterval() * 1000));
		} else {
			this.memcachedClient.set(key, session);
		}
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#getSession(java.lang.String)
	 */
	@Override
	public HttpSession getSession(String sessionId)
			throws UndefinedCacheModelException {
		return (GiantsSession) this.memcachedClient.get(this
				.buildCacheKey(sessionId));
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#removeSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void removeSession(HttpSession session)
			throws UndefinedCacheModelException {
		this.memcachedClient.delete(this.buildCacheKey(session.getId()));
	}

	@Override
	public void expireSession(HttpSession session)
			throws UndefinedCacheModelException {
		this.memcachedClient.set(this.buildCacheKey(session.getId()), session,
				new Date(session.getMaxInactiveInterval() * 1000));
	}
	
	

}
