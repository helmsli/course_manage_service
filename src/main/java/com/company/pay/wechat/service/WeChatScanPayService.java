package com.company.pay.wechat.service;

import com.company.pay.wechat.domain.WeChatScanPayRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface WeChatScanPayService {
	/**
	 * 
	 * @param weChatScanPayRequest
	 * @return
	 */
	public ProcessResult doUnifiedPay(WeChatScanPayRequest weChatScanPayRequest);
	/**
	 * 关闭支付
	 * @param out_trade_no
	 * @return
	 */
	public ProcessResult doPayClose(String out_trade_no);
	/**
	 * 支付查询
	 * @param out_trade_no
	 * @return
	 */
	public ProcessResult doPayQuery(String out_trade_no);
	/**
	 * 支付撤销
	 * @param out_trade_no
	 * @return
	 */
	public ProcessResult doPayReverse(String out_trade_no);

	/**
	 * 长链接转短链接
	 * @param out_trade_no
	 * @return
	 */
	public ProcessResult doShortUrl(String out_trade_no);

	/**
	 * 退款
	 * @param out_refund_no
	 * @param weChatScanPayRequest
	 * @return
	 */
	public ProcessResult doRefund(String out_refund_no,WeChatScanPayRequest weChatScanPayRequest);

	/**
	 * 
	 * @param out_refund_no
	 * @return
	 */
	public ProcessResult doRefundQuery(String out_refund_no);
}
