/**
 * 
 */
package com.giants.cache.ehcache.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import com.giants.cache.core.AbstractGinatsCache;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.Element;
import com.giants.cache.core.exception.UndefinedCacheModelException;

/**
 * @author vencent.lu
 *
 */
public class GiantsEhcacheImpl extends AbstractGinatsCache {

	private static final long serialVersionUID = -7344306333781609447L;
	
	private CacheManager cacheManager = CacheManager.getInstance();
	
	private Cache getCacheModel(String cacheModelName) throws UndefinedCacheModelException {
		Cache cache = this.cacheManager.getCache(cacheModelName);
		if (cache == null) {
			throw new UndefinedCacheModelException(cacheModelName);
		}
		return cache;
	}

	@Override
	public Element get(CacheKey key)
			throws UndefinedCacheModelException {
		net.sf.ehcache.Element element = this.getCacheModel(key.getCacheModelName()).get(key);
		if (element == null) {
			return null;
		}
		return (Element) element.getObjectValue();
	}
	
	private void set(Element element) throws UndefinedCacheModelException {
		this.getCacheModel(element.getKey().getCacheModelName()).put(
				new net.sf.ehcache.Element(element.getKey(), element));
	}

	@Override
	public void put(Element element) throws UndefinedCacheModelException {
		this.set(element);
		CacheKey cacheKeyList = new CacheKey(element.getKey().getCacheModelName(), element.getKey().getElementConfName());
		Element cacheKeysElement = this.get(cacheKeyList);
		if (cacheKeysElement == null) {
			cacheKeysElement = new Element(cacheKeyList.getCacheModelName(), cacheKeyList, new ArrayList<CacheKey>(), 0L);
		}
		@SuppressWarnings("unchecked")
		List<CacheKey> cacheElementkeys = (List<CacheKey>)cacheKeysElement.getValue();
		if (cacheElementkeys == null) {
			cacheElementkeys = new ArrayList<CacheKey>();
		}
		if (!cacheElementkeys.contains(element.getKey())) {
			cacheElementkeys.add(element.getKey());
			this.set(cacheKeysElement);
		}
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	private void delete(Element element) throws UndefinedCacheModelException {
		this.getCacheModel(element.getKey().getCacheModelName()).remove(
				element.getKey());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.giants.cache.GiantsCache#remove(com.giants.cache.Element)
	 */
	@Override
	public void remove(Element element) throws UndefinedCacheModelException {
		this.delete(element);
		CacheKey cacheKeyList = new CacheKey(element.getKey().getCacheModelName(), element.getKey().getKeyName());
		Element cacheKeysElement = this.get(cacheKeyList);
		if (cacheKeysElement == null) {
			cacheKeysElement = new Element(cacheKeyList.getCacheModelName(), cacheKeyList, new ArrayList<CacheKey>(), 0L);
		}
		@SuppressWarnings("unchecked")
		List<CacheKey> cacheElementkeys = (List<CacheKey>)cacheKeysElement.getValue();
		if (cacheElementkeys != null && cacheElementkeys.contains(element.getKey())) {
			cacheElementkeys.remove(element.getKey());
			this.set(cacheKeysElement);
		}
	}

	@Override
	public void removeAll(String cacheModelName, String elementConfName)
			throws UndefinedCacheModelException {
		CacheKey cacheKeyList = new CacheKey(cacheModelName, elementConfName);
		Element cacheKeysElement = this.get(cacheKeyList);
		if (cacheKeysElement == null) {
			cacheKeysElement = new Element(cacheModelName, cacheKeyList, new ArrayList<CacheKey>(), 0L);
		}
		@SuppressWarnings("unchecked")
		List<CacheKey> cacheElementkeys = (List<CacheKey>)cacheKeysElement.getValue();
		if (cacheElementkeys != null) {
			this.getCacheModel(cacheModelName).removeAll(cacheElementkeys);
			this.delete(cacheKeysElement);
		}
	}
	
	public void removeCacheKey(CacheKey cacheKey) throws UndefinedCacheModelException {
		CacheKey cacheKeyList = new CacheKey(cacheKey.getCacheModelName(), cacheKey.getKeyName());
		Element cacheKeysElement = this.get(cacheKeyList);
		if (cacheKeysElement == null) {
			cacheKeysElement = new Element(cacheKeyList.getCacheModelName(), cacheKeyList, new ArrayList<CacheKey>(), 0L);
		}
		@SuppressWarnings("unchecked")
		List<CacheKey> cacheElementkeys = (List<CacheKey>)cacheKeysElement.getValue();
		if (cacheElementkeys != null && cacheElementkeys.contains(cacheKey)) {
			cacheElementkeys.remove(cacheKey);
			this.set(cacheKeysElement);
		}
	}

}
