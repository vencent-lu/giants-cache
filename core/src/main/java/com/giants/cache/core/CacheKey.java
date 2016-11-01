/**
 * 
 */
package com.giants.cache.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vencent.lu
 *
 */
public class CacheKey implements Serializable {

	private static final long serialVersionUID = 6545451722374674783L;
	
	private String cacheModelName;
	private String keyName;
	protected String elementConfName;
	private List<Object> arguments;
	
	public CacheKey() {
		super();
	}

	/**
	 * @param cacheModelName
	 * @param keyName
	 */
	public CacheKey(String cacheModelName,String keyName) {
		super();
		this.cacheModelName = cacheModelName;
		this.keyName = keyName;
		this.elementConfName = keyName;
	}

	/**
	 * @param cacheModelName
	 * @param keyName
	 * @param arguments
	 */
	public CacheKey(String cacheModelName,String keyName, List<Object> arguments) {
		super();
		this.cacheModelName = cacheModelName;
		this.keyName = keyName;
		this.elementConfName = keyName;
		this.arguments = arguments;
	}
	
	/**
	 * @param cacheModelName
	 * @param keyName
	 * @param arguments
	 */
	public CacheKey(String cacheModelName,String keyName, Object... arguments) {
		super();
		this.cacheModelName = cacheModelName;
		this.keyName = keyName;
		this.elementConfName = keyName;
		for (Object arg : arguments) {
			this.addArgument(arg);
		}
	}
		
	public CacheKey addArgument(Object arg) {
		if (this.arguments == null) {
			this.arguments = new ArrayList<Object>();
		}
		this.arguments.add(arg);
		return this;
	}

	public String getKeyName() {
		return this.keyName;
	}
	
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	public List<Object> getArguments() {
		return arguments;
	}

	public void setElementConfName(String elementConfName) {
		this.elementConfName = elementConfName;
	}

	public String getElementConfName() {
		return elementConfName;
	}

	public String getCacheModelName() {
		return cacheModelName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((cacheModelName == null) ? 0 : cacheModelName.hashCode());
		result = prime * result + ((keyName == null) ? 0 : keyName.hashCode());
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
		CacheKey other = (CacheKey) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (cacheModelName == null) {
			if (other.cacheModelName != null)
				return false;
		} else if (!cacheModelName.equals(other.cacheModelName))
			return false;
		if (keyName == null) {
			if (other.keyName != null)
				return false;
		} else if (!keyName.equals(other.keyName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [cacheModelName=" + cacheModelName + ", keyName="
				+ keyName + ", elementConfName=" + elementConfName
				+ ", arguments=" + arguments + "]";
	}
	
}
