/**
 * 
 */
package com.giants.cache.config.elements;

import com.giants.common.regex.Pattern;

/**
 * @author vencent.lu
 *
 */
public interface ServletCacheElement extends CacheElement {

	boolean isAllowAccordingParam(String paramName);
	
	boolean isAllowCookieName(String cookieName);
	
	Pattern getURIPattern();

	/**
	 * @return the regex
	 */
	//String getRegex();

	boolean isCookie();

	/**
	 * @return the queryParam
	 */
	boolean isQueryParam();

}
