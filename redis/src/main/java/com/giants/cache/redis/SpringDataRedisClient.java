/**
 * 
 */
package com.giants.cache.redis;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * SpringDataRedisClient
 * Spring data redis upgrading to 2.1
 * 2021/4/12 11:02
 * @author vencent-lu
 * @since 1.1
 */
public class SpringDataRedisClient extends AbstractRedisClient {

	private RedisTemplate<byte[], byte[]> redisTemplate;

	@Override
	public Serializable get(Serializable key) {
		if (key != null) {
			return this.deserialization(this.redisTemplate.opsForValue().get(this.serializationKey(key)));
		}
		return null;
	}

	@Override
	public void set(Serializable key, Serializable value) {
		if (key != null && value != null) {
			this.redisTemplate.opsForValue().set(this.serializationKey(key), this.serialization(value));
		}
	}

	@Override
	public boolean set(Serializable key, Serializable value, SetOption option, ExpirationUnit expirationUnit,
					   long time) {
		if (key != null && value != null && option != null
				&& expirationUnit != null) {
			ValueOperations<byte[], byte[]> valueOperations = this.redisTemplate.opsForValue();
			TimeUnit timeUnit = expirationUnit == ExpirationUnit.SECONDS ? TimeUnit.SECONDS : TimeUnit.MILLISECONDS;
			if (SetOption.SET_IF_ABSENT == option) {
				return valueOperations.setIfAbsent(this.serializationKey(key), this.serialization(value), time, timeUnit);
			} else {
				return valueOperations.setIfPresent(this.serializationKey(key), this.serialization(value), time, timeUnit);
			}
		}
		return false;
	}

	@Override
	public boolean set(Serializable key, Serializable value, SetOption option, ExpirationUnit expirationUnit,
					   int time) {
		return this.set(key, value, option, expirationUnit, (long)time);
	}

	@Override
	public boolean setnx(Serializable key, Serializable value) {
		if (key != null && value != null) {
			return this.redisTemplate.opsForValue().setIfAbsent(this.serializationKey(key), this.serialization(value));
		}
		return false;
	}

	@Override
	public Serializable getSet(Serializable key, Serializable value) {
		if (key != null && value != null) {
			return this.deserialization(this.redisTemplate.opsForValue().getAndSet(this.serializationKey(key),
					this.serialization(value)));
		}
		return null;
	}

	@Override
	public void expire(Serializable key, int seconds) {
		if (key != null) {
			this.redisTemplate.expire(this.serializationKey(key), seconds, TimeUnit.SECONDS);
		}
	}

	@Override
	public long pttl(Serializable key) {
		if (key != null) {
			return this.redisTemplate.getExpire(this.serializationKey(key), TimeUnit.MILLISECONDS);
		}
		return 0;
	}

	@Override
	public void set(Serializable key, Serializable value, int seconds) {
		if (key != null && value != null) {
			this.redisTemplate.opsForValue().set(this.serializationKey(key), this.serialization(value), seconds,
					TimeUnit.SECONDS);
		}
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		if (pattern != null && pattern.length > 0) {
			return this.redisTemplate.keys(pattern);
		}
		return null;
	}

	@Override
	public Set<String> keys(String pattern) {
		if (StringUtils.isNotEmpty(pattern)) {
			Set<byte[]> keyByteSet = this.redisTemplate.keys(this.serializationKey(pattern));
			if (CollectionUtils.isNotEmpty(keyByteSet)) {
				Set<String> keyStringSet = new HashSet<String>();
				for (byte[] byteKey : keyByteSet) {
					keyStringSet.add(new String(byteKey));
				}
				return keyStringSet;
			}
		}
		return null;
	}

	@Override
	public void del(Serializable... keys) {
		keys = this.conversionSerializableArray(keys);
		if (keys != null && keys.length > 0) {
			byte[][] keyBytes = new byte[keys.length][];
			for (int i = 0; i < keys.length; i++) {
				keyBytes[i] = this.serializationKey(keys[i]);
			}
			this.redisTemplate.delete(Arrays.asList(keyBytes));
		}
	}

	@Override
	public void del(byte[]... keys) {
		if (keys != null && keys.length >0) {
			this.redisTemplate.delete(Arrays.asList(keys));
		}
	}

