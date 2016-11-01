/**
 * 
 */
package com.giants.cache.ehcache.impl;

import java.io.Serializable;

import com.giants.cache.common.CacheConstants;
import com.giants.cache.core.CacheKey;
import com.giants.cache.core.GiantsCache;
import com.giants.cache.core.GiantsCacheManager;
import com.giants.cache.core.exception.UndefinedCacheModelException;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * @author vencent.lu
 *
 */
public class EhCacheEventListener implements CacheEventListener {
	
	private String cacheConfigFilePath = CacheConstants.DEFAULT_CONFIG_FILE_PATH;
	private GiantsCacheManager giantsCacheManager;
	
	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementRemoved(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	@Override
	public void notifyElementRemoved(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementPut(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	@Override
	public void notifyElementPut(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementUpdated(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	@Override
	public void notifyElementUpdated(Ehcache cache, Element element)
			throws CacheException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementExpired(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	@Override
	public void notifyElementExpired(Ehcache cache, Element element) {
		Serializable key = (Serializable)element.getObjectKey();
		if (key instanceof CacheKey) {
			try {
				this.getGiantsEhcache().removeCacheKey((CacheKey)key);
			} catch (UndefinedCacheModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyElementEvicted(net.sf.ehcache.Ehcache, net.sf.ehcache.Element)
	 */
	@Override
	public void notifyElementEvicted(Ehcache cache, Element element) {
		Serializable key = (Serializable)element.getObjectKey();
		if (key instanceof CacheKey) {
			try {
				this.getGiantsEhcache().removeCacheKey((CacheKey)key);
			} catch (UndefinedCacheModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#notifyRemoveAll(net.sf.ehcache.Ehcache)
	 */
	@Override
	public void notifyRemoveAll(Ehcache cache) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see net.sf.ehcache.event.CacheEventListener#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	private GiantsEhcacheImpl getGiantsEhcache() {
		if (this.giantsCacheManager == null) {
			this.giantsCacheManager = GiantsCacheManager
					.getInstance(this.cacheConfigFilePath);
		}
		GiantsCache giantsCache = this.giantsCacheManager.getGiantsCache();
		if (giantsCache instanceof GiantsEhcacheImpl) {
			return (GiantsEhcacheImpl) giantsCache;
		} else {
			return null;
		}
	}

	public void setCacheConfigFilePath(String cacheConfigFilePath) {
		this.cacheConfigFilePath = cacheConfigFilePath;
	}

}
