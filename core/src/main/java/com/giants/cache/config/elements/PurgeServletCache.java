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
public interface PurgeServletCache extends Serializable {

	boolean allowPurge(String ip);

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the purgeURIPrefix
	 */
	String getPurgeURIPrefix();

	/**
	 * @return the purgeIPs
	 */
	//List<PurgeIP> getPurgeIPs();

}