	@Override
	public List<Serializable> hmget(Serializable key, Serializable... fields) {
		fields = this.conversionSerializableArray(fields);
		if (key != null && fields != null && fields.length > 0){
			byte[] keyByte = this.serializationKey(key);
			byte[][] fieldBytesArray = new byte[fields.length][];
			for (int i=0; i<fields.length; i++) {
				fieldBytesArray[i] = this.serialization(fields[i]);
			}
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			List<byte[]> valueBytesArr = hashOperations.multiGet(keyByte, Arrays.asList(fieldBytesArray));
			if (CollectionUtils.isNotEmpty(valueBytesArr)) {
				List<Serializable> values = new ArrayList<Serializable>();
				for(byte[] valueBytes : valueBytesArr) {
					if (valueBytes == null || valueBytes.length == 0){
						values.add(null);
					} else {
						values.add(this.deserialization(valueBytes));
					}
				}
				return values;
			}

		}
		return null;
	}

	@Override
	public Serializable hmget(Serializable key, Serializable field) {
		if (key != null && field != null) {
			List<Serializable> valueList = this.hmget(key, new Serializable[]{field});
			if (CollectionUtils.isNotEmpty(valueList)) {
				return valueList.get(0);
			}
		}
		return null;
	}

	@Override
	public void hmset(Serializable key, Map<?, ?> hash) {
		if (key != null && hash != null && !hash.isEmpty()) {
			final byte[] keyByte = this.serializationKey(key);
			final Map<byte[],byte[]> bytesHash = new HashMap<byte[],byte[]>();
			Iterator<?> it = hash.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("unchecked")
				Map.Entry<Serializable, Serializable> entry = (Map.Entry<Serializable, Serializable>)it.next();
				bytesHash.put(this.serialization(entry.getKey()), this.serialization(entry.getValue()));
			}
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			hashOperations.putAll(keyByte, bytesHash);
		}
	}

	@Override
	public void hmset(Serializable key, Serializable field, Serializable value) {
		if (key != null && field != null && value != null) {
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			hashOperations.put(this.serializationKey(key), this.serialization(field),
					this.serialization(value));
		}
	}

	@Override
	public void hdel(Serializable key, Serializable... fields) {
		fields = this.conversionSerializableArray(fields);
		if (key != null && fields != null && fields.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] fieldBytesArray = new byte[fields.length][];
			for (int i=0; i<fields.length; i++) {
				fieldBytesArray[i] = this.serialization(fields[i]);
			}
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			hashOperations.delete(keyByte, fieldBytesArray);
		}
	}

	@Override
	public Set<Serializable> hkeys(Serializable key) {
		if (key != null) {
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			Set<byte[]> bytesKeys = hashOperations.keys(this.serializationKey(key));
			if (CollectionUtils.isNotEmpty(bytesKeys)) {
				Set<Serializable> keys = new HashSet<Serializable>();
				Iterator<byte[]> it = bytesKeys.iterator();
				while (it.hasNext()) {
					keys.add(this.deserialization(it.next()));
				}
				return keys;
			}
		}
		return null;
	}

	@Override
	public List<Serializable> hvals(Serializable key) {
		if (key != null) {
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			List<byte[]> bytesValues = hashOperations.values(this.serializationKey(key));
			if (CollectionUtils.isNotEmpty(bytesValues)) {
				List<Serializable> values = new ArrayList<Serializable>();
				for (byte[] bytesValue : bytesValues) {
					values.add(this.deserialization(bytesValue));
				}
				return values;
			}
		}
		return null;
	}

	@Override
	public Map<Serializable, Serializable> hgetall(Serializable key) {
		if (key != null) {
			HashOperations<byte[], byte[], byte[]> hashOperations = this.redisTemplate.opsForHash();
			Map<byte[], byte[]> entryByteMap = hashOperations.entries(this.serializationKey(key));
			if (MapUtils.isNotEmpty(entryByteMap)) {
				Map<Serializable, Serializable> entryMap = new HashMap<Serializable, Serializable>();
				for (Map.Entry<byte[], byte[]> entry : entryByteMap.entrySet()) {
					entryMap.put(this.deserialization(entry.getKey()), this.deserialization(entry.getValue()));
				}
				return entryMap;
			}
		}
		return null;
	}

	@Override
	public long sadd(Serializable key, Serializable... members) {
		members = this.conversionSerializableArray(members);
		if (key != null && members != null && members.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			return this.redisTemplate.opsForSet().add(keyByte, objBytes);
		}
		return 0;
	}

	@Override
	public boolean sismember(Serializable key, Serializable member) {
		if (key != null && member != null) {
			return this.redisTemplate.opsForSet().isMember(this.serializationKey(key), this.serialization(member));
		}
		return false;
	}

	@Override
	public long srem(Serializable key, Serializable... members) {
		members = this.conversionSerializableArray(members);
		if (key != null && members != null && members.length > 0) {
			byte[] keyByte = this.serializationKey(key);
			byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			return this.redisTemplate.opsForSet().remove(keyByte, objBytes);
		}
		return 0;
	}

	@Override
	public Set<Serializable> smembers(Serializable key) {
		if (key != null) {
			Set<byte[]> bytesKeys = this.redisTemplate.opsForSet().members(this.serializationKey(key));
			Set<Serializable> keys = new HashSet<Serializable>();
			Iterator<byte[]> it = bytesKeys.iterator();
			while (it.hasNext()) {
				keys.add(this.deserialization(it.next()));
			}
			return keys;
		}
		return null;
	}

	@Override
	public Set<byte[]> smembers(byte[] key) {
		if (key != null) {
			return this.redisTemplate.opsForSet().members(key);
		}
		return null;
	}

	@Override
	public long zadd(Serializable key, Tuple... tuples) {
		if (key != null && ArrayUtils.isNotEmpty(tuples)) {
			byte[] keyByte = this.serializationKey(key);
			Set<ZSetOperations.TypedTuple<byte[]>> tupleSet = new HashSet<ZSetOperations.TypedTuple<byte[]>>();
			for (Tuple tuple : tuples) {
				tupleSet.add(new DefaultTypedTuple<byte[]>(this.serialization(tuple.getMember()),
						tuple.getScore().doubleValue()));
			}
			return this.redisTemplate.opsForZSet().add(keyByte, tupleSet);
		}
		return 0;
	}

	@Override
	public Double zscore(Serializable key, Serializable member) {
		if (key != null && member != null) {
			return this.redisTemplate.opsForZSet().score(this.serializationKey(key), this.serialization(member));
		}
		return null;
	}

	@Override
	public long zrem(Serializable key, Serializable... members) {
		members = this.conversionSerializableArray(members);
		if (key != null && members != null && members.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			return this.redisTemplate.opsForZSet().remove(keyByte, objBytes);
		}
		return 0;
	}

	private RedisZSetCommands.Range conversionRange(Range range){
		RedisZSetCommands.Range dataRange = RedisZSetCommands.Range.range();
		if (range.getMin()!=null && range.getMin().getValue() != null) {
			if (range.getMin().isIncluding()) {
				dataRange.gte(range.getMin().getValue());
			} else {
				dataRange.gt(range.getMin().getValue());
			}
		}
		if (range.getMax()!=null && range.getMax().getValue() != null) {
			if (range.getMax().isIncluding()) {
				dataRange.lte(range.getMax().getValue());
			} else {
				dataRange.lt(range.getMax().getValue());
			}
		}
		return dataRange;
	}

	@Override
	public long zcount(Serializable key, Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			final RedisZSetCommands.Range rzRange = this.conversionRange(range);
			return this.redisTemplate.execute(new RedisCallback<Long>() {
				@Override
				public Long doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.zCount(keyByte, rzRange);
				}
			}, true);
		}
		return 0;
	}

	@Override
	public long zremrangeByScore(Serializable key, Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			final RedisZSetCommands.Range rzRange = this.conversionRange(range);
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zRemRangeByScore(keyByte, rzRange);
				}
			}, true);
		}
		return 0;
	}

	@Override
	public Set<Serializable> zrevrangeByScore(Serializable key, Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			final RedisZSetCommands.Range rzRange = this.conversionRange(range);
			Set<byte[]> memberByteSet = this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zRevRangeByScore(keyByte, rzRange);
				}
			}, true);
			if (CollectionUtils.isNotEmpty(memberByteSet)) {
				Set<Serializable> memberSet = new LinkedHashSet<Serializable>();
				for (byte[] memberByte : memberByteSet) {
					memberSet.add(this.deserialization(memberByte));
				}
				return memberSet;
			}
		}
		return null;
	}

	@Override
	public Set<Serializable> zrevrangeByScore(Serializable key, final Range range, final int offset, final int count) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			final RedisZSetCommands.Range rzRange = this.conversionRange(range);
			Set<byte[]> memberByteSet = this.redisTemplate
					.execute(new RedisCallback<Set<byte[]>>() {

						@Override
						public Set<byte[]> doInRedis(RedisConnection connection)
								throws DataAccessException {
							return connection.zRevRangeByScore(keyByte,
									rzRange, RedisZSetCommands.Limit.limit()
											.offset(offset).count(count));
						}
					}, true);
			if (CollectionUtils.isNotEmpty(memberByteSet)) {
				Set<Serializable> memberSet = new LinkedHashSet<Serializable>();
				for (byte[] memberByte : memberByteSet) {
					memberSet.add(this.deserialization(memberByte));
				}
				return memberSet;
			}
		}
		return null;
	}

	public void setRedisTemplate(RedisTemplate<byte[], byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}