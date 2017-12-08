package com.company.pay.wechat.service;

import java.util.Date;

import com.xinwei.nnl.common.domain.ProcessResult;

public interface WeChatPayManagerService {
	/**
	 * 下载对账单
	 * @param billDate
	 * @param bill_type
	 * @return
	 */
	public ProcessResult doDownloadBill(Date billDate,String bill_type);
	
}
