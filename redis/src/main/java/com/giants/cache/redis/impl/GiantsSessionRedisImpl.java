/**
 * 
 */
package com.giants.cache.redis.impl;

import java.io.Serializable;

import javax.servlet.http.HttpSession;

import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.core.session.AbstractGiantsSessionCache;
import com.giants.cache.core.session.GiantsSession;
import com.giants.cache.redis.RedisClient;

/**
 * @author vencent.lu
 *
 */
public class GiantsSessionRedisImpl extends AbstractGiantsSessionCache {

	private static final long serialVersionUID = -3393747772040225860L;
	
	private RedisClient redisClient;

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#putSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void putSession(HttpSession session)
			throws UndefinedCacheModelException {
		String key = this.buildCacheKey(session.getId());
		if (session.getMaxInactiveInterval() != -1) {
			this.redisClient.set(key, (Serializable)session, session.getMaxInactiveInterval());
		} else {
			this.redisClient.set(key, (Serializable)session);
		}
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#getSession(java.lang.String)
	 */
	@Override
	public HttpSession getSession(String sessionId)
			throws UndefinedCacheModelException {
		String key = this.buildCacheKey(sessionId);
		GiantsSession session = (GiantsSession)this.redisClient.get(key);
		if (session!=null && session.getMaxInactiveInterval() != -1) {
			this.redisClient.expire(key, session.getMaxInactiveInterval());
			/**
			 * 推算上次访问时间
			 * 每次访问session后，如果没有修改session，只会调用expire重置到期时间
			 * session 中的lastAccessedTime 并没有更新，因此需要重新计算
			 */
			session.setLastAccessedTime(System.currentTimeMillis()
					- session.getMaxInactiveInterval() * 1000L
					+ this.redisClient.pttl(key));
		}
		return session;
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.session.GiantsSessionCache#removeSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void removeSession(HttpSession session)
			throws UndefinedCacheModelException {
		this.redisClient.del(this.buildCacheKey(session.getId()));
	}
	
	/* (non-Javadoc)
	 * @see com.giants.cache.session.AbstractGiantsSessionCache#expireSession(javax.servlet.http.HttpSession)
	 */
	@Override
	public void expireSession(HttpSession session)
			throws UndefinedCacheModelException {
		this.redisClient.expire(this.buildCacheKey(session.getId()), session.getMaxInactiveInterval());
	}

	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}
	
	

	
}
