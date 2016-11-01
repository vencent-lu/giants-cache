/**
 * 
 */
package com.giants.cache.config.elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.giants.xmlmapping.annotation.XmlElement;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class ServletCacheModel extends CacheModel {

	private static final long serialVersionUID = -8900712028246615243L;
	
	@XmlElement
	private PurgeServletCache purgeServletCache;
	
	@XmlManyElement
	private List<ServletCacheElement> cacheElements;
	
	private Map<String,ServletCacheElement> cacheElementMap;
	
	/* (non-Javadoc)
	 * @see com.giants.cache.config.elements.CacheModel#createCacheElement(java.lang.String)
	 */
	@Override
	public CacheElement createCacheElement(String name) {
		return new ServletCacheElement(name, this.getDefaultTimeToLive());
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

	/**
	 * @return the cacheElements
	 */
	public List<ServletCacheElement> getCacheElements() {
		return cacheElements;
	}

	/**
	 * @param cacheElements the cacheElements to set
	 */
	public void setCacheElements(List<ServletCacheElement> cacheElements) {
		this.cacheElements = cacheElements;
		if (CollectionUtils.isNotEmpty(this.cacheElements)) {
			this.cacheElementMap = new HashMap<String,ServletCacheElement>();
			for (ServletCacheElement cacheElement : cacheElements) {
				this.cacheElementMap.put(cacheElement.getName(), cacheElement);
			}
		}
	}

	/**
	 * @return the purgeServletCache
	 */
	public PurgeServletCache getPurgeServletCache() {
		return purgeServletCache;
	}

	/**
	 * @param purgeServletCache the purgeServletCache to set
	 */
	public void setPurgeServletCache(PurgeServletCache purgeServletCache) {
		this.purgeServletCache = purgeServletCache;
	}
	
}
