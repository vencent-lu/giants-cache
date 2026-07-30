package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.PurgeIP;
import com.giants.cache.config.elements.PurgeServletCache;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;

import java.util.List;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="purgeServletCache")
public class PurgeServletCacheXml implements PurgeServletCache {

	private static final long serialVersionUID = -2567721542028465084L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
	
	@XmlAttribute
	private String purgeURIPrefix;
	
	@XmlManyElement
	private List<PurgeIPXml> purgeIPs;

	@Override
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
	@Override
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
	@Override
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
	//@Override
	/*public List<PurgeIP> getPurgeIPs() {
		return new ArrayList<PurgeIP>(this.purgeIPs);
	}*/

	/**
	 * @param purgeIPs the purgeIPs to set
	 */
	public void setPurgeIPs(List<PurgeIPXml> purgeIPs) {
		this.purgeIPs = purgeIPs;
	}	

}
