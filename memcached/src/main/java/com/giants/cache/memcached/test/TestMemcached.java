/**
 * Copyright (c) 2008 Greg Whalin
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the BSD license
 *
 * This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * You should have received a copy of the BSD License along with this
 * library.
 *
 * @author greg whalin <greg@meetup.com> 
 */
package com.giants.cache.memcached.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.giants.cache.memcached.MemcachedClient;
import com.giants.cache.memcached.SockIOPool;
import com.giants.common.tools.EncryptionTools;

public class TestMemcached  {  
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		String[] servers = { "192.168.0.153:11211"};
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers( servers );
		pool.setFailover( true );
		pool.setInitConn( 10 ); 
		pool.setMinConn( 5 );
		pool.setMaxConn( 250 );
		pool.setMaintSleep( 30 );
		pool.setNagle( false );
		pool.setSocketTO( 300 );
		pool.setAliveCheck( true );
		pool.initialize();

		MemcachedClient mcc = new MemcachedClient();

		/*User user = new User("vencent", 32);
		mcc.set("user", user);
		
		List<User> users = new ArrayList<User>();
		users.add(user);
		users.add(new User("scsedux", 23));
		users.add(new User("lutao", 13));
		mcc.set("users", users);*/
		System.out.println(mcc.get("user").toString());
		System.out.println(mcc.get("users").toString());
		
		Date date = new Date(1000);
		System.out.println(date.getTime());
		
		User user = new User("vencent", 32);
		BUser buser = new BUser("vencent", 10);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		AbstractHessianOutput out = new Hessian2Output(bos);
        out.writeObject(buser);
        out.flush();
        System.out.println(bos);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bos.toByteArray());
        System.out.println(EncryptionTools.MD5(bos.toByteArray()));
        
        Hessian2Input ois = new Hessian2Input(new ByteArrayInputStream(bos.toByteArray()));
        Object o = ois.readObject();
		ois.close();
		System.out.println(o.toString());
	}
}
