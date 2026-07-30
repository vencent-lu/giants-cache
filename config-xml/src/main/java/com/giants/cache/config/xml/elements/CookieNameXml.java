package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.CookieName;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="cookieName")
public class CookieNameXml implements CookieName {

	private static final long serialVersionUID = 4183999977377426007L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;

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

}
