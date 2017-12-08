package com.company.pay.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Model class of PayInfo.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
public class Payinfo implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** 商家信息的title. */
	private String titleSeller;

	/** 付款商品信息. */
	private String titleGoods;

	/** 付款的原始订单号. */
	private String orderId;

	/** 请求支付的用户信息. */
	private String reqUser;

	/** 设备信息. */
	private String deviceInfo;

	/** 接入MAC和IP地址信息. */
	private String spbillIp;

	/** 币种. */
	private String feeType;

	/** 总费用. */
	private double totalFee;

	/** 实付金额. */
	private double payFee;

	/** 剩余金额. */
	private double remainFee;

	/** 付款的方式. */
	private String category;

	/** 付款应答信息. */
	private String response;

	/** 付款结果. */
	private int payResult;

	/** 校验和. */
	private String crc;

	/** status. */
	private int status;

	/**
	 * Constructor.
	 */
	public Payinfo() {
	}

	/**
	 * Set the 商家信息的title.
	 * 
	 * @param titleSeller
	 *            商家信息的title
	 */
	public void setTitleSeller(String titleSeller) {
		this.titleSeller = titleSeller;
	}

	/**
	 * Get the 商家信息的title.
	 * 
	 * @return 商家信息的title
	 */
	public String getTitleSeller() {
		return this.titleSeller;
	}

	/**
	 * Set the 付款商品信息.
	 * 
	 * @param titleGoods
	 *            付款商品信息
	 */
	public void setTitleGoods(String titleGoods) {
		this.titleGoods = titleGoods;
	}

	/**
	 * Get the 付款商品信息.
	 * 
	 * @return 付款商品信息
	 */
	public String getTitleGoods() {
		return this.titleGoods;
	}

	/**
	 * Set the 付款的原始订单号.
	 * 
	 * @param orderId
	 *            付款的原始订单号
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * Get the 付款的原始订单号.
	 * 
	 * @return 付款的原始订单号
	 */
	public String getOrderId() {
		return this.orderId;
	}

	/**
	 * Set the 请求支付的用户信息.
	 * 
	 * @param reqUser
	 *            请求支付的用户信息
	 */
	public void setReqUser(String reqUser) {
		this.reqUser = reqUser;
	}

	/**
	 * Get the 请求支付的用户信息.
	 * 
	 * @return 请求支付的用户信息
	 */
	public String getReqUser() {
		return this.reqUser;
	}

	/**
	 * Set the 设备信息.
	 * 
	 * @param deviceInfo
	 *            设备信息
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * Get the 设备信息.
	 * 
	 * @return 设备信息
	 */
	public String getDeviceInfo() {
		return this.deviceInfo;
	}

	/**
	 * Set the 接入MAC和IP地址信息.
	 * 
	 * @param spbillIp
	 *            接入MAC和IP地址信息
	 */
	public void setSpbillIp(String spbillIp) {
		this.spbillIp = spbillIp;
	}

	/**
	 * Get the 接入MAC和IP地址信息.
	 * 
	 * @return 接入MAC和IP地址信息
	 */
	public String getSpbillIp() {
		return this.spbillIp;
	}

	/**
	 * Set the 币种.
	 * 
	 * @param feeType
	 *            币种
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	/**
	 * Get the 币种.
	 * 
	 * @return 币种
	 */
	public String getFeeType() {
		return this.feeType;
	}

	/**
	 * Set the 总费用.
	 * 
	 * @param totalFee
	 *            总费用
	 */
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	/**
	 * Get the 总费用.
	 * 
	 * @return 总费用
	 */
	public double getTotalFee() {
		return this.totalFee;
	}

	/**
	 * Set the 实付金额.
	 * 
	 * @param payFee
	 *            实付金额
	 */
	public void setPayFee(double payFee) {
		this.payFee = payFee;
	}

	/**
	 * Get the 实付金额.
	 * 
	 * @return 实付金额
	 */
	public double getPayFee() {
		return this.payFee;
	}

	/**
	 * Set the 剩余金额.
	 * 
	 * @param remainFee
	 *            剩余金额
	 */
	public void setRemainFee(double remainFee) {
		this.remainFee = remainFee;
	}

	/**
	 * Get the 剩余金额.
	 * 
	 * @return 剩余金额
	 */
	public double getRemainFee() {
		return this.remainFee;
	}

	/**
	 * Set the 付款的方式.
	 * 
	 * @param category
	 *            付款的方式
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Get the 付款的方式.
	 * 
	 * @return 付款的方式
	 */
	public String getCategory() {
		return this.category;
	}

	/**
	 * Set the 付款应答信息.
	 * 
	 * @param response
	 *            付款应答信息
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * Get the 付款应答信息.
	 * 
	 * @return 付款应答信息
	 */
	public String getResponse() {
		return this.response;
	}

	/**
	 * Set the 付款结果.
	 * 
	 * @param payResult
	 *            付款结果
	 */
	public void setPayResult(int payResult) {
		this.payResult = payResult;
	}

	/**
	 * Get the 付款结果.
	 * 
	 * @return 付款结果
	 */
	public int getPayResult() {
		return this.payResult;
	}

	/**
	 * Set the 校验和.
	 * 
	 * @param crc
	 *            校验和
	 */
	public void setCrc(String crc) {
		this.crc = crc;
	}

	/**
	 * Get the 校验和.
	 * 
	 * @return 校验和
	 */
	public String getCrc() {
		return this.crc;
	}

	/**
	 * Set the status.
	 * 
	 * @param status
	 *            status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Get the status.
	 * 
	 * @return status
	 */
	public int getStatus() {
		return this.status;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
