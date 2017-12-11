package com.company.pay.wechat.controller.rest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.web.client.RestTemplate;

import com.company.coursestudent.domain.StudentConst;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public class TestWechatPayController {
	//private String baseUrl = "http://127.0.0.1:9200/weChatPay";
	private String baseUrl = "http://www.chunzeacademy.com:8080/weChatPay";
	
	private RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(StringEscapeUtils.unescapeJava("weixin://wxpay/bizpayurl?pr\\u003dqSDlQe9"));
		
		TestWechatPayController testWechatPayController = new TestWechatPayController();
		testWechatPayController.requestPay();
	}
	public void requestPay()
	{
		String requestUrl = this.baseUrl+"/requestPayNoOrder";
		WeChatScanPayRequest weChatScanPayRequest =new WeChatScanPayRequest();
		weChatScanPayRequest.setTitle_desc_seller("春泽");
		weChatScanPayRequest.setTitle_goods("test");
		weChatScanPayRequest.setDevice_info("172.16.1.1");
		//weChatScanPayRequest.setOut_trade_no(String.valueOf(System.currentTimeMillis()));
		String orderid= "5061070002";
		weChatScanPayRequest.setOut_trade_no(orderid);
		
		weChatScanPayRequest.setTotal_fee("301");
		weChatScanPayRequest.setSpbill_create_ip("127.0.0.1");
		
		//weChatScanPayRequest.setNotify_action("/wx_notify/123/456");
		weChatScanPayRequest.setNotify_action(StudentConst.ORDER_BUYER_CATEGORY + "/" + 123 + "/" + orderid + "/notifyPaySuccess");
		ProcessResult processResult = restTemplate.postForObject(
				requestUrl, weChatScanPayRequest,
				ProcessResult.class);
		 if(processResult.getRetCode()==0)
		 {
			 
		 }
		 System.out.println(processResult);
		
	}

}
