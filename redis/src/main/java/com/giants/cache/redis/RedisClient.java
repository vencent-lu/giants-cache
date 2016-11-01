/**
 * 
 */
package com.giants.cache.redis;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis 统一处理接口包含序列化与反序列化
 * @author vencent.lu
 *
 */
public interface RedisClient {
	
	/**
	 * 返回key对应的值
	 * @param key 
	 * @return key对应的值
	 */
	Serializable get(Serializable key);
	
	/**
	 * 设置key对应的值
	 * @param key
	 * @param value 值
	 */
	void set(Serializable key, Serializable value);
	
	/**
	 * 设置key对应的值
	 * @param key
	 * @param value 值
	 * @param option SET_IF_ABSENT | SET_IF_PRESENT,SET_IF_ABSENT -如果不存在则设置。SET_IF_PRESENT,如果存在则设置。
	 * @param expirationUnit SECONDS|MILLISECONDS, 过期时间单位: SECONDS = seconds; MILLISECONDS = milliseconds
	 * @param time 过期时间
	 * @return 是否设置成功
	 */
	boolean set(Serializable key, Serializable value, SetOption option, ExpirationUnit expirationUnit,
			long time);
	
	/**
	 * 设置key对应的值
	 * @param key
	 * @param value 值
	 * @param option SET_IF_ABSENT | SET_IF_PRESENT,SET_IF_ABSENT -如果不存在则设置。SET_IF_PRESENT,如果存在则设置。
	 * @param expirationUnit SECONDS|MILLISECONDS, 过期时间单位: SECONDS = seconds; MILLISECONDS = milliseconds
	 * @param time 过期时间
	 * @return 是否设置成功
	 */
	boolean set(Serializable key, Serializable value, SetOption option, ExpirationUnit expirationUnit,
			int time);
	
	/**
	 * 设置key对应的值 如果不存在，则 SET
	 * 不建议使用，如果需要同时设置过期时间不能保证原子性
	 * @param key
	 * @param value
	 * @return
	 */
	@Deprecated
	boolean setnx(Serializable key, Serializable value);
	
	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * @param key
	 * @param seconds 生存时间
	 */
	void expire(Serializable key, int seconds);
	
	/**
	 * 以毫秒为单位返回 key 的剩余生存时间
	 * @param key
	 * @return 生存时间
	 */
	long pttl(Serializable key);
	
	/**
	 * 设置key对应的值,同时指定key生存时间
	 * @param key
	 * @param value 值
	 * @param seconds 生存时间
	 */
	void set(Serializable key, Serializable value, int seconds);
	
	/**
	 * 查找所有符合给定模式 pattern 的 key
	 * @param pattern 模式字节数组
	 * @return 符合给定模式 pattern 的 key 字节数组
	 */
	Set<byte[]> keys(byte[] pattern);
	
	/**
	 * 查找所有符合给定模式 pattern 的 key
	 * @param pattern 模式
	 * @return 符合给定模式 pattern 的 key
	 */
	Set<String> keys(String pattern);
	
	/**
	 * 删除给定的一个或多个 key 
	 * @param keys
	 */
	void del(Serializable... keys);
	
	/**
	 * 删除给定的key  字节数组
	 * @param key
	 */
	void del(byte[] key);
	
	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * @param key 哈希表 key
	 * @param fields 一个或多个域
	 * @return
	 */
	List<?> hmget(Serializable key, Serializable... fields);
	
	/**
	 * 返回哈希表 key 中，给定域的值。
	 * @param key 哈希表 key
	 * @param field 域
	 * @return
	 */
	Serializable hmget(Serializable key, Serializable field);
	
	/**
	 * 将Map中的所有 域-值 对 设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * @param key 哈希表 key
	 * @param hash hash表
	 */
	void hmset(Serializable key, Map<?,?> hash);
	
	/**
	 * 将域-值 对 设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * @param key 哈希表 key
	 * @param field 域
	 * @param value 值
	 */
	void hmset(Serializable key, Serializable field, Serializable value);
	
	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * @param key 哈希表 key
	 * @param fields 一个或多个域
	 */
	void hdel(Serializable key, Serializable... fields);
	
	/**
	 * 返回哈希表 key 中的所有域。
	 * @param key 哈希表 key
	 * @return
	 */
	Set<?> hkeys(Serializable key);
	
	/**
	 * 返回哈希表 key 中所有域的值。
	 * @param key 哈希表 key
	 * @return 所有域的值
	 */
	List<?> hvals(Serializable key);
	
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * @param key 集合Key
	 * @param members 一个或多个 member 元素
	 */
	void sadd(Serializable key, Serializable... members);
	
	/**
	 * 判断 member 元素是否集合 key 的成员。
	 * @param key 集合Key
	 * @param member 元素
	 * @return
	 */
	boolean sismember(Serializable key, Serializable member);
	
	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * @param key 集合Key
	 * @param members 一个或多个 member 元素
	 */
	void srem(Serializable key, Serializable... members);
	
	/**
	 * 返回集合 key 中的所有成员, 不存在的 key 被视为空集合
	 * @param key 集合Key
	 * @return 所有成员
	 */
	Set<?> smembers(Serializable key);
	
	/**
	 * 返回集合 key 中的所有成员(字节数组), 不存在的 key 被视为空集合
	 * @param key 集合Key 字节数组
	 * @return 所有成员(字节数组)
	 */
	Set<byte[]> smembers(byte[] key);
	
	/**
	 * 将对象序列化为 字节数组
	 * @param obj 对象
	 * @return 字节数组
	 */
	byte[] serialization(Serializable obj);
	
	/**
	 * 将字节数组 反序列化为 JAVA对象
	 * @param bytes 字节数组
	 * @return 对象
	 */
	Serializable deserialization(byte[] bytes);
	
	public enum SetOption {		
		/**
		 * 如果不存在则设置
		 * {@code NX}
		 * 
		 * @return
		 */
		SET_IF_ABSENT("NX"),
		/**
		 * 如果存在则设置
		 * {@code XX}
		 * 
		 * @return
		 */
		SET_IF_PRESENT("XX");
		
		private String nxxx;

		private SetOption(String nxxx) {
			this.nxxx = nxxx;
		}

		public String getNxxx() {
			return nxxx;
		}		
	}
	
	public enum ExpirationUnit {
		/**
		 * 秒
		 * {@code EX}
		 */
		SECONDS("EX"),
		/**
		 * 毫秒
		 * {@code PX}
		 */
		MILLISECONDS("PX");
		
		private String unit;

		private ExpirationUnit(String unit) {
			this.unit = unit;
		}

		public String getUnit() {
			return unit;
		}		
	}

}
