/**
 * 
 */
package com.giants.cache.core;

import java.util.List;

/**
 * @author vencent.lu
 *
 */
public class MethodCacheKey extends CacheKey {

	private static final long serialVersionUID = -6915099713777611048L;
	
	private String className;
	private String methodName;
	private transient String simpleMethod;
	private transient String simpleMethodName;
	private boolean returnValue = true;
	private Object target; 

	public MethodCacheKey() {
		super();
	}

	/**
	 * @param cacheModelName cacheModelName
	 * @param keyName keyName
	 */
	public MethodCacheKey(String cacheModelName,String keyName) {
		super(cacheModelName,keyName);
		this.initMethod(keyName);
	}

	/**
	 * @param cacheModelName cacheModelName
	 * @param keyName keyName
	 * @param arguments arguments
	 */
	public MethodCacheKey(String cacheModelName, String keyName, List<Object> arguments) {
		super(cacheModelName, keyName, arguments);
		this.initMethod(keyName);
	}
	
	private void initMethod(String keyName) {
		String[] methodSplitArr = keyName.split("\\(");
		this.methodName = methodSplitArr[0];
		String[] keyNameArr = this.methodName.split("\\.");
		this.simpleMethodName = keyNameArr[keyNameArr.length - 1];
		StringBuilder charSequenceSb = new StringBuilder(".")
				.append(this.simpleMethodName);
		this.className = this.methodName.replace(charSequenceSb.toString(), "");
		this.simpleMethod = new StringBuilder(this.simpleMethodName)
				.append("(").append(methodSplitArr[1]).toString();
		this.elementConfName = keyName;
	}
	
	public void setKeyName(String keyName) {
		super.setKeyName(keyName);
		this.initMethod(keyName);
	}
	
	/* (non-Javadoc)
	 * @see com.giants.cache.core.CacheKey#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((target == null) ? 0 : target.hashCode());
		result = prime * result + super.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see com.giants.cache.core.CacheKey#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean superEquals = super.equals(obj);
		if (!superEquals) {
			return false;
		}
		MethodCacheKey other = (MethodCacheKey) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}
	
	public String getSimpleMethod() {
		return simpleMethod;
	}

	public String getSimpleMethodName() {
		return simpleMethodName;
	}

	public boolean isReturnValue() {
		return returnValue;
	}

	public void setReturnValue(boolean returnValue) {
		this.returnValue = returnValue;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

}
