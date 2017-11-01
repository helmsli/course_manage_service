package com.company.courseManager.domain;

import java.io.Serializable;

public class RequestAjax implements Serializable{
	/**
	 * 请求的transid
	 */
	private String transId;
	/**
	 * 请求的扩展参数，可以将客户端的扩展对象转换为ajax字符串进行传输
	 */
	private String reqParms;
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getReqParms() {
		return reqParms;
	}
	public void setReqParms(String reqParms) {
		this.reqParms = reqParms;
	}
	@Override
	public String toString() {
		return "RequestAjax [transId=" + transId + ", reqParms=" + reqParms + "]";
	}
	
	
}
