package com.company.platform.order;

import java.security.MessageDigest;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderPayResult {
	public static final int PAY_RESULT_ALLSUCCESS= 0;
	public static final int PAY_RESULT_PARTSUCCESS= 1;
	public static final int PAY_RESULT_FAIL= -1;
	
	public static final String PTYPE_WECHAT= "wechat";
	
	// 支付金额类型
	private String fee_type = "CNY";
	// 支付的总金额
	private String total_fee;
	/**
	 * 可能是个列表，用,分割
	 */
	private String payOrder="";
	private String crcKey;
	/**
	 * 0--全部支付   1--部分支付成功；
	 */
	private int payResult=PAY_RESULT_FAIL;
	/**
	 * 支付方式
	 */
	private String paymentType = "0";

	private Date payTime;
	
	
	public String getFee_type() {
		return fee_type;
	}


	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}


	public String getTotal_fee() {
		return total_fee;
	}


	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}


	public String getPayOrder() {
		return payOrder;
	}


	public void setPayOrder(String payOrder) {
		this.payOrder = payOrder;
	}


	public String getCrcKey() {
		return crcKey;
	}


	public void setCrcKey(String crcKey) {
		this.crcKey = crcKey;
	}


	public String getPaymentType() {
		return paymentType;
	}


	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}


	public Date getPayTime() {
		return payTime;
	}


	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}


	public int getPayResult() {
		return payResult;
	}


	public void setPayResult(int payResult) {
		this.payResult = payResult;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public String createCrcKey(String crckey)  {
	    try {
			StringBuilder str = new StringBuilder();
			str.append(this.getFee_type());
			str.append(this.getTotal_fee());
			str.append(this.paymentType);
			str.append(this.payOrder);
			str.append(this.payResult);
			if(crckey==null||crckey=="")
			{
				str.append("lihairong");	
			}
			else
			{
				str.append(crckey);
			}
			return MD5(str.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace()
			;
		}
	    return null;
        
	}
	
	public boolean crcOk(String crcResult)
	{
		if(this.crcKey.compareToIgnoreCase(crcResult)==0)
			return true;
		return false;
	}

	
	public  String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

}
