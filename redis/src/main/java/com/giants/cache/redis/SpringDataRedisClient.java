/**
 * 
 */
package com.giants.cache.redis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
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
	public Set<byte[]> keys(byte[] pattern) {
		if (pattern != null) {
			return this.redisTemplate.keys(pattern);
		}
		return null;
	}

	@Override
	public Set<String> keys(String pattern) {
		if (pattern != null) {
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
	public List<?> hmget(Serializable key, Serializable... fields) {
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
	public Set<?> hkeys(Serializable key) {
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
	public List<?> hvals(Serializable key) {
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
	public void sadd(Serializable key, Serializable... members) {
		if (key != null && members != null && members.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.sAdd(keyByte, objBytes);
					return null;
				}
			});
		}
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
	public void srem(Serializable key, Serializable... members) {
		if (key != null && members != null && members.length > 0) {
			final byte[] keyByte = this.serializationKey(key);
			final byte[][] objBytes = new byte[members.length][];
			for (int i=0; i<members.length; i++) {
				objBytes[i] = this.serialization(members[i]);
			}
			this.redisTemplate.execute(new RedisCallback<Object>() {

				@Override
				public Object doInRedis(RedisConnection connection)
						throws DataAccessException {
					connection.sRem(keyByte, objBytes);
					return null;
				}
			});
		}
		
	}

	@Override
	public Set<?> smembers(Serializable key) {
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

	public void setRedisTemplate(RedisTemplate<byte[], byte[]> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
