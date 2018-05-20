package com.company.platform.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMainContext;

public class StatAmountService {
	protected int RESULT_Success = 0;
	
	
	@Value("${stat.statServiceUrl}")
	private String statServiceUrl;
	
	
	
	
	@Autowired
	protected RestTemplate restTemplate;
	
	/**
	 * 增加统计计数
	 * @param category
	 * @param statAmountBase
	 * @return
	 */
	public ProcessResult plusStatAmount(String category,StatCounter statAmountBase)
	{
		
		ProcessResult result = null;		
		//System.out.println("**************"+this.orderServiceUrl + "/" + category + "/" + orderId + "/startOrder");
		result  = restTemplate.postForObject(this.statServiceUrl + "/" + category + "/plusStatAmount" ,statAmountBase,ProcessResult.class);
		return result;
	}
	
	/**
	 * 
	 * @param category
	 * @param statAmountBase
	 * @return
	 */
	public ProcessResult queryStatAmount(String category,StatCounter statAmountBase)
	{
		
		ProcessResult result = null;		
		//System.out.println("**************"+this.orderServiceUrl + "/" + category + "/" + orderId + "/startOrder");
		result  = restTemplate.postForObject(this.statServiceUrl + "/" + category + "/plusStatAmount" ,statAmountBase,ProcessResult.class);
		return result;
	}
}
