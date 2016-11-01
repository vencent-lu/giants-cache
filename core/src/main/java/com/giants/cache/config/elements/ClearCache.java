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
public class ClearCache implements Serializable {

	private static final long serialVersionUID = -1368529301795908356L;
	
	@XmlAttribute
	@XmlIdKey
	private String name;
	
	@XmlManyElement
	private List<ClearElement> clearElements;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<ClearElement> getClearElements() {
		return clearElements;
	}
	
	public void setClearElements(List<ClearElement> clearElements) {
		this.clearElements = clearElements;
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
		ClearCache other = (ClearCache) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	

}
