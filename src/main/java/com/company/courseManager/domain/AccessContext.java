package com.company.courseManager.domain;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PrivateKey;

/**
 * 用户接入的各种上下文信息
 * @author helmsli
 *
 */
public class AccessContext implements Serializable{
	
	/**
	 * 返回的对象
	 */
	private Object object;
	/**
	 * 事务号
	 */
	private String transid;
	
		public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}
	
	

		
	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	
	


	
	
	
	
	
}
