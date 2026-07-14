package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.PurgeServletCache;
import com.giants.cache.config.elements.ServletCacheElement;
import com.giants.cache.config.elements.ServletCacheModel;
import com.giants.xmlmapping.annotation.XmlElement;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlManyElement;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="servletCacheModel")
public class ServletCacheModelXml extends CacheModelXml implements ServletCacheModel {

	private static final long serialVersionUID = -8900712028246615243L;
	
	@XmlElement
	private PurgeServletCacheXml purgeServletCache;
	
	@XmlManyElement
	private List<ServletCacheElementXml> cacheElements;
	
	private Map<String, ServletCacheElement> cacheElementMap;

	@Override
	public CacheElement createCacheElement(String name) {
		return new ServletCacheElementXml(name, this.getDefaultTimeToLive());
	}

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
	@Override
	public List<ServletCacheElement> getCacheElements() {
		return new ArrayList<ServletCacheElement>(this.cacheElements);
	}

	/**
	 * @param cacheElements the cacheElements to set
	 */
	public void setCacheElements(List<ServletCacheElementXml> cacheElements) {
		this.cacheElements = cacheElements;
		if (CollectionUtils.isNotEmpty(this.cacheElements)) {
			this.cacheElementMap = new HashMap<>();
			for (ServletCacheElement cacheElement : cacheElements) {
				this.cacheElementMap.put(cacheElement.getName(), cacheElement);
			}
		}
	}

	/**
	 * @return the purgeServletCache
	 */
	@Override
	public PurgeServletCache getPurgeServletCache() {
		return purgeServletCache;
	}

	/**
	 * @param purgeServletCache the purgeServletCache to set
	 */
	public void setPurgeServletCache(PurgeServletCacheXml purgeServletCache) {
		this.purgeServletCache = purgeServletCache;
	}
	
}
