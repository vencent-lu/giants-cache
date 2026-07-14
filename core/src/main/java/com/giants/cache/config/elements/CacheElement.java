/**
 * 
 */
package com.giants.cache.config.elements;

import java.io.Serializable;
import java.util.List;

/**
 * @author vencent.lu
 *
 */
public interface CacheElement extends Serializable {

	String getName();

	Long getTimeToLive();

	boolean isExclusionMethod(String methodName);
			
	//List<ExclusionMethod> getExclusionMethods();

	boolean isCleanMethod(String methodName);
	
	//List<CleanMethod> getCleanMethods();

}
