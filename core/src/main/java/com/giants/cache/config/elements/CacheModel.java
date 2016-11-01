/**
 * 
 */
package com.giants.cache.config.elements;

import java.io.Serializable;

import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlIdKey;

/**
 * @author vencent.lu
 *
 */
public abstract class CacheModel implements Serializable {

	private static final long serialVersionUID = 7947331452941414059L;
	
	@XmlAttribute
	@XmlIdKey
	protected String name;
	
	@XmlAttribute
	private Boolean defaultCache = false;
	
	@XmlAttribute
	private Long defaultTimeToLive = 300L;
	
	public abstract CacheElement createCacheElement(String name);
	public abstract CacheElement getCacheElement(String elementName);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the defaultCache
	 */
	public Boolean isDefaultCache() {
		return defaultCache;
	}

	/**
	 * @param defaultCache the defaultCache to set
	 */
	public void setDefaultCache(Boolean defaultCache) {
		this.defaultCache = defaultCache;
	}

	/**
	 * @return the defaultTimeToLive
	 */
	public Long getDefaultTimeToLive() {
		return defaultTimeToLive;
	}

	/**
	 * @param defaultTimeToLive the defaultTimeToLive to set
	 */
	public void setDefaultTimeToLive(Long defaultTimeToLive) {
		this.defaultTimeToLive = defaultTimeToLive;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheModel other = (CacheModel) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
