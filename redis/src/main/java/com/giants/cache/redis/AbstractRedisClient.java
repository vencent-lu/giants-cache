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

}
