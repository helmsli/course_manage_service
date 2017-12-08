package com.company.pay.wechat.controller.rest;

import org.springframework.web.client.RestTemplate;

import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public class TestWechatPayController {
	private String baseUrl = "http://127.0.0.1:9200/weChatPay";
	private RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		weChatScanPayRequest.setOut_trade_no(String.valueOf(System.currentTimeMillis()));
		weChatScanPayRequest.setTotal_fee("1");
		weChatScanPayRequest.setSpbill_create_ip("127.0.0.1");
		
		ProcessResult processResult = restTemplate.postForObject(
				requestUrl, weChatScanPayRequest,
				ProcessResult.class);
		 if(processResult.getRetCode()==0)
		 {
			 
		 }
		 System.out.println(processResult);
		
	}

}
