package com.company.pay.wechat.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.company.pay.wechat.domain.WeChatPayConst;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.company.pay.wechat.sdk.WXPay;
import com.company.pay.wechat.sdk.WXPayConstants;
import com.company.pay.wechat.service.WeChatScanPayService;
import com.xinwei.nnl.common.domain.ProcessResult;
@Service("weChatScanPayService")
public class WeChatScanPayServiceImpl extends BaseServiceImpl implements WeChatScanPayService {
	@Resource(name="weChatPay")
	private WXPay wxpay;
	private Logger log = LoggerFactory.getLogger(getClass());
		
	protected String getBody(WeChatScanPayRequest weChatScanPayRequest)
	{
		return weChatScanPayRequest.getTitle_desc_seller() + "-" + weChatScanPayRequest.getTitle_goods();
	}
	
	@Override
	public ProcessResult doUnifiedPay(WeChatScanPayRequest weChatScanPayRequest) {
		log.error(weChatScanPayRequest.toString());
		if(weChatScanPayRequest.CLIENT_H5_Pay.compareToIgnoreCase(weChatScanPayRequest.getTrade_type() )==0)
		{
			weChatScanPayRequest.setTrade_type(weChatScanPayRequest.TRADE_TYPE_H5);
		}
		else
		{
			weChatScanPayRequest.setTrade_type(weChatScanPayRequest.TRADE_TYPE_Native);
		}
		HashMap<String, String> data = new HashMap<String, String>();
	        /*
	         * PC网站	扫码支付	浏览器打开的网站主页title名 -商品概述	腾讯充值中心-QQ会员充
	         */
	        data.put("body", getBody(weChatScanPayRequest));
	        //商户订单号。要求32个字符内，只能是数字、大小写字母
	        data.put("out_trade_no", weChatScanPayRequest.getOut_trade_no());
	        //自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
	        data.put("device_info", weChatScanPayRequest.getDevice_info());
	        //符合ISO 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
	        data.put("fee_type", weChatScanPayRequest.getFee_type());
	        
	        data.put("total_fee", weChatScanPayRequest.getTotal_fee());
	        //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	        data.put("spbill_create_ip", weChatScanPayRequest.getSpbill_create_ip());
	        
	        data.put("notify_url",wxpay.getNotifyUrl(weChatScanPayRequest.getNotify_action()));
	        /*
	         * JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
	MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
	         */
	        data.put("trade_type", weChatScanPayRequest.getTrade_type());
	        /*
	         * trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义
	         */
	        data.put("product_id", weChatScanPayRequest.getProduct_id());
	        // data.put("time_expire", "20170112104120");
	        data.put("time_expire", "20291227091010");
	        try {
	            Map<String, String> responseData = wxpay.unifiedOrder(data);
	            return getFromResponse(responseData,null);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            return getFromResponse(e,null);
	        }
	        
	}

	@Override
	public ProcessResult doPayClose(String out_trade_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResult doPayQuery(String out_trade_no) {
		  System.out.println("查询订单");
	        HashMap<String, String> data = new HashMap<String, String>();
	        data.put("out_trade_no", out_trade_no);
//	        data.put("transaction_id", "4008852001201608221962061594");
	        try {
	            Map<String, String> responseData = wxpay.orderQuery(data);
	            return getFromResponse(responseData,null);
		           
	        } catch (Exception e) {
	            e.printStackTrace();
	            return getFromResponse(e,null);
	        }
	}

	@Override
	public ProcessResult doPayReverse(String out_trade_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResult doShortUrl(String out_trade_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResult doRefund(String out_refund_no, WeChatScanPayRequest weChatScanPayRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResult doRefundQuery(String out_refund_no) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Map<String, String> processResponseXml(String xmlStr) throws Exception 
	{
		return this.wxpay.processResponseXml(xmlStr);
	}
}
