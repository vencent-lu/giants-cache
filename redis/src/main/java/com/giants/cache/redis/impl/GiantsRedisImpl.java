/**
 * 
 */
package com.giants.cache.redis.impl;

import java.util.Set;

import com.giants.cache.core.AbstractGinatsCache;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.Element;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.redis.RedisClient;

/**
 * @author vencent.lu
 *
 * Create Date:2014年5月15日
 */
public class GiantsRedisImpl extends AbstractGinatsCache {

	private static final long serialVersionUID = -3665120789171008897L;
	
	private RedisClient redisClient;
		
	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#get(com.giants.cache.CacheKey)
	 */
	@Override
	public Element get(CacheKey key) throws UndefinedCacheModelException {
		return (Element) this.redisClient.get(key);
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#put(com.giants.cache.Element)
	 */
	@Override
	public void put(Element element) throws UndefinedCacheModelException {
		if (element.getTimeToLive() != -1) {
			this.redisClient.set(element.getKey(), element, element.getTimeToLive()
				.intValue());
		} else {
			this.redisClient.set(element.getKey(), element);
		}
		this.redisClient.sadd(new CacheKey(
				element.getKey().getCacheModelName(), element.getKey()
						.getElementConfName()), element.getKey());
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#remove(com.giants.cache.Element)
	 */
	@Override
	public void remove(Element element) throws UndefinedCacheModelException {
		this.redisClient.del(element.getKey());
		this.redisClient.srem(new CacheKey(
				element.getKey().getCacheModelName(), element.getKey()
						.getElementConfName()), element.getKey());
	}

	@Override
	public void removeAll(String cacheModelName, String elementConfName)
			throws UndefinedCacheModelException {
		CacheKey cacheKeySetKey = new CacheKey(cacheModelName, elementConfName);
		Set<byte[]> cateKeyByteSet = this.redisClient.smembers(this.redisClient.serialization(cacheKeySetKey));
		cateKeyByteSet.add(this.redisClient.serialization(cacheKeySetKey));
		this.redisClient.del(cateKeyByteSet.toArray(new byte[][]{}));
	}
	
	public void setRedisClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

}
