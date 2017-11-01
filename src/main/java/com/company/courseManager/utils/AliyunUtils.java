package com.company.courseManager.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.company.courseManager.domain.AliyunSignature;

public class AliyunUtils {
	/**
	 * action
	 */
	public static final String Action_GetVideoPlayAuth="GetVideoPlayAuth";
	
	/**
	 * format
	 */
	public static final String Format_JSON = "JSON";
	/**
	 * http method
	 */
	public  static final String HttpMethod_Get = "GET";
	
	/**
	 * signature method
	 */
	public static final String Sign_method_HMACSHA1="HMAC-SHA1";
	/**
	 * signature algorithm
	 */
	private static final String MAC_NAME_HmacSha1 = "HmacSHA1";    
    private static final String ENCODING_UTF8 = "UTF-8";
    
    
	
	/**
	 * 返回阿里url编码
	 * @param value
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String percentEncode(String value) throws UnsupportedEncodingException {
		  return value != null ? URLEncoder.encode(value, ENCODING_UTF8).replace("+", "%20").replace("*", "%2A").replace("%7E", "~") : null;
		}
	/**
	 * 用于向阿里云发送公共参数签名，获取get的Url
	 * @param httpMethod
	 * @param key
	 * @param aliyunSignature
	 * @return
	 * @throws Exception
	 */
	public static String formatSignatureUrl(String httpMethod,String key,AliyunSignature aliyunSignature) throws Exception
	{
		String strToken = getStringToken(httpMethod,aliyunSignature);
		String signature= hmacSHA1Encrypt(strToken,key);
		Map<String, String> parameters = aliyunSignature.getSignatureParas();
		String[] sortedKeys = parameters.keySet().toArray(new String[]{});
		StringBuilder sRet = new StringBuilder("Signature=");
		sRet.append(signature);
		
		for(String parameterKey : sortedKeys) {
			sRet.append("&");
			sRet.append(URLEncoder.encode(parameterKey,ENCODING_UTF8));
			sRet.append("=");
			sRet.append(URLEncoder.encode(parameters.get(parameterKey),ENCODING_UTF8));
		};
	    return sRet.toString();
	}

	/**
	 * 获取阿里需要签名的原始字符串
	 * @param httpMethod
	 * @param aliyunSignature
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getStringToken(String httpMethod,AliyunSignature aliyunSignature) throws UnsupportedEncodingException
	{
		final String HTTP_METHOD = httpMethod;

	    Map<String, String> parameters = new HashMap<String, String>();
	    // 加入请求参数
	    parameters.put("Action", aliyunSignature.getAction());
	    parameters.put("Version", aliyunSignature.getVersion());
	    parameters.put("AccessKeyId", aliyunSignature.getAccessKeyId());
	    parameters.put("TimeStamp", aliyunSignature.getTimestamp());
	    parameters.put("SignatureMethod", aliyunSignature.getSignatureMethod());
	    parameters.put("SignatureVersion", aliyunSignature.getSignatureVersion());
	    parameters.put("SignatureNonce", aliyunSignature.getSignatureNonce());
	    parameters.put("Format", aliyunSignature.getFormat());
	    parameters.putAll(aliyunSignature.getParameters());
	    
	    // 对参数进行排序
	    String[] sortedKeys = parameters.keySet().toArray(new String[]{});
	    Arrays.sort(sortedKeys);

	    final String SEPARATOR = "&";

	    // 生成stringToSign字符串
	    StringBuilder stringToSign = new StringBuilder();
	    stringToSign.append(HTTP_METHOD).append(SEPARATOR);
	    stringToSign.append(percentEncode("/")).append(SEPARATOR);

	    StringBuilder canonicalizedQueryString = new StringBuilder();
	    for(String key : sortedKeys) {
	        // 这里注意对key和value进行编码
	        canonicalizedQueryString.append("&")
	        .append(percentEncode(key)).append("=")
	        .append(percentEncode(parameters.get(key)));
	    }

	    // 这里注意对canonicalizedQueryString进行编码
	    stringToSign.append(percentEncode(
	        canonicalizedQueryString.toString().substring(1)));
		return stringToSign.toString();
	}
	
	/**
	 * 根据key和待签名的字符串进行签名
	 * @param encryptText
	 * @param encryptKey
	 * @return
	 * @throws Exception
	 */
	public static String hmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception     
    {           
        byte[] data=encryptKey.getBytes(ENCODING_UTF8);  
        //根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称  
        SecretKey secretKey = new SecretKeySpec(data,MAC_NAME_HmacSha1);   
        //生成一个指定 Mac 算法 的 Mac 对象  
        Mac mac = Mac.getInstance(MAC_NAME_HmacSha1);   
        //用给定密钥初始化 Mac 对象  
        mac.init(secretKey);    
        //mac.init(new SecretKeySpec(key.getBytes(ENCODING), ALGORITHM));          
        byte[] text = encryptText.getBytes(ENCODING_UTF8);    
        //完成 Mac 操作    UI%2FwKfuvTtphzCKHwPhP0ErtLnc%3D  RY4Fvhz6FlCVJzboDtheS25tghM=
        byte[] bRet=mac.doFinal(text); 
        //Base64.encodeBase64(signData)
        String sRet= Base64.getEncoder().encodeToString(bRet);
        //System.out.println(sRet);
        //return sRet;
        return URLEncoder.encode(sRet,ENCODING_UTF8);
       // return new String(Base64.encodeBase64(bRet),"UTF-8");
        
    }
}
