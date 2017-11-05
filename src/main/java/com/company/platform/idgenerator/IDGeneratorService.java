package com.company.platform.idgenerator;

import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface IDGeneratorService {
	/**
	 * 创建订单ID
	 * @param catetory
	 * @param ownerKey
	 * @param jsonRequest
	 * @return
	 */
	public ProcessResult createId(String category,String ownerKey,JsonRequest jsonRequest);
}
