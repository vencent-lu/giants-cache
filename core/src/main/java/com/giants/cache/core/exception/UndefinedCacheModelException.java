/**
 * 
 */
package com.giants.cache.core.exception;

import java.text.MessageFormat;

/**
 * @author vencent.lu
 *
 */
public class UndefinedCacheModelException extends Exception {

	private static final long serialVersionUID = -4324947958604250128L;
	
	private String cacheModelName;

	public UndefinedCacheModelException(String cacheModelName) {
		super(MessageFormat.format("caching module \"{0}\" undefined!", cacheModelName));
		this.cacheModelName = cacheModelName;
	}

	public String getCacheModelName() {
		return cacheModelName;
	}

}
