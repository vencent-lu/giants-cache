/**
 * 
 */
package com.giants.cache.memcached.test;

import java.io.Serializable;

/**
 * @author vencent.lu
 *
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = -3277621412396196049L;
	
	private String name;
	private Integer age;	

	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [name=" + name + ", age=" + age + "]";
	}
	
	

}
