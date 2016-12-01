/**
 * 
 */
package com.giants.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

/**
 * @author vencent.lu
 *
 */
public class SpringDataRedisClient extends AbstractRedisClient {
	
	private RedisTemplate<byte[], byte[]> redisTemplate;

	@Override
	public Serializable get(Serializable key) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			return this.deserialization(this.redisTemplate
					.execute(new RedisCallback<byte[]>() {
						@Override
						public byte[] doInRedis(RedisConnection connection)
								throws DataAccessException {
							return connection.get(keyByte);
						}
					}));
		}
		return null;
	}

	@Override
	public void set(Serializable key, Serializable value) {
		if (key != null && value != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] valueByte = this.serialization(value);
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.set(keyByte, valueByte);
					return null;
				}
			});
		}		
	}
		
	@Override
	public boolean set(Serializable key, Serializable value, final SetOption option,
			final ExpirationUnit expirationUnit, final long time) {
		if (key != null && value != null && option != null
				&& expirationUnit != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] valueByte = this.serialization(value);
			return this.redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					Object conn = connection.getNativeConnection();
					if (!(conn instanceof Jedis)) {
						throw new UnsupportedOperationException(
								"SET with options Only support Jedis. Please use SETNX, SETEX, PSETEX.");
					}					
					return ((Jedis)conn).set(keyByte, valueByte,
							option.getNxxx().getBytes(), expirationUnit.getUnit()
							.getBytes(), time) != null;		
				}
			});
		}
		return false;
	}

	@Override
	public boolean set(Serializable key, Serializable value, SetOption option,
			ExpirationUnit expirationUnit, int time) {
		return this.set(key, value, option, expirationUnit, (long)time);
	}

	@Override
	public boolean setnx(Serializable key, Serializable value) {
		if (key != null && value != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] valueByte = this.serialization(value);
			return this.redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.setNX(keyByte, valueByte);
				}
			});
		}
		return false;
	}

	@Override
	public void expire(Serializable key, final int seconds) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.expire(keyByte, seconds);
					return null;
				}
			});
		}
	}

	@Override
	public long pttl(Serializable key) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.pTtl(keyByte);
				}
			});
		}
		return 0;
	}

	@Override
	public void set(Serializable key, Serializable value, final int seconds) {
		if (key != null && value != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] valueByte = this.serialization(value);
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.set(keyByte, valueByte);
					connection.expire(keyByte, seconds);
					return null;
				}
			});
		}
	}

	@Override
	public Set<byte[]> keys(final byte[] pattern) {
		if (pattern != null) {
			return this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.keys(pattern);
				}
			});
		}
		return null;
	}

	@Override
	public Set<String> keys(String pattern) {
		if (pattern != null) {
			final byte[] patternByte = this.serializationKey(pattern);
			Set<byte[]> keyByteSet = this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.keys(patternByte);
				}
			});
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
		if (keys != null && keys.length >0) {
			final byte[][] keyBytes = new byte[keys.length][];
			for (int i = 0; i < keys.length; i++) {
				keyBytes[i] = this.serializationKey(keys[i]);
			}
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.del(keyBytes);
					return null;
				}
			});
		}
		
		
	}

	@Override
	public void del(final byte[] key) {
		if (key != null) {
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.del(key);
					return null;
				}
			});
		}
	}

	@Override
	public List<Serializable> hmget(Serializable key, Serializable... fields) {
		fields = this.conversionSerializableArray(fields);
		if (key != null && fields != null && fields.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] fieldBytesArray = new byte[fields.length][];
			for (int i=0; i<fields.length; i++) {
				fieldBytesArray[i] = this.serialization(fields[i]);
			}
			List<byte[]> valueBytesArr = this.redisTemplate.execute(new RedisCallback<List<byte[]>>() {

				@Override
				public List<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.hMGet(keyByte, fieldBytesArray);
				}
			});
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
			final byte[] keyByte = this.serializationKey(key);
			final byte[] fieldByte = this.serialization(field);
			return this.deserialization(this.redisTemplate.execute(new RedisCallback<byte[]>() {

				@Override
				public byte[] doInRedis(RedisConnection connection)
						throws DataAccessException {
					List<byte[]> rs = connection.hMGet(keyByte, fieldByte);
					if (CollectionUtils.isNotEmpty(rs)) {
						return rs.get(0);
					}
					return null;
				}
			}));
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
				Entry<Serializable, Serializable> entry = (Entry<Serializable, Serializable>)it.next();
				bytesHash.put(this.serialization(entry.getKey()), this.serialization(entry.getValue()));
			}
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.hMSet(keyByte, bytesHash);
					return null;
				}
			});
		}		
	}

	@Override
	public void hmset(Serializable key, Serializable field, Serializable value) {
		if (key != null && field != null && value != null) {
			final byte[] keyByte = this.serializationKey(key);
			final Map<byte[],byte[]> bytesHash = new HashMap<byte[],byte[]>();
			bytesHash.put(this.serialization(field), this.serialization(value));
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.hMSet(keyByte, bytesHash);
					return null;
				}
			});
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
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.hDel(keyByte, fieldBytesArray);
					return null;
				}
			});
		}
		
	}

	@Override
	public Set<Serializable> hkeys(Serializable key) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			Set<byte[]> bytesKeys = this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.hKeys(keyByte);
				}
			});
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
			final byte[] keyByte = this.serializationKey(key);
			List<byte[]> bytesValues = this.redisTemplate.execute(new RedisCallback<List<byte[]>>() {

				@Override
				public List<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.hVals(keyByte);
				}
			});
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
			final byte[] keyByte = this.serializationKey(key);
			Map<byte[], byte[]> entryByteMap = this.redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {

				@Override
				public Map<byte[], byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.hGetAll(keyByte);
				}
			});
			if (MapUtils.isNotEmpty(entryByteMap)) {
				Map<Serializable, Serializable> entryMap = new HashMap<Serializable, Serializable>();
				for (Entry<byte[], byte[]> entry : entryByteMap.entrySet()) {
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
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.sAdd(keyByte, objBytes);
				}
			});
		}
		return 0;
	}
	
	@Override
	public boolean sismember(Serializable key, Serializable member) {
		if (key != null && member != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] memberByte = this.serialization(member);
			return this.redisTemplate.execute(new RedisCallback<Boolean>(){

				@Override
				public Boolean doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.sIsMember(keyByte, memberByte);
				}});
		}
		return false;
	}

	@Override
	public long srem(Serializable key, Serializable... members) {
		members = this.conversionSerializableArray(members);
		if (key != null && members != null && members.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.sRem(keyByte, objBytes);
				}
			});
		}
		return 0;
	}

	@Override
	public Set<Serializable> smembers(Serializable key) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			Set<byte[]> bytesKeys = this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.sMembers(keyByte);
				}
			});
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
	public Set<byte[]> smembers(final byte[] key) {
		if (key != null) {
			return this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.sMembers(key);
				}
			});
		}
		return null;
	}
	
	@Override
	public long zadd(Serializable key, Tuple... tuples) {
		if (key != null && ArrayUtils.isNotEmpty(tuples)) {
			final byte[] keyByte = this.serializationKey(key);
			final Set<RedisZSetCommands.Tuple> tupleSet = new HashSet<RedisZSetCommands.Tuple>();
			for (Tuple tuple : tuples) {
				tupleSet.add(new DefaultTuple(this.serialization(tuple.getMember()), tuple.getScore().doubleValue()));
			}
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zAdd(keyByte, tupleSet);
				}
			});
		}
		return 0;
	}
	
	@Override
	public Double zscore(Serializable key, Serializable member) {
		if (key != null && member != null) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[] memberByte = this.serialization(member);
			return this.redisTemplate.execute(new RedisCallback<Double>() {

				@Override
				public Double doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zScore(keyByte, memberByte);
				}
			});
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
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zRem(keyByte, objBytes);
				}
			});
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
	public long zcount(Serializable key,final Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zCount(keyByte, conversionRange(range));
				}
			});
		}
		return 0;
	}

	@Override
	public long zremrangeByScore(Serializable key, final Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			return this.redisTemplate.execute(new RedisCallback<Long>() {

				@Override
				public Long doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zRemRangeByScore(keyByte, conversionRange(range));
				}
			});
		}
		return 0;
	}

	@Override
	public Set<Serializable> zrevrangeByScore(Serializable key,final Range range) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			Set<byte[]> memberByteSet = this.redisTemplate.execute(new RedisCallback<Set<byte[]>>() {

				@Override
				public Set<byte[]> doInRedis(RedisConnection connection)
						throws DataAccessException {
					return connection.zRevRangeByScore(keyByte, conversionRange(range));
				}
			});
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
	public Set<Serializable> zrevrangeByScore(Serializable key, final Range range,
		final	int offset, final int count) {
		if (key != null) {
			final byte[] keyByte = this.serializationKey(key);
			Set<byte[]> memberByteSet = this.redisTemplate
					.execute(new RedisCallback<Set<byte[]>>() {

						@Override
						public Set<byte[]> doInRedis(RedisConnection connection)
								throws DataAccessException {
							return connection.zRevRangeByScore(keyByte,
									conversionRange(range), Limit.limit()
											.offset(offset).count(count));
						}
					});
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
