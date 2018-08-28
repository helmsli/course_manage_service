package com.company.platform.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.company.courseManager.teacher.domain.UserOrderQueryResult;
import com.company.userOrder.domain.QueryUserOrderRequest;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.Courses;
import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlow;
import com.xinwei.orderDb.domain.OrderMain;
import com.xinwei.orderDb.domain.OrderMainContext;

public class OrderClientService {
	
	protected int RESULT_Success = 0;
	
	
	@Value("${order.orderServiceUrl}")
	private String orderServiceUrl;
	
	@Value("${order.orderIdUrl}")
	private String orderIdUrl;
	
	@Value("${order.userOrderServiceUrl}")
	private String userOrderServiceUrl;
	
	
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
		if(StringUtils.isEmpty(orderMainContext.getOrderId()))
		{
			String orderid = this.getOrderId(orderMainContext.getCatetory(), orderMainContext.getOwnerKey());
			if(StringUtils.isEmpty(orderid))
			{
				ProcessResult ret = new ProcessResult();
				ret.setRetCode(-1);
				ret.setRetMsg("orderid error");
				return ret;
			}
			orderMainContext.setOrderId(orderid);
		}
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
	 * 
	 * @param category
	 * @param ownerKey
	 * @return null -- if no orderId return;
	 */
	public String getOrderId(String category, String ownerKey) {
		String orderId = null;
		JsonRequest jsonRequest = new JsonRequest();
		ProcessResult processResult = restTemplate.postForObject(
				orderIdUrl + "/" + category + "/" + ownerKey + "/createOrderId", jsonRequest, ProcessResult.class);
		if (processResult.getRetCode() == 0) {
			orderId = (String) processResult.getResponseInfo();
		}
		return orderId;


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
		if(dbId==null)
		{
			dbId = OrderMainContext.getDbId(orderid);
			
		}
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
		if(dbId==null)
		{
			dbId = OrderMainContext.getDbId(orderId);
			
		}
		jsonRequest.setJsonString(JsonUtil.toJson(maps));
		OrderMainContext orderMainContext= new OrderMainContext();
		orderMainContext.setOrderId(orderId);
		orderMainContext.setContextDatas(maps);
		orderMainContext.setCatetory(category);
		System.out.println(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/putContextData");
		result  = restTemplate.postForObject(orderServiceUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/putContextData" ,orderMainContext ,ProcessResult.class);
		
		return result;
		
	}
	
	public ProcessResult getOrder(String category, String orderId) {
		String dbId = OrderMainContext.getDbId(orderId);
		ProcessResult processResult = restTemplate.postForObject(orderServiceUrl + "/" + category + "/" + dbId + "/" + orderId + "/getOrder", 
				null, ProcessResult.class);
		if (processResult.getRetCode() == RESULT_Success) {
			processResult.setResponseInfo(JsonUtil.fromJson((String) processResult.getResponseInfo(), OrderMain.class));
		}
		return processResult;
	}
	public ProcessResult manualJumpToNextStep(String category, String orderId, String retCode) {
		// 获取orderMain
		ProcessResult processResult = this.getOrder(category, orderId);
		if (processResult.getRetCode() != RESULT_Success) {
			return processResult;
		}
		String dbId = OrderMainContext.getDbId(orderId);
		OrderMain orderMain = (OrderMain) processResult.getResponseInfo();
		OrderFlow orderFlow = new OrderFlow();
		orderFlow.setOrderId(orderId);
		orderFlow.setFlowId(orderMain.getFlowId());
		orderFlow.setStepId(orderMain.getCurrentStep());
		orderFlow.setCurrentStatus(orderMain.getCurrentStatus());
		orderFlow.setRetCode(String.valueOf(retCode));
		processResult = manualJumpToNextStep(category, dbId, orderId, orderMain.getCurrentStep(), orderMain.getFlowId(), orderMain.getCurrentStatus(), retCode);
		return processResult;
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
	/**
	 * 发布数据到用户中心
	 * @param userDbWriteUrl
	 * @param userOrder
	 * @return
	 */
	public ProcessResult saveUserOrder(String userDbWriteUrl,UserOrder userOrder)
	{
		
		ProcessResult result = null;
		String requestUrl = userDbWriteUrl;
		if(StringUtils.isEmpty(userDbWriteUrl))
		{
			requestUrl= this.userOrderServiceUrl;
		}
		result  = restTemplate.postForObject(requestUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/configUserOrder" ,userOrder ,ProcessResult.class);
		return result;
	}
	/**
	 * 对userorder的amount增加
	 * @param userDbWriteUrl
	 * @param userOrder
	 * @return
	 */
	public ProcessResult plusUserOrderAmount(String userDbWriteUrl,UserOrder userOrder)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(userDbWriteUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/plusUserStatus" ,userOrder ,ProcessResult.class);
		return result;
	}
	
	public ProcessResult updateUserOrderStatus(String userDbWriteUrl,UserOrder userOrder)
	{
		
		ProcessResult result = null;
		String requestUrl = userDbWriteUrl;
		if(StringUtils.isEmpty(userDbWriteUrl))
		{
			requestUrl= this.userOrderServiceUrl;
		}
		result  = restTemplate.postForObject(requestUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/updateUserOrderStatus" ,userOrder ,ProcessResult.class);
		return result;
	}
	
	public ProcessResult queryOneOrder(String userDbWriteUrl,UserOrder userOrder)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(userDbWriteUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/queryOneOrder" ,userOrder ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			UserOrder retUserOrder = JsonUtil.fromJson((String)result.getResponseInfo(), UserOrder.class);
			result.setResponseInfo(retUserOrder);
		}
		return result;
	}
	
	public ProcessResult queryOrdersByUserId(String userDbWriteUrl,QueryUserOrderRequest queryUserOrderRequest)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(userDbWriteUrl + "/" +  queryUserOrderRequest.getCategory()+ "/" + queryUserOrderRequest.getUserId() + "/queryUserOrder" ,queryUserOrderRequest ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			String lsUserOrder = JsonUtil.toJson(result.getResponseInfo());
			UserOrderQueryResult userOrderQueryResult = JsonUtil.fromJson(lsUserOrder, UserOrderQueryResult.class);
			List<UserOrder> lists = userOrderQueryResult.getList();
			result.setResponseInfo(lists);
		}
		return result;
	}
	/**
	 * 带分页信息
	 * @param userDbWriteUrl
	 * @param queryUserOrderRequest
	 * @return
	 */
	public ProcessResult queryAllOrderReturnPage(String userDbWriteUrl,QueryUserOrderRequest queryUserOrderRequest)
	{
		
		ProcessResult result = null;
		String queryUrl = userDbWriteUrl;
		if(queryUrl==null)
		{
			queryUrl = this.userOrderServiceUrl;
		}
		result  = restTemplate.postForObject(queryUrl + "/" +  queryUserOrderRequest.getCategory()+ "/" + queryUserOrderRequest.getUserId() + "/queryUserOrder" ,queryUserOrderRequest ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			String lsUserOrder = JsonUtil.toJson(result.getResponseInfo());
			UserOrderQueryResult userOrderQueryResult = JsonUtil.fromJson(lsUserOrder, UserOrderQueryResult.class);
			result.setResponseInfo(userOrderQueryResult);
		}
		return result;
	}
	
	public ProcessResult delOneOrder(String userDbWriteUrl,UserOrder userOrder)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(userDbWriteUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/delUserOrder" ,userOrder ,ProcessResult.class);
		
		return result;
	}
	
	public ProcessResult startOrder(String category,String orderId)
	{
		
		ProcessResult result = null;
		String dbid = OrderMainContext.getDbId(orderId);
		//System.out.println("**************"+this.orderServiceUrl + "/" + category + "/" + orderId + "/startOrder");
		result  = restTemplate.getForObject(this.orderServiceUrl + "/" + category + "/" + "/" + dbid + "/" +orderId + "/startOrder" ,ProcessResult.class);
		return result;
	}
	/**
	 * 用于保存生成的需要付款信息的订单KEY
	 * @return
	 */
	public String getOrderWillPayKey()
	{
		return "willPayment";
	}
	/**
	 * 用户保存付款成功后的订单中的key
	 * @return
	 */
	public String getOrderSuccessPayKey()
	{
		return "succPayment";
	}
	/**
	 * 
	 * @return
	 */
	public String getOrderPayInfoKey()
	{
		return "infoPayment";
	}

}
