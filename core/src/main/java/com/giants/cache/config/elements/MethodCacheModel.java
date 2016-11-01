/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class MethodCacheModel extends CacheModel {

	private static final long serialVersionUID = 3046801121535154683L;
		
	@XmlManyElement
	private List<CacheElement> cacheElements;
	
	private Map<String,CacheElement> cacheElementMap;
	
	@XmlManyElement
	private List<ClearCache> clearCaches;
	
	private Map<String,ClearCache> clearCacheMap;
		
	/* (non-Javadoc)
	 * @see com.giants.cache.config.elements.CacheModel#createCacheElement(java.lang.String)
	 */
	@Override
	public CacheElement createCacheElement(String name) {
		return new CacheElement(name, this.getDefaultTimeToLive());
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.config.elements.CacheModel#getCacheElement(java.lang.String)
	 */
	@Override
	public CacheElement getCacheElement(String elementName) {
		if (this.cacheElementMap == null) {
			return null;
		}
		return this.cacheElementMap.get(elementName);
	}
	
	public ClearCache getClearCache(String clearCacheName) {
		if (this.clearCacheMap == null) {
			return null;
		}
		return this.clearCacheMap.get(clearCacheName);
	}
	
	public List<CacheElement> getCacheElements() {
		return cacheElements;
	}

	public void setCacheElements(List<CacheElement> cacheElements) {
		this.cacheElements = cacheElements;
		if (CollectionUtils.isNotEmpty(this.cacheElements)) {
			this.cacheElementMap = new HashMap<String,CacheElement>();
			for (CacheElement cacheElement : cacheElements) {
				this.cacheElementMap.put(cacheElement.getName(), cacheElement);
			}
		}
	}

	public List<ClearCache> getClearCaches() {
		return clearCaches;
	}

	public void setClearCaches(List<ClearCache> clearCaches) {
		this.clearCaches = clearCaches;
		if (CollectionUtils.isNotEmpty(this.clearCaches)) {
			this.clearCacheMap = new HashMap<String,ClearCache>();
			for (ClearCache clearCache : clearCaches) {
				this.clearCacheMap.put(clearCache.getName(), clearCache);
			}
		}
	}

}
