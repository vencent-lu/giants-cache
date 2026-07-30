package com.giants.cache.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.giants.common.collections.CollectionUtils;
import com.giants.cache.config.CacheConfig;
import com.giants.cache.config.elements.CacheElement;
import com.giants.cache.config.elements.CacheModel;
import com.giants.cache.config.elements.MethodCacheModel;
import com.giants.cache.config.elements.ClearCache;
import com.giants.cache.config.elements.ServletCacheElement;
import com.giants.cache.config.elements.ServletCacheModel;
import com.giants.cache.core.exception.UndefinedCacheModelException;
import com.giants.cache.core.session.GiantsSessionCache;

/**
 * @author vencent.lu
 *
 */
public class GiantsCacheManager {

	private final CacheConfig cacheConfig;
	private final GiantsCache giantsCache;
	
	private static final Map<String, GiantsCacheManager> instanceMap = new HashMap<>();
	private static GiantsSessionCache giantsSessionCache;
	
	public static GiantsCacheManager getInstance(String cacheConfigKey) {
		return instanceMap.get(cacheConfigKey);
	}

	public GiantsCacheManager(GiantsCache giantsCache) {
		this.giantsCache = giantsCache;
		this.cacheConfig = giantsCache.getCacheConfigBuilder().build();
		instanceMap.put(this.giantsCache.getCacheConfigBuilder().getCacheConfigKey(), this);
	}

	public final CacheElement getCacheElementConf(String modelName,
			String elementConfName) {
		if (this.cacheConfig == null) {
			return null;
		}
		CacheModel cacheModel = this.cacheConfig.getCacheModel(modelName);
		if (cacheModel == null) {
			return null;
		}
		CacheElement element = cacheModel.getCacheElement(elementConfName);
		if (element != null) {
			return element;
		}
		if (cacheModel.isDefaultCache()) {
			return cacheModel.createCacheElement(elementConfName);
		} else {
			return null;
		}
	}

	public final ServletCacheElement getServletCacheElementConf(
			String modelName, String URI) {
		if (this.cacheConfig == null) {
			return null;
		}
		CacheModel cacheModel = this.cacheConfig.getCacheModel(modelName);
		if (!(cacheModel instanceof ServletCacheModel)) {
			return null;
		}
		List<ServletCacheElement> servletCacheElements = ((ServletCacheModel) cacheModel)
				.getCacheElements();
		if (CollectionUtils.isNotEmpty(servletCacheElements)) {
			for (ServletCacheElement cacheElement : servletCacheElements) {
				if (cacheElement.getURIPattern().matches(URI)) {
					return cacheElement;
				}
			}
		}
		if (cacheModel.isDefaultCache()) {
			return (ServletCacheElement) cacheModel.createCacheElement(URI);
		} else {
			return null;
		}
	}

	public final ClearCache getClearCache(String modelName,
			String clearCacheName) {
		if (this.cacheConfig == null) {
			return null;
		}
		CacheModel cacheModel = this.cacheConfig.getCacheModel(modelName);
		if (cacheModel == null) {
			return null;
		}
		ClearCache clearCache = ((MethodCacheModel) cacheModel)
				.getClearCache(clearCacheName);
		if (clearCache == null) {
			return null;
		}
		return clearCache;
	}

	public final Element getCacheElement(CacheKey key)
			throws UndefinedCacheModelException {
		return this.giantsCache.get(key);
	}

	public final void putCacheElement(Element cacheElement)
			throws UndefinedCacheModelException {
		this.giantsCache.put(cacheElement);
	}

	public final void removeCacheElement(Element cacheElement)
			throws UndefinedCacheModelException {
		this.giantsCache.remove(cacheElement);
	}

	public final void removeCacheElement(String cacheModelName,
			String elementConfName) throws UndefinedCacheModelException {
		this.giantsCache.removeAll(cacheModelName, elementConfName);
	}

	/**
	 * @return the cacheConfig
	 */
	public final CacheConfig getCacheConfig() {
		return this.cacheConfig;
	}

	/**
	 * @return the giantsCache
	 */
	public GiantsCache getGiantsCache() {
		return this.giantsCache;
	}

	/**
	 * @return the giantsSessionCache
	 */
	public static GiantsSessionCache getGiantsSessionCache() {
		return giantsSessionCache;
	}

	/**
	 * @param giantsSessionCache the giantsSessionCache to set
	 */
	public static void setGiantsSessionCache(GiantsSessionCache giantsSessionCache) {
		GiantsCacheManager.giantsSessionCache = giantsSessionCache;
	}

}
