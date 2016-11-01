/**
 * 
 */
package com.giants.cache.memcached.config;

import java.io.Serializable;

import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class CacheServer implements Serializable {

	private static final long serialVersionUID = 4996785528986673273L;
	
	@XmlAttribute
	@XmlIdKey
	private String serviceAddress;
	
	@XmlAttribute
	private Integer weight;

	public String getServiceAddress() {
		return serviceAddress;
	}

	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serviceAddress == null) ? 0 : serviceAddress.hashCode());
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
		CacheServer other = (CacheServer) obj;
		if (serviceAddress == null) {
			if (other.serviceAddress != null)
				return false;
		} else if (!serviceAddress.equals(other.serviceAddress))
			return false;
		return true;
	}

	
}
