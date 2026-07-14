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
public interface ClearCache extends Serializable {

	String getName();

	List<ClearElement> getClearElements();

}
