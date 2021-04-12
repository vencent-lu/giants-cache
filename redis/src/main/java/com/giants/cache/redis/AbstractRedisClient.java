/**
 * 
 */
package com.giants.cache.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * @author vencent.lu
 *
 */
public abstract class AbstractRedisClient implements RedisClient {
	
	private SerializerFactory serializerFactory = new SerializerFactory();
	
	/**
	 * List转数组后，类型不确定的情况下，需要对数据的每个元素做强转
	 * 如 List.toArray() 方法得到 Object[] 不能确定是否能转成Serializable[]
	 * @param serializables serializables
	 * @return
	 */
	protected Serializable[] conversionSerializableArray(
			Serializable... serializables) {
		if (serializables!=null && serializables.length == 1) {
			Class<?> cls = serializables[0].getClass();
			if (cls.isArray()
					&& !Serializable.class.isAssignableFrom(cls
							.getComponentType())) {
				Object[] objArr = (Object[]) serializables[0];
				Serializable[] seriArr = new Serializable[objArr.length];
				for (int i = 0; i < objArr.length; i++) {
					seriArr[i] = (Serializable) objArr[i];
				}
				return seriArr;
			}
		}
		return serializables;
	}
	
	protected byte[] serializationKey(Serializable key) {
		if (key instanceof String) {
			return ((String) key).getBytes();
		}
		return this.serialization(key);
	}
	
	@Override
	public byte[] serialization(Serializable obj) {
		try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		AbstractHessianOutput out = new Hessian2Output(bos);
			out.writeObject(obj);
			out.flush();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return obj.toString().getBytes();
		}
	}

	@Override
	public Serializable deserialization(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Hessian2Input ois = new Hessian2Input(new ByteArrayInputStream(bytes));
		ois.setSerializerFactory(this.serializerFactory);
		try {
			Serializable obj = (Serializable)ois.readObject();
			ois.close();
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

    @Override
    public Lock getLock(Serializable key, int lockTimeOut) {
        while(true) {
            Long expireTime = System.currentTimeMillis() + lockTimeOut +1;
            if (this.setnx(key, expireTime)) {
                return new Lock(key, expireTime);
            }
            Long lockExpireTime =  (Long)this.get(key);
            if (lockExpireTime == null) {
                if (this.setnx(key, expireTime)) {
                    return new Lock(key, expireTime);
                }
            } else if (System.currentTimeMillis() > lockExpireTime) {
                lockExpireTime = (Long)this.getSet(key, expireTime);
                if (lockExpireTime == null || System.currentTimeMillis() > lockExpireTime) {
                    return new Lock(key, expireTime);
                }
            }
        }        
    }

    @Override
    public void releaseLock(Lock lock, int unReleaseExpireSends) {
        if (System.currentTimeMillis() <= lock.getExpireTime()) {
            this.del(lock.getKey());
        } else {
            this.expire(lock.getKey(), unReleaseExpireSends);
        }
    }

}
