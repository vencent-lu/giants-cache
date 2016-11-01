/**
 * 
 */
package com.giants.cache.nocaching.impl;

import com.giants.cache.core.AbstractGinatsCache;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.Element;
import com.giants.cache.core.exception.UndefinedCacheModelException;

/**
 * @author vencent.lu
 *
 */
public class NoCachingImpl extends AbstractGinatsCache {
	
	private static final long serialVersionUID = 1660345655961185987L;

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#get(com.giants.cache.CacheKey)
	 */
	@Override
	public Element get(CacheKey key) throws UndefinedCacheModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#put(com.giants.cache.Element)
	 */
	@Override
	public void put(Element element) throws UndefinedCacheModelException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#remove(com.giants.cache.Element)
	 */
	@Override
	public void remove(Element element) throws UndefinedCacheModelException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAll(String cacheModelName, String elementConfName)
			throws UndefinedCacheModelException {
		// TODO Auto-generated method stub
		
	}

}
