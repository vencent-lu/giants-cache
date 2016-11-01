/**
 * 
 */
package com.giants.cache.memcached;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.giants.cache.memcached.config.CacheServer;
import com.giants.cache.memcached.config.MemcachedConfig;
import com.giants.cache.memcached.config.SockPool;
import com.giants.xmlmapping.XmlDataModule;
import com.giants.xmlmapping.XmlMappingData;
import com.giants.xmlmapping.config.exception.XmlMapException;
import com.giants.xmlmapping.exception.XMLParseException;
import com.giants.xmlmapping.exception.XmlDataException;

/**
 * @author vencent.lu
 *
 */
public class MemcachedManager {
	
	private static MemcachedConfig memcachedConfig;
	private static Map<String, MemcachedClient> memcachedClientMap;
	
	public static final void initialize(String memcachedConfigFilePath)
			throws XmlMapException, XmlDataException, XMLParseException {
		XmlMappingData xmlMappingData = new XmlMappingData(
				MemcachedConfig.class);
		xmlMappingData.loadXmls(memcachedConfigFilePath);
		XmlDataModule<MemcachedConfig> xmlDataModule = xmlMappingData
				.getDataModule(MemcachedConfig.class);
		if (xmlDataModule != null && xmlDataModule.isNotEmpty()) {
			memcachedConfig = xmlDataModule.getAll().iterator().next();
			if (CollectionUtils.isNotEmpty(memcachedConfig.getSockPools())) {
				memcachedClientMap = new HashMap<String, MemcachedClient>();
				for (SockPool sockPool : memcachedConfig.getSockPools()) {
					String[] servers = new String[sockPool.getCacheServers().size()];
					Integer[] weights = new Integer[servers.length];
					for (int i = 0; i < servers.length; i++) {
						CacheServer cacheServer = sockPool.getCacheServers().get(i);
						servers[i] = cacheServer.getServiceAddress();
						weights[i] = cacheServer.getWeight();
					}
					SockIOPool pool = SockIOPool.getInstance(sockPool.getPoolName());
					pool.setServers(servers);
					pool.setWeights(weights);
					if (sockPool.getInitConn() != null) {
						pool.setInitConn(sockPool.getInitConn());
					}
					if (sockPool.getMinConn() != null) {
						pool.setMinConn(sockPool.getMinConn());
					}
					if (sockPool.getMaxConn() != null) {
						pool.setMaxConn(sockPool.getMaxConn());
					}
					if (sockPool.getMaxIdle() != null) {
						pool.setMaxIdle(sockPool.getMaxIdle());
					}
					if (sockPool.getMaintSleep() != null) {
						pool.setMaintSleep(sockPool.getMaintSleep());
					}
					if (sockPool.getNagle() != null) {
						pool.setNagle(sockPool.getNagle());
					}
					if (sockPool.getSocketTo() != null) {
						pool.setSocketTO(sockPool.getSocketTo());
					}
					if (sockPool.getSocketConnectTo() != null) {
						pool.setSocketConnectTO(sockPool.getSocketConnectTo());
					}
					if (sockPool.getAliveCheck() != null) {
						pool.setAliveCheck(sockPool.getAliveCheck());
					}
					if (sockPool.getFailback() != null) {
						pool.setFailback(sockPool.getFailback());
					}
					if (sockPool.getFailover() != null) {
						pool.setFailover(sockPool.getFailover());
					}
					if (sockPool.getHashingAlg() != null) {
						pool.setHashingAlg(sockPool.getHashingAlg());
					}					
					pool.initialize();
					memcachedClientMap.put(sockPool.getPoolName(), new MemcachedClient(sockPool.getPoolName()));
				}
			}			
		}
	}
	
	public static final void initialize() throws XmlMapException, XmlDataException,
			XMLParseException {
		if (memcachedConfig == null) {
			initialize("memcached.xml");
		}		
	}
	
	public static final MemcachedClient getMemcachedClient(String poolName) {
		if (memcachedClientMap == null) {
			return null;
		}
		return memcachedClientMap.get(poolName);
	}

}
