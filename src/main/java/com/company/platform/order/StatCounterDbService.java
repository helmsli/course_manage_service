package com.company.platform.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

@Service("statCounterDbService")
public class StatCounterDbService {
	public static final int Error_Default = -2;
	public static final int Error_Exception = -1;
	public static final int Error_update=1;
	public static final int Haved_Counter = 255;
	public static final int Success_Counter=0;
	protected int RESULT_Success = 0;
	
	
	@Value("${counter.DbCenterUrl}")
	private String counterDbCenterUrl;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	/**
	 * 初始化对应的计数器
	 * @param category
	 * @param statCounter
	 * @return
	 */
	public ProcessResult initToZero( String category,StatCounter statCounter)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(counterDbCenterUrl + "/" +  category+ "/" + "initToZero" ,statCounter ,ProcessResult.class);
		
		return result;
	}
	
	/**
	 * 
	 * @param category
	 * @param statCounter
	 * @return   --result -- response return string counter; not exist return success
	 */
	public ProcessResult getAmount(String category,StatCounter statCounter)
	{
		
		ProcessResult result = null;
		result  = restTemplate.getForObject(counterDbCenterUrl + "/" +  category+ "/" + statCounter.getUserId() + "/" +   statCounter.getAmountId() +"/getAmount"  ,ProcessResult.class);

		return result;
	}
	/**
	 * 
	 * @param category
	 * @param statCounter
	 * @return
	 */
	public ProcessResult plusOne(String category,StatCounter statCounter)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(counterDbCenterUrl + "/" +  category +"/plusOne"  ,statCounter,ProcessResult.class);
		return result;
	}
}
