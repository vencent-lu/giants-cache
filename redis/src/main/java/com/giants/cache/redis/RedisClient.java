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
	 * @param key key
	 * @return key对应的值
	 */
	Serializable get(Serializable key);
	
	/**
	 * 设置key对应的值
	 * @param key key
	 * @param value 值
	 */
	void set(Serializable key, Serializable value);
	
	/**
	 * 设置key对应的值
	 * @param key key
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
	 * @param key key
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
	 * @param key key
	 * @param value
	 * @return
	 */
	@Deprecated
	boolean setnx(Serializable key, Serializable value);
	
	Serializable getSet(Serializable key, Serializable value);
	
	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
	 * @param key key
	 * @param seconds 生存时间
	 */
	void expire(Serializable key, int seconds);
	
	/**
	 * 以毫秒为单位返回 key 的剩余生存时间
	 * @param key key
	 * @return 生存时间
	 */
	long pttl(Serializable key);
	
	/**
	 * 设置key对应的值,同时指定key生存时间
	 * @param key key
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
	 * @param keys keys
	 */
	void del(Serializable... keys);
	
	/**
	 * 删除给定的key  字节数组
	 * @param keys keys
	 */
	void del(byte[]... keys);
	
	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。
	 * @param key 哈希表 key
	 * @param fields 一个或多个域
	 * @return List
	 */
	List<Serializable> hmget(Serializable key, Serializable... fields);
	
	/**
	 * 返回哈希表 key 中，给定域的值。
	 * @param key 哈希表 key
	 * @param field 域
	 * @return Serializable
	 */
	Serializable hmget(Serializable key, Serializable field);
	
	/**
	 * 将Map中的所有 域-值 对 设置到哈希表 key 中。
	 * 此命令会覆盖哈希表中已存在的域。
	 * @param key 哈希表 key
	 * @param hash hash表
	 */
	void hmset(Serializable key, Map<?, ?> hash);
	
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
	 * @return Set
	 */
	Set<Serializable> hkeys(Serializable key);
	
	/**
	 * 返回哈希表 key 中所有域的值。
	 * @param key 哈希表 key
	 * @return 所有域的值
	 */
	List<Serializable> hvals(Serializable key);
	
	/**
	 * 返回哈希表 key 中，所有的域和值。
	 * @param key 哈希表 key
	 * @return 所有的域和值
	 */
	Map<Serializable, Serializable> hgetall(Serializable key);
	
	/**
	 * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
	 * @param key 集合Key
	 * @param members 一个或多个 member 元素
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 */
	long sadd(Serializable key, Serializable... members);
	
	/**
	 * 判断 member 元素是否集合 key 的成员。
	 * @param key 集合Key
	 * @param member 元素
	 * @return boolean
	 */
	boolean sismember(Serializable key, Serializable member);
	
	/**
	 * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
	 * @param key 集合Key
	 * @param members 一个或多个 member 元素
	 * @return 被成功移除的成员的数量，不包括被忽略的成员。
	 */
	long srem(Serializable key, Serializable... members);
	
	/**
	 * 返回集合 key 中的所有成员, 不存在的 key 被视为空集合
	 * @param key 集合Key
	 * @return 所有成员
	 */
	Set<Serializable> smembers(Serializable key);
	
	/**
	 * 返回集合 key 中的所有成员(字节数组), 不存在的 key 被视为空集合
	 * @param key 集合Key 字节数组
	 * @return 所有成员(字节数组)
	 */
	Set<byte[]> smembers(byte[] key);
	
	/**
	 * 将一个或多个 Tuple(member 元素及其 score 值)加入到有序集 key 当中。
	 * @param key 有序集合key
	 * @param tuples 添加的有序集合元素
	 * @return long
	 */
	long zadd(Serializable key, Tuple... tuples);
	
	/**
	 * 返回有序集 key 中，成员 member 的 score 值。
	 * @param key 有序集合key
	 * @param member 成员
	 * @return 成员score 值
	 */
	Double zscore(Serializable key, Serializable member);
	
	/**
	 * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
	 * @param key 有序集合key
	 * @param members 一个或多个 member 元素
	 * @return 被成功移除的成员的数量，不包括被忽略的成员。
	 */
	long zrem(Serializable key, Serializable... members);
	
	/**
	 * 返回有序集 key 中， score 值在 range 内的成员的数量。
	 * @param key 有序集合key
	 * @param range score 取值范围
	 * @return score 值在 min 和 max 之间的成员的数量。
	 */
	long zcount(Serializable key, Range range);
	
	/**
	 * 移除有序集 key 中，所有 score 值介于 range 内的成员。
	 * @param key 有序集合key
	 * @param range score 取值范围
	 * @return 被移除成员的数量
	 */
	long zremrangeByScore(Serializable key, Range range);
	
	/**
	 * 返回有序集 key 中，所有 score 值介于 range内 的成员。有序集成员按 score 值递增(从大到小)次序排列。
	 * @param key 有序集合key
	 * @param range score 取值范围
	 * @return 指定区间内的有序集成员的列表。
	 */
	Set<Serializable> zrevrangeByScore(Serializable key, Range range);
	
	/**
	 * 返回有序集 key 中，所有 score 值介于 range 内的成员。有序集成员按 score 值递增(从大到小)次序排列。
	 * 支持分页功能
	 * @param key 有序集合key
	 * @param range score 取值范围
	 * @param offset 起始座标
	 * @param count 返回数量
	 * @return 指定区间内的有序集成员的列表(分页)。
	 */
	Set<Serializable> zrevrangeByScore(Serializable key, Range range, int offset, int count);
	
	/**
	 * 得到一个分布式锁
	 * @param key 分布式锁Key
	 * @param lockTimeOut 锁的过期时间
	 * @return 返回布式锁过期时间戳
	 */
	Lock getLock(Serializable key, int lockTimeOut);
	
	/**
	 * 释放分布式锁
	 * @param lock
	 * @param unReleaseExpireSends 过期时设置过期时间
	 */
	void releaseLock(Lock lock, int unReleaseExpireSends);
	
	
	
	
	
	
	/*
	*//**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * @param key 有序集合key
	 * @param min score最小值 为null表示 -inf(负无穷大)
	 * @param max score最大值 为null表示 +inf(正无穷大)
	 * @return 指定区间内的有序集成员的列表。
	 *//*
	Set<Serializable> zrangeByScore(Serializable key, Double min, Double max);
	
	*//**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * 支持分页功能
	 * @param key 有序集合key
	 * @param min score最小值 为null表示 -inf(负无穷大)
	 * @param max score最大值 为null表示 +inf(正无穷大)
	 * @param offset 起始座标
	 * @param count 返回数量
	 * @return 指定区间内的有序集成员的列表(分页)。
	 *//*
	Set<Serializable> zrangeByScore(Serializable key, Double min, Double max, long offset, long count);
	
	*//**
	 * 返回有序集 key 的基数。集合大小，当 key 不存在时，返回 0 。
	 * @param key 有序集合key
	 * @return 集合大小
	 *//*
	long zcard(Serializable key);
	
	*//**
	 * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
	 * @param key 有序集合key
	 * @param increment 增加值，可以为负数
	 * @param member 成员
	 * @return 增加后的score值
	 *//*
	double zincrby(Serializable key, double increment, Serializable member);
	
	*//**
	 * 计算给定的一个或多个有序集的交集 并将该交集(结果集)储存到 destination 。
	 * @param destKey 目标集合Key
	 * @param keies 需要计算交集的多个集合
	 * @return 保存到 destination 的结果集的基数。
	 *//*
	long zinterstore(Serializable destKey, Serializable... keies);
	
	sdfsdf
	long zinterstore(Serializable destKey, Aggregate aggregate, int[] weights, byte[]... sets);
		
	*//**
	 * 返回有序集 key 中，指定区间内的成员。
	 * @param key 有序集合key
	 * @param begin 开始下标 0 表示第一个 -1 表示最后一个
	 * @param end 结束下标 0 表示第一个 -1 表示最后一个
	 * @return 所有成员
	 *//*
	Set<Serializable> zrange(Serializable key, long begin, long end);
	
	*//**
	 * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
	 * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
	 * @param key 有序集合key
	 * @param member 成员
	 * @return 排名
	 *//*
	long zRank(Serializable key, Serializable member);
	
	*//**
	 * 返回有序集 key 中，指定区间内的成员(包含score分娄)。
	 * @param key 有序集合key
	 * @param begin 开始下标 0 表示第一个 -1 表示最后一个
	 * @param end 结束下标 0 表示第一个 -1 表示最后一个
	 * @return 所有成员(包含score分娄)
	 *//*
	Set<Tuple> zrangeWithScores(Serializable key, long begin, long end);	
		
	*//**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * @param key 有序集合key
	 * @param min score最小值 为null表示 -inf(负无穷大)
	 * @param max score最大值 为null表示 +inf(正无穷大)
	 * @return 指定区间内，带有 score 值的有序集成员的列表。
	 *//*
	Set<Tuple> zrangeByScoreWithScores(Serializable key, Double min, Double max);
	
	*//**
	 * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
	 * 支持分页功能
	 * @param key 有序集合key
	 * @param min score最小值 为null表示 -inf(负无穷大)
	 * @param max score最大值 为null表示 +inf(正无穷大)
	 * @param offset 起始座标
	 * @param count 返回数量
	 * @return 指定区间内，带有 score 值的有序集成员的列表(分页)。
	 *//*
	Set<Tuple> zrangeByScoreWithScores(Serializable key, Double min, Double max, long offset, long count);*/
	
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
		 */
		SET_IF_ABSENT("NX"),
		/**
		 * 如果存在则设置
		 * {@code XX}
		 *
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
