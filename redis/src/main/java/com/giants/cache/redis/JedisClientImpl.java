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

import com.giants.cache.redis.RedisClient.ExpirationUnit;
import com.giants.cache.redis.RedisClient.SetOption;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author vencent.lu
 *
 * Create Date:2014年5月17日
 */
public class JedisClientImpl extends AbstractRedisClient {
	
	private JedisPool jedisPool;	
	
	@Override
	public Serializable get(Serializable key) {
		if (key != null) {
			Jedis jedis = null;
			Serializable value = null;
			try{
				jedis = this.jedisPool.getResource();				
				value = this.deserialization(jedis.get(this.serializationKey(key)));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return value;
		}		
		return null;
	}
	
	@Override
	public void set(Serializable key, Serializable value) {
		if (key != null && value != null) {
			Jedis jedis = null;
			try{
				jedis = this.jedisPool.getResource();
				jedis.set(this.serializationKey(key), this.serialization(value));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}			
		}		
	}
			
	@Override
	public boolean set(Serializable key, Serializable value, SetOption option,
			ExpirationUnit expirationUnit, long time) {
		if (key != null && value != null && option != null
				&& expirationUnit != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				return jedis.set(this.serializationKey(key), this.serialization(value),
						option.getNxxx().getBytes(), expirationUnit.getUnit()
								.getBytes(), time) != null;
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return false;
	}
	
	@Override
	public boolean set(Serializable key, Serializable value, SetOption option,
			ExpirationUnit expirationUnit, int time) {
		return this.set(key, value, option, expirationUnit, (long) time);
	}

	@Override
	public boolean setnx(Serializable key, Serializable value) {
		if (key != null && value != null) {
			Jedis jedis = null;
			try{
				jedis = this.jedisPool.getResource();
				return jedis.setnx(this.serializationKey(key), this.serialization(value)) == 1;
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}			
		}
		return false;
	}

	@Override
	public void expire(Serializable key, int seconds) {
		if (key != null) {
			Jedis jedis = null;
			try{
				jedis = this.jedisPool.getResource();
				byte[] keyBytes = this.serializationKey(key);
				jedis.expire(keyBytes, seconds);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public long pttl(Serializable key) {
		if (key != null) {
			Jedis jedis = null;
			long millisecondsToIdle;
			try{
				jedis = this.jedisPool.getResource();
				millisecondsToIdle = jedis.pttl(this.serializationKey(key));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return millisecondsToIdle;
		}		
		return 0;
	}
	
	@Override
	public void set(Serializable key, Serializable value, int seconds) {
		if (key != null && value != null) {
			Jedis jedis = null;
			try{
				jedis = this.jedisPool.getResource();
				byte[] keyBytes = this.serializationKey(key);
				jedis.set(keyBytes, this.serialization(value));
				jedis.expire(keyBytes, seconds);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public Set<byte[]> keys(byte[] pattern) {
		if (pattern != null) {
			Jedis jedis = null;
			Set<byte[]> value = null;
			try {
				jedis = this.jedisPool.getResource();
				value = jedis.keys(pattern);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return value;
		}
		return null;
	}

	@Override
	public Set<String> keys(String pattern) {
		if (pattern != null) {
			Jedis jedis = null;
			Set<String> value = null;
			try {
				jedis = this.jedisPool.getResource();
				value = jedis.keys(pattern);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return value;
		}
		return null;
	}

	@Override
	public void del(Serializable... keys) {
		keys = this.conversionSerializableArray(keys);
		if (keys != null && keys.length > 0) {			
			byte[][] bytesArray = new byte[keys.length][];
			for (int i=0; i<keys.length; i++) {
				bytesArray[i] = this.serializationKey(keys[i]);
			}
			
			Jedis jedis = null;
			try{
					jedis = this.jedisPool.getResource();
					jedis.del(bytesArray);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}					
	}
		
	@Override
	public void del(byte[] key) {
		if (key != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				jedis.del(key);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public List<Serializable> hmget(Serializable key, Serializable... fields) {
		fields = this.conversionSerializableArray(fields);
		if (key != null && fields != null && fields.length > 0) {			
			byte[][] fieldBytesArray = new byte[fields.length][];
			for (int i=0; i<fields.length; i++) {
				fieldBytesArray[i] = this.serialization(fields[i]);
			}
			Jedis jedis = null;
			List<byte[]> valueBytesArr = null;
			try {				
				jedis = this.jedisPool.getResource();
				valueBytesArr = jedis.hmget(this.serializationKey(key), fieldBytesArray);				
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
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
			Jedis jedis = null;
			Serializable obj = null;
			try {
				jedis = this.jedisPool.getResource();
				obj = this.deserialization(jedis.hmget(this.serializationKey(key), this.serialization(field)).get(0));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return obj;
		}
		return null;
	}
	
	@Override
	public void hmset(Serializable key, Map<?, ?> hash){
		if (key != null && hash != null && !hash.isEmpty()) {
			Map<byte[],byte[]> bytesHash = new HashMap<byte[],byte[]>();
			Iterator<?> it = hash.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<Serializable, Serializable> entry = (Entry<Serializable, Serializable>)it.next();
				bytesHash.put(this.serialization(entry.getKey()), this.serialization(entry.getValue()));
			}
			
			Jedis jedis = null;
			try {				
				jedis = this.jedisPool.getResource();
				jedis.hmset(this.serializationKey(key), bytesHash);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
	}
	
	@Override
	public void hmset(Serializable key, Serializable field, Serializable value) {
		if (key != null && field != null && value != null){
			Map<byte[],byte[]> bytesHash = new HashMap<byte[],byte[]>();
			bytesHash.put(this.serialization(field), this.serialization(value));
			Jedis jedis = null;
			try{				
				jedis = this.jedisPool.getResource();
				jedis.hmset(this.serializationKey(key), bytesHash);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}		
	}
	
	@Override
	public void hdel(Serializable key, Serializable... fields) {
		fields = this.conversionSerializableArray(fields);
		if (key != null && fields != null && fields.length > 0) {
			byte[][] fieldBytesArray = new byte[fields.length][];
			for (int i=0; i<fields.length; i++) {
				fieldBytesArray[i] = this.serialization(fields[i]);
			}
			Jedis jedis = null;
			try {				
				jedis = this.jedisPool.getResource();
				jedis.hdel(this.serializationKey(key), fieldBytesArray);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}			
		}
	}
		
	@Override
	public Set<Serializable> hkeys(Serializable key) {
		if (key != null) {
			Jedis jedis = null;
			Set<byte[]> bytesKeys = null;
			try {
				jedis = this.jedisPool.getResource();
				bytesKeys = jedis.hkeys(this.serializationKey(key));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
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
			Jedis jedis = null;
			List<byte[]> bytesValues = null;
			try {
				jedis = this.jedisPool.getResource();
				bytesValues = jedis.hvals(this.serializationKey(key));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
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
			Jedis jedis = null;
			Map<byte[], byte[]> entryByteMap = null;
			try {
				jedis = this.jedisPool.getResource();
				entryByteMap = jedis.hgetAll(this.serializationKey(key));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
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
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				byte[][] objBytes = new byte[members.length][];
				for (int i=0; i<members.length; i++) {
					objBytes[i] = this.serialization(members[i]);
				}
				return jedis.sadd(this.serializationKey(key), objBytes);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return 0;
	}
		
	@Override
	public boolean sismember(Serializable key, Serializable member) {
		if (key != null && member != null) {
			Jedis jedis = null;
			boolean isMember = false;
			try{
				jedis = this.jedisPool.getResource();
				isMember = jedis.sismember(this.serializationKey(key), this.serialization(member));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
			return isMember;
		}
		return false;
	}

	@Override
	public long srem(Serializable key, Serializable... members) {
		members = this.conversionSerializableArray(members);
		if (key != null && members != null && members.length > 0) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				byte[][] objBytes = new byte[members.length][];
				for (int i=0; i<members.length; i++) {
					objBytes[i] = this.serialization(members[i]);
				}
				return jedis.srem(this.serializationKey(key), objBytes);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return 0;
	}
	
	@Override
	public Set<Serializable> smembers(Serializable key) {
		if (key != null) {
			Jedis jedis = null;
			Set<byte[]> bytesKeys = null;
			try {
				jedis = this.jedisPool.getResource();
				bytesKeys = jedis.smembers(this.serializationKey(key));
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
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
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				return jedis.smembers(key);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	@Override
	public long zadd(Serializable key, Tuple... tuples) {
		if (key != null && ArrayUtils.isNotEmpty(tuples)) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				Map<byte[], Double> scoreMembers = new HashMap<byte[], Double>();
				for (Tuple tuple : tuples) {
					scoreMembers.put(this.serialization(tuple.getMember()), tuple.getScore());
				}
				return jedis.zadd(this.serializationKey(key), scoreMembers);
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return 0;
	}

	@Override
	public long zcount(Serializable key, Range range) {
		if (key != null && range != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				return jedis.zcount(this.serializationKey(key),
						range.minToBytes(), range.maxToBytes());
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return 0;
	}

	@Override
	public long zremrangeByScore(Serializable key, Range range) {
		if (key != null && range != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				return jedis.zremrangeByScore(this.serializationKey(key),
						range.minToBytes(), range.maxToBytes());
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return 0;
	}

	@Override
	public Set<Serializable> zrevrangeByScore(Serializable key, Range range) {
		if (key != null && range != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				Set<byte[]> memberByteSet = jedis.zrevrangeByScore(
						this.serializationKey(key), range.maxToBytes(),
						range.minToBytes());
				if (CollectionUtils.isNotEmpty(memberByteSet)) {
					Set<Serializable> memberSet = new LinkedHashSet<Serializable>();
					for (byte[] memberByte : memberByteSet) {
						memberSet.add(this.deserialization(memberByte));
					}
					return memberSet;
				}
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	@Override
	public Set<Serializable> zrevrangeByScore(Serializable key, Range range,
			int offset, int count) {
		if (key != null && range != null) {
			Jedis jedis = null;
			try {
				jedis = this.jedisPool.getResource();
				Set<byte[]> memberByteSet = jedis.zrevrangeByScore(
						this.serializationKey(key), range.maxToBytes(),
						range.minToBytes(), offset, count);
				if (CollectionUtils.isNotEmpty(memberByteSet)) {
					Set<Serializable> memberSet = new LinkedHashSet<Serializable>();
					for (byte[] memberByte : memberByteSet) {
						memberSet.add(this.deserialization(memberByte));
					}
					return memberSet;
				}
			} catch (JedisException e) {
				this.jedisPool.returnBrokenResource(jedis);
				throw e;
			} finally {
				this.jedisPool.returnResource(jedis);
			}
		}
		return null;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}	

}
