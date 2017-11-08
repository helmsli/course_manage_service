package com.company.platform.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlow;
import com.xinwei.orderDb.domain.OrderMainContext;

public class OrderClientService {
	
	protected int RESULT_Success = 0;
	
	@Value("${order.orderServiceUrl}")
	private String orderServiceUrl;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	
	
	public String getOrderServiceUrl() {
		return orderServiceUrl;
	}

	public void setOrderServiceUrl(String orderServiceUrl) {
		this.orderServiceUrl = orderServiceUrl;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	
	//@RequestMapping(method = RequestMethod.POST, value = "/{category}/{dbId}/{orderId}/createOrder")
	public ProcessResult createOrder(OrderMainContext orderMainContext)
	{
		ProcessResult result  = restTemplate.postForObject(orderServiceUrl + "/" +  orderMainContext.getCatetory()+ "/" + orderMainContext.getDbId(orderMainContext.getOrderId()) + "/" + orderMainContext.getOrderId() + "/createOrder" ,orderMainContext ,ProcessResult.class);
		return result;
	}
	
	
	/**
	 * 向订单中心请求上下文数据
	 * @param category
	 * @param dbId
	 * @param orderId
	 * @param keys
	 * @return
	 */
	public ProcessResult getContextData(String category,String dbId,String orderId,List<String> keys)
	{
		
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		jsonRequest.setJsonString(JsonUtil.toJson(keys));
		result  = restTemplate.postForObject(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/getContextData" ,jsonRequest ,ProcessResult.class);
		if(result.getRetCode() == RESULT_Success)
		{
			String jsonStr = (String)result.getResponseInfo();
			Map<String, String> retMap = JsonUtil.fromJson((String)result.getResponseInfo(),  
	                new TypeToken<Map<String, String>>() {  
	                }.getType());
			
			
			result.setResponseInfo(retMap);
		}	
		return result;
		
	}
	
	/**
	 * 从订单中心获取上下文
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @param keys
	 * @return
	 */
	protected Map<String,String> getOrderContextMap(String category, String dbId, String orderid,List<String> keys)
	{
		
		JsonRequest jsonRequest =new JsonRequest();
		jsonRequest.setJsonString(JsonUtil.toJson(keys));
		ProcessResult processResult = restTemplate.postForObject(
				orderServiceUrl + "/" + category + "/" + dbId + "/" + orderid + "/getContextData", jsonRequest,
				ProcessResult.class);
		 if(processResult.getRetCode()==RESULT_Success)
		 {
			 Map<String,String> contextMaps =
			 JsonUtil.fromJson((String)processResult.getResponseInfo(),new TypeToken<HashMap<String,String>>(){}.getType());
			
			 return contextMaps;
			 
		 }
		return null;
		
		//{category}/{dbId}/{orderId}/getContextData
		
	}
	
	public ProcessResult putContextData(String category,String dbId,String orderId,Map<String,String> maps)
	{
		
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		jsonRequest.setJsonString(JsonUtil.toJson(maps));
		OrderMainContext orderMainContext= new OrderMainContext();
		orderMainContext.setOrderId(orderId);
		orderMainContext.setContextDatas(maps);
		orderMainContext.setCatetory(category);
		System.out.println(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/putContextData");
		result  = restTemplate.postForObject(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/putContextData" ,orderMainContext ,ProcessResult.class);
		
		return result;
		
	}
	
	
	/**
	 * 用于步骤跳转
	 * @param category
	 * @param dbId
	 * @param orderId
	 * @param stepId
	 * @param flowId
	 * @param currentStatus
	 * @param retCode
	 * @return
	 */
	public ProcessResult manualJumpToNextStep(String category,String dbId,String orderId,String stepId,String flowId, int currentStatus,String retCode)
	{
		OrderFlow orderFlow = new OrderFlow();
	
		orderFlow.setOrderId(orderId);
		orderFlow.setStepId(stepId);
		orderFlow.setCurrentStatus(currentStatus);
		orderFlow.setRetCode(retCode);
		ProcessResult result = null;
		result  = restTemplate.postForObject(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/mJumpToNext" ,orderFlow ,ProcessResult.class);
		return result;
	}
	
	

}
