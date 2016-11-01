/**
 * 
 */
package com.giants.cache.memcached.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Output;
import com.giants.cache.core.AbstractGinatsCache;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.Element;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.memcached.MemcachedClient;
import com.giants.cache.memcached.MemcachedManager;
import com.giants.common.tools.EncryptionTools;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.exception.XMLParseException;
import com.giants.xmlmapping.exception.XmlDataException;

/**
 * @author vencent.lu
 *
 */
public class GiantsMemcachedImpl extends AbstractGinatsCache {

	private static final long serialVersionUID = 2304220787341967107L;

	public GiantsMemcachedImpl() throws XmlMapException, XmlDataException,
			XMLParseException {
		super();
		MemcachedManager.initialize();
	}
	
	private String createMemcachedKey(CacheKey key) {
        try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		AbstractHessianOutput out = new Hessian2Output(bos);
			out.writeObject(key);
			out.flush();
			return EncryptionTools.MD5(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return EncryptionTools.MD5(key.toString());
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#get(com.giants.cache.CacheKey)
	 */
	@Override
	public Element get(CacheKey key) throws UndefinedCacheModelException {
		return (Element) MemcachedManager.getMemcachedClient(
				key.getCacheModelName()).get(this.createMemcachedKey(key));
	}
	
	private void set(Element element) {
		MemcachedManager.getMemcachedClient(
				element.getKey().getCacheModelName()).set(
				this.createMemcachedKey(element.getKey()), element,
				new Date(element.getTimeToLive() * 1000));
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.GiantsCache#put(com.giants.cache.Element)
	 */
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
	
	private void delete(Element element) {
		MemcachedManager.getMemcachedClient(
				element.getKey().getCacheModelName()).delete(
				this.createMemcachedKey(element.getKey()));
	}

	/* (non-Javadoc)
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
			MemcachedClient memcachedClient = MemcachedManager.getMemcachedClient(cacheModelName);
			for (CacheKey key : cacheElementkeys) {
				memcachedClient.delete(this.createMemcachedKey(key));
			}
		
			
			this.delete(cacheKeysElement);
		}
	}

}
