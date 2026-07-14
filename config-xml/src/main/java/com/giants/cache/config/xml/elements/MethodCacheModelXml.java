package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.ClearCache;
import com.giants.cache.config.elements.MethodCacheModel;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="methodCacheModel")
public class MethodCacheModelXml extends CacheModelXml implements MethodCacheModel {

	private static final long serialVersionUID = 3046801121535154683L;
		
	@XmlManyElement
	private List<CacheElementXml> cacheElements;
	
	private Map<String,CacheElement> cacheElementMap;
	
	@XmlManyElement
	private List<ClearCacheXml> clearCaches;
	
	private Map<String,ClearCache> clearCacheMap;

	@Override
	public CacheElement createCacheElement(String name) {
		return new CacheElementXml(name, this.getDefaultTimeToLive());
	}

	@Override
	public CacheElement getCacheElement(String elementName) {
		if (this.cacheElementMap == null) {
			return null;
		}
		return this.cacheElementMap.get(elementName);
	}

	@Override
	public ClearCache getClearCache(String clearCacheName) {
		if (this.clearCacheMap == null) {
			return null;
		}
		return this.clearCacheMap.get(clearCacheName);
	}

	//@Override
	/*public List<CacheElement> getCacheElements() {
		return new ArrayList<CacheElement>(this.cacheElements);
	}*/

	public void setCacheElements(List<CacheElementXml> cacheElements) {
		this.cacheElements = cacheElements;
		if (CollectionUtils.isNotEmpty(this.cacheElements)) {
			this.cacheElementMap = new HashMap<>();
			for (CacheElement cacheElement : cacheElements) {
				this.cacheElementMap.put(cacheElement.getName(), cacheElement);
			}
		}
	}

	//@Override
	/*public List<ClearCache> getClearCaches() {
		return new ArrayList<ClearCache>(this.clearCaches);
	}*/

	public void setClearCaches(List<ClearCacheXml> clearCaches) {
		this.clearCaches = clearCaches;
		if (CollectionUtils.isNotEmpty(this.clearCaches)) {
			this.clearCacheMap = new HashMap<>();
			for (ClearCache clearCache : clearCaches) {
				this.clearCacheMap.put(clearCache.getName(), clearCache);
			}
		}
	}

}
