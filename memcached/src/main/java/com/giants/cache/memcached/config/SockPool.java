/**
 * 
 */
package com.giants.cache.memcached.config;

import java.io.Serializable;
import java.util.List;

import com.giants.xmlmapping.annotation.XmlAttribute;
import com.giants.xmlmapping.annotation.XmlEntity;
import com.giants.xmlmapping.annotation.XmlIdKey;
import com.giants.xmlmapping.annotation.XmlManyElement;

/**
 * @author vencent.lu
 *
 */
@XmlEntity
public class SockPool implements Serializable {

	private static final long serialVersionUID = -4716127759125429299L;
	
	@XmlAttribute
	@XmlIdKey
	private String poolName;	
	@XmlManyElement
	private List<CacheServer> cacheServers;
	@XmlAttribute
	private Integer  initConn;//设置开始时每个cache服务器的可用连接数
	@XmlAttribute
	private Integer minConn;//设置每个服务器最少可用连接数
	@XmlAttribute
	private Integer maxConn;//设置每个服务器最大可用连接数
	@XmlAttribute
	private Long maxIdle;//设置可用连接池的最长等待时间
	@XmlAttribute
	private Long maintSleep;//设置连接池维护线程的睡眠时间
	@XmlAttribute
	private Boolean nagle;//设置是否使用Nagle算法，因为我们的通讯数据量通常都比较大（相对TCP控制数据）而且要求响应及时，因此该值需要设置为false（默认是true）
	@XmlAttribute
	private Integer socketTo;//设置socket的读取等待超时值
	@XmlAttribute
	private Integer socketConnectTo;//设置socket的连接等待超时值
	@XmlAttribute
	private Boolean aliveCheck;//设置连接心跳监测开关
	@XmlAttribute
	private Boolean failback;//设置连接失败恢复开关
	@XmlAttribute
	private Boolean failover;//设置容错开关
	@XmlAttribute
	private Integer hashingAlg;//设置hash算法
	
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public List<CacheServer> getCacheServers() {
		return cacheServers;
	}
	public void setCacheServers(List<CacheServer> cacheServers) {
		this.cacheServers = cacheServers;
	}
	public Integer getInitConn() {
		return initConn;
	}
	public void setInitConn(Integer initConn) {
		this.initConn = initConn;
	}
	public Integer getMinConn() {
		return minConn;
	}
	public void setMinConn(Integer minConn) {
		this.minConn = minConn;
	}
	public Integer getMaxConn() {
		return maxConn;
	}
	public void setMaxConn(Integer maxConn) {
		this.maxConn = maxConn;
	}
	public Long getMaxIdle() {
		return maxIdle;
	}
	public void setMaxIdle(Long maxIdle) {
		this.maxIdle = maxIdle;
	}
	public Long getMaintSleep() {
		return maintSleep;
	}
	public void setMaintSleep(Long maintSleep) {
		this.maintSleep = maintSleep;
	}
	public Boolean getNagle() {
		return nagle;
	}
	public void setNagle(Boolean nagle) {
		this.nagle = nagle;
	}
	public Integer getSocketTo() {
		return socketTo;
	}
	public void setSocketTo(Integer socketTo) {
		this.socketTo = socketTo;
	}
	public Integer getSocketConnectTo() {
		return socketConnectTo;
	}
	public void setSocketConnectTo(Integer socketConnectTo) {
		this.socketConnectTo = socketConnectTo;
	}
	public Boolean getAliveCheck() {
		return aliveCheck;
	}
	public void setAliveCheck(Boolean aliveCheck) {
		this.aliveCheck = aliveCheck;
	}
	public Boolean getFailover() {
		return failover;
	}
	public void setFailover(Boolean failover) {
		this.failover = failover;
	}
	public Integer getHashingAlg() {
		return hashingAlg;
	}
	public void setHashingAlg(Integer hashingAlg) {
		this.hashingAlg = hashingAlg;
	}	
	public Boolean getFailback() {
		return failback;
	}
	public void setFailback(Boolean failback) {
		this.failback = failback;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((poolName == null) ? 0 : poolName.hashCode());
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
		SockPool other = (SockPool) obj;
		if (poolName == null) {
			if (other.poolName != null)
				return false;
		} else if (!poolName.equals(other.poolName))
			return false;
		return true;
	}	

}
