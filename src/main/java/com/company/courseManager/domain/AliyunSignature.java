package com.company.courseManager.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

public class AliyunSignature implements Serializable{
	private String action;
	private String version="2017-03-21";
	private String accessKeyId;
	private String signatureMethod ="HMAC-SHA1";
	private String signatureVersion = "1.0";
	/**
	 * 唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值
	 */
	private String signatureNonce = "";
	private String format = "JSON";
	private String timestamp="";
	private  final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    
	private Map<String,String> parameters = new HashMap<String,String>();
	
	
	public AliyunSignature()
	{
		//formatIso8601Date(new Date())
		this.setTimestamp(this.formatIso8601Date(Calendar.getInstance().getTime()));
		this.setSignatureNonce(createSignatureNonce());
		
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getSignatureMethod() {
		return signatureMethod;
	}
	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}
	public String getSignatureVersion() {
		return signatureVersion;
	}
	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}
	public String getSignatureNonce() {
		return signatureNonce;
	}
	public void setSignatureNonce(String signatureNonce) {
		this.signatureNonce = signatureNonce;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public Map<String, String> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getAction() {
		return action;
	}
	
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String,String> getSignatureParas()
	{
		Map<String, String> parameters = new HashMap<String, String>();
	    // 加入请求参数
	    parameters.put("Action", this.getAction());
	    parameters.put("Version", this.getVersion());
	    parameters.put("AccessKeyId", this.getAccessKeyId());
	    parameters.put("TimeStamp", this.getTimestamp());
	    parameters.put("SignatureMethod", this.getSignatureMethod());
	    parameters.put("SignatureVersion", this.getSignatureVersion());
	    parameters.put("SignatureNonce", this.getSignatureNonce());
	    parameters.put("Format", this.getFormat());
	    parameters.putAll(this.getParameters());
	    return parameters;
	}
	
	/**
	 * 时间格式化
	 * @param date
	 * @return
	 */
	public  String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
	/**
	 * 事务号
	 * @return
	 */
	public String createSignatureNonce()
	{
		long transId =  System.currentTimeMillis()-1504369881000l;
		return transId+"-"+getRandom();
	}
	/**
	 * 随机数
	 * @return
	 */
	protected String getRandom()
	{
		long mobile_code = (long)(Math.random()*100000000);		
		return String.valueOf(mobile_code);
	}
}
