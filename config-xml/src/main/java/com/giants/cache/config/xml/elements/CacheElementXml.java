package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.CleanMethod;
import com.giants.cache.config.elements.ExclusionMethod;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="cacheElement")
public class CacheElementXml implements CacheElement {

	private static final long serialVersionUID = 2636828796472931431L;

	@XmlAttribute
	@XmlIdKey
	private String name;

	@XmlAttribute
	private Long timeToLive = 300L;

	@XmlManyElement
	private List<ExclusionMethodXml> exclusionMethods;

	private Map<String, ExclusionMethod> exclusionMethodMap;

	@XmlManyElement
	public List<CleanMethodXml> cleanMethods;

	private Map<String, CleanMethod> cleanMethodMap;

	/**
	 *
	 */
	public CacheElementXml() {
		super();
	}

	/**
	 * @param name name
	 * @param timeToLive timeToLive
	 */
	public CacheElementXml(String name, Long timeToLive) {
		super();
		this.name = name;
		this.timeToLive = timeToLive;
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name.replaceAll("\\s+", "");
	}

	@Override
	public Long getTimeToLive() {
		return timeToLive;
	}
	
	public void setTimeToLive(Long timeToLive) {
		this.timeToLive = timeToLive;
	}

	@Override
	public boolean isExclusionMethod(String methodName) {
		if (this.exclusionMethodMap == null) {
			return false;
		}
		return this.exclusionMethodMap.get(methodName) != null;
	}

	//@Override
	/*public List<ExclusionMethod> getExclusionMethods() {
		return new ArrayList<ExclusionMethod>(this.exclusionMethods);
	}*/

	public void setExclusionMethods(List<ExclusionMethodXml> exclusionMethods) {
		this.exclusionMethods = exclusionMethods;
		if (CollectionUtils.isNotEmpty(this.exclusionMethods)) {
			this.exclusionMethodMap = new HashMap<>();
			for (ExclusionMethod exclusionMethod : exclusionMethods) {
				this.exclusionMethodMap.put(exclusionMethod.getName(), exclusionMethod);
			}
		}
	}

	@Override
	public boolean isCleanMethod(String methodName) {
		if (this.cleanMethodMap == null) {
			return false;
		}
		return this.cleanMethodMap.get(methodName) != null;
	}

	//@Override
	/*public List<CleanMethod> getCleanMethods() {
		return new ArrayList<CleanMethod>(this.cleanMethods);
	}*/

	public void setCleanMethods(List<CleanMethodXml> cleanMethods) {
		this.cleanMethods = cleanMethods;
		if (CollectionUtils.isNotEmpty(this.cleanMethods)) {
			this.cleanMethodMap = new HashMap<>();
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
		CacheElementXml other = (CacheElementXml) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
