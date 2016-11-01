/**
 * 
 */
package com.giants.cache.config.elements;

import java.io.Serializable;
import java.util.List;

import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class PurgeServletCache implements Serializable {

	private static final long serialVersionUID = -2567721542028465084L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
	
	@XmlAttribute
	private String purgeURIPrefix;
	
	@XmlManyElement
	private List<PurgeIP> purgeIPs;
	
	public boolean allowPurge(String ip) {
		if (this.purgeIPs == null) {
			return false;
		}
		for (PurgeIP purgeIP : this.purgeIPs) {
			if (purgeIP.getValue().equals(ip)) {
				return true;
			}
		}
		return false;
	}

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
	 * @return the purgeURIPrefix
	 */
	public String getPurgeURIPrefix() {
		return purgeURIPrefix;
	}

	/**
	 * @param purgeURIPrefix the purgeURIPrefix to set
	 */
	public void setPurgeURIPrefix(String purgeURIPrefix) {
		this.purgeURIPrefix = purgeURIPrefix;
	}

	/**
	 * @return the purgeIPs
	 */
	public List<PurgeIP> getPurgeIPs() {
		return purgeIPs;
	}

	/**
	 * @param purgeIPs the purgeIPs to set
	 */
	public void setPurgeIPs(List<PurgeIP> purgeIPs) {
		this.purgeIPs = purgeIPs;
	}	

}
