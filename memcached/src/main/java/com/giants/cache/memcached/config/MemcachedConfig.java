/**
 * 
 */
package com.giants.cache.memcached.config;

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
public class MemcachedConfig implements Serializable {

	private static final long serialVersionUID = 227427487168348169L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
	@XmlManyElement
	private List<SockPool> sockPools;
	
	private Map<String,SockPool> sockPoolMap;
	
	public SockPool getSockPool(String poolName) {
		if (this.sockPoolMap == null){
			return null;
		}
		return this.sockPoolMap.get(poolName);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<SockPool> getSockPools() {
		return sockPools;
	}
	
	public void setSockPools(List<SockPool> sockPools) {
		this.sockPools = sockPools;
		if (CollectionUtils.isNotEmpty(this.sockPools)) {
			this.sockPoolMap = new HashMap<String,SockPool>();
			for (SockPool sockPool : this.sockPools) {
				this.sockPoolMap.put(sockPool.getPoolName(), sockPool);
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
		MemcachedConfig other = (MemcachedConfig) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}
