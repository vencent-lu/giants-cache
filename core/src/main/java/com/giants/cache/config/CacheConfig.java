/**
 * 
 */
package com.giants.cache.config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.giants.cache.config.elements.CacheModel;
import com.giants.cache.config.elements.MethodCacheModel;
import com.giants.cache.config.elements.ServletCacheModel;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="cacheConfig")
public class CacheConfig implements Serializable {

	private static final long serialVersionUID = -3700148871067922798L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
		
	@XmlManyElement
	private List<MethodCacheModel> methodCacheModels;
	
	private Map<String,MethodCacheModel> methodCacheModelMap;
	
	@XmlManyElement
	private List<ServletCacheModel> servletCacheModels;
	
	private Map<String,ServletCacheModel> servletCacheModelMap;

	public CacheModel getCacheModel(String modelName) {
		CacheModel cacheModel = null;
		if (this.methodCacheModelMap == null) {
			if (this.servletCacheModelMap != null) {
				cacheModel = this.servletCacheModelMap.get(modelName);
			}
		} else {
			cacheModel = this.methodCacheModelMap.get(modelName);
			if (cacheModel == null && this.servletCacheModelMap != null) {
				cacheModel = this.servletCacheModelMap.get(modelName);				
			}
		}
		return cacheModel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MethodCacheModel> getMethodCacheModels() {
		return methodCacheModels;
	}

	public void setMethodCacheModels(List<MethodCacheModel> methodCacheModels) {
		this.methodCacheModels = methodCacheModels;
		if (CollectionUtils.isNotEmpty(this.methodCacheModels)) {
			this.methodCacheModelMap = new HashMap<String, MethodCacheModel>();
			for (MethodCacheModel cacheModel : this.methodCacheModels) {
				this.methodCacheModelMap.put(cacheModel.getName(), cacheModel);
			}
		}
	}

	/**
	 * @return the servletCacheModels
	 */
	public List<ServletCacheModel> getServletCacheModels() {
		return servletCacheModels;
	}

	/**
	 * @param servletCacheModels the servletCacheModels to set
	 */
	public void setServletCacheModels(List<ServletCacheModel> servletCacheModels) {
		this.servletCacheModels = servletCacheModels;
		if (CollectionUtils.isNotEmpty(this.servletCacheModels)) {
			this.servletCacheModelMap = new HashMap<String, ServletCacheModel>();
			for (ServletCacheModel cacheModel : this.servletCacheModels) {
				this.servletCacheModelMap.put(cacheModel.getName(), cacheModel);
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
		CacheConfig other = (CacheConfig) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
