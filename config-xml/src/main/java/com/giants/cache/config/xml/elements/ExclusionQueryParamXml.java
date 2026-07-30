package com.giants.cache.config.xml.elements;

import com.giants.cache.config.elements.ExclusionQueryParam;
import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;

/**
 * @author vencent.lu
 *
 */
@XmlEntity(name="exclusionQueryParam")
public class ExclusionQueryParamXml implements ExclusionQueryParam {

	private static final long serialVersionUID = -5110395691649866297L;
	
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
