/**
 * 
 */
package com.giants.cache.config.elements;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class CacheElement implements Serializable {

	private static final long serialVersionUID = 2636828796472931431L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
	
	@XmlAttribute
	private Long timeToLive = 300L;
	
	@XmlManyElement
	private List<ExclusionMethod> exclusionMethods;
	
	private Map<String, ExclusionMethod> exclusionMethodMap;
	
	@XmlManyElement
	public List<CleanMethod> cleanMethods;
	
	private Map<String, CleanMethod> cleanMethodMap;
	
	/**
	 * 
	 */
	public CacheElement() {
		super();
	}

	/**
	 * @param name
	 * @param timeToLive
	 */
	public CacheElement(String name, Long timeToLive) {
		super();
		this.name = name;
		this.timeToLive = timeToLive;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name.replaceAll("\\s+", "");
	}
	
	public Long getTimeToLive() {
		return timeToLive;
	}
	
	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}
	
	public boolean isExclusionMethod(String methodName) {
		if (this.exclusionMethodMap == null) {
			return false;
		}
		return this.exclusionMethodMap.get(methodName) != null;
	}
			
	public List<ExclusionMethod> getExclusionMethods() {
		return exclusionMethods;
	}

	public void setExclusionMethods(List<ExclusionMethod> exclusionMethods) {
		this.exclusionMethods = exclusionMethods;
		if (CollectionUtils.isNotEmpty(this.exclusionMethods)) {
			this.exclusionMethodMap = new HashMap<String, ExclusionMethod>();
			for (ExclusionMethod exclusionMethod : exclusionMethods) {
				this.exclusionMethodMap.put(exclusionMethod.getName(), exclusionMethod);
			}
		}
	}
	
	public boolean isCleanMethod(String methodName) {
		if (this.cleanMethodMap == null) {
			return false;
		}
		return this.cleanMethodMap.get(methodName) != null;
	}
	
	public List<CleanMethod> getCleanMethods() {
		return cleanMethods;
	}

	public void setCleanMethods(List<CleanMethod> cleanMethods) {
		this.cleanMethods = cleanMethods;
		if (CollectionUtils.isNotEmpty(this.cleanMethods)) {
			this.cleanMethodMap = new HashMap<String, CleanMethod>();
			for (CleanMethod cleanMethod : cleanMethods) {
				this.cleanMethodMap.put(cleanMethod.getName(), cleanMethod);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheElement other = (CacheElement) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
