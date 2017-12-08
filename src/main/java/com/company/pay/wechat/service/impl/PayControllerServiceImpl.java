package com.company.pay.wechat.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.company.pay.domain.Payinfo;
import com.company.pay.wechat.domain.WeChatPayConst;
import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.company.pay.wechat.service.WeChatScanPayService;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

@Service("payControllerService")
public class PayControllerServiceImpl extends OrderClientService {
	@Value("${notify_action:/weChatPayResp}")
	private String notify_action;

	@Resource(name = "weChatScanPayService")
	private WeChatScanPayService weChatScanPayService;

	/**
	 * 
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @return
	 */
	public ProcessResult requestPay(String category, String dbId, String orderid,
			WeChatScanPayRequest weChatScanPayRequest) {
		// TODO Auto-generated method stub
		List<String> keys = new ArrayList<String>();
		String payInfoKey = WeChatPayConst.ORDERKEY_PAYINFO;
		keys.add(payInfoKey);
		Map<String, String> maps = getOrderContextMap(category, dbId, orderid, keys);
		if (maps.containsKey(payInfoKey)) {
			Payinfo payinfo = JsonUtil.fromJson(maps.get(payInfoKey), Payinfo.class);
			if (weChatScanPayRequest == null) {
				weChatScanPayRequest = getWeChatScanPayRequest(payinfo);

			}
			weChatScanPayRequest.setNotify_action("/" + category + "/" + dbId + "/" + orderid + notify_action);
			weChatScanPayRequest.setOut_trade_no(orderid);
			weChatScanPayRequest.setProduct_id(orderid);
			return weChatScanPayService.doUnifiedPay(weChatScanPayRequest);

		} else {
			return ControllerUtils.getErrorResponse(WeChatPayConst.RESULT_Error_NoPayInfo,
					WeChatPayConst.RESULT_Error_NoPayInfo_MSG);
		}

	}

	public ProcessResult requestPayNoOrder(WeChatScanPayRequest weChatScanPayRequest) {
		// TODO Auto-generated method stub
		weChatScanPayRequest.setNotify_action(notify_action);
		weChatScanPayRequest.setOut_trade_no(weChatScanPayRequest.getOut_trade_no());
		weChatScanPayRequest.setProduct_id(weChatScanPayRequest.getOut_trade_no());
		System.out.println(weChatScanPayRequest.toString());
		return weChatScanPayService.doUnifiedPay(weChatScanPayRequest);

	}

	protected WeChatScanPayRequest getWeChatScanPayRequest(Payinfo payinfo) {
		WeChatScanPayRequest weChatScanPayRequest = new WeChatScanPayRequest();
		weChatScanPayRequest.setDevice_info(payinfo.getDeviceInfo());
		weChatScanPayRequest.setFee_type(payinfo.getFeeType());
		// weChatScanPayRequest.setNotify_action(notify_action + );
		// weChatScanPayRequest.setOut_trade_no(out_trade_no);
		// weChatScanPayRequest.setProduct_id(product_id);
		weChatScanPayRequest.setSpbill_create_ip(payinfo.getSpbillIp());
		weChatScanPayRequest.setTitle_desc_seller(payinfo.getTitleSeller());
		weChatScanPayRequest.setTitle_goods(payinfo.getTitleGoods());
		String totalFee = "";
		try {
			totalFee = String.valueOf(payinfo.getPayFee());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		weChatScanPayRequest.setTotal_fee(totalFee);
		// weChatScanPayRequest.setTrade_type(payinfo.get);
		return weChatScanPayRequest;
	}
}
