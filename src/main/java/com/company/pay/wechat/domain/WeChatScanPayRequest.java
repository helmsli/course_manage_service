package com.company.pay.wechat.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
/**
 * 扫码支付的请求
 * @author helmsli
 *
 */
public  class WeChatScanPayRequest implements Serializable {
	
		
	//付款商家信息title
	private String title_desc_seller;
	//付款商品信息title
	private String title_goods;
	//交易号
	private String out_trade_no;
	//自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
	private String device_info="WEB";
	//支付金额类型
	private String fee_type="CNY";
	//支付的总金额
	private String total_fee;
	//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
    private String spbill_create_ip="127.0.0.1";
	//微信回调地址
    private String notify_action;
	 /*
    * JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
    */
    private String trade_type="NATIVE";
    /*
     * trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义
     */
    private String product_id;
	public String getTitle_desc_seller() {
		return title_desc_seller;
	}
	public void setTitle_desc_seller(String title_desc_seller) {
		this.title_desc_seller = title_desc_seller;
	}
	public String getTitle_goods() {
		return title_goods;
	}
	public void setTitle_goods(String title_goods) {
		this.title_goods = title_goods;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
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
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	
	
	public String getNotify_action() {
		return notify_action;
	}
	public void setNotify_action(String notify_action) {
		this.notify_action = notify_action;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
    
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
    
}
