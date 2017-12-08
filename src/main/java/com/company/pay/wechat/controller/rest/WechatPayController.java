package com.company.pay.wechat.controller.rest;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.pay.wechat.domain.WeChatPayConst;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.company.pay.wechat.service.impl.PayControllerServiceImpl;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

@RestController
@RequestMapping("/weChatPay")
public class WechatPayController {
	@Resource(name = "payControllerService")
	private PayControllerServiceImpl payControllerService;

	@RequestMapping(method = RequestMethod.POST, value = "{category}/{dbid}/{orderid}/requestPay")
	public ProcessResult requestPay(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid, @RequestBody WeChatScanPayRequest weChatScanPayRequest) {
		ProcessResult processResult = ControllerUtils.getErrorResponse(WeChatPayConst.RESULT_Error_Fail, "");
		try {
			processResult = payControllerService.requestPay(category, dbid, orderid, weChatScanPayRequest);
			return ControllerUtils.toJsonSimpleProcessResult(processResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, WeChatPayConst.RESULT_Error_Fail, processResult);
		}

	}
	@RequestMapping(method = RequestMethod.POST, value = "/requestPayNoOrder")
	public ProcessResult requestPayNoOrder(@RequestBody WeChatScanPayRequest weChatScanPayRequest) {
		ProcessResult processResult = ControllerUtils.getErrorResponse(WeChatPayConst.RESULT_Error_Fail, "");
		try {
			processResult = payControllerService.requestPayNoOrder(weChatScanPayRequest);
			return ControllerUtils.toJsonSimpleProcessResult(processResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, WeChatPayConst.RESULT_Error_Fail, processResult);
		}

	}
}
