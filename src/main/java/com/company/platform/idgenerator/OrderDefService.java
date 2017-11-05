package com.company.platform.idgenerator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlowDef;
import com.xinwei.orderDb.domain.OrderFlowStepdef;


public class OrderDefService {
	
	private Map<String,ProcessResult> orderDefList = new ConcurrentHashMap<String,ProcessResult>();
	
	@Autowired
	private  RestTemplate template;
	/**
	 * 订单定义表的数据库ID
	 */
	
	private String httpOrderDbDefUrl;
	
	
	/**
	 * 流程定义key前缀
	 */
	private String Prefix_key_OrderDef = "orderDef:1.0";
	/**
	 * 流程定义步骤Key前缀
	 */
	private String Prefix_key_OrderStepDef = "orderStepDef:1.0";
	
	/**
	 * map中对象过期时间
	 */
	private final long  MillisPerDay = (24*3600000);
	
	
	/**
	 * 判断cache是否过期
	 * @return
	 */
	protected boolean isCacheExpired(ProcessResult processResult)
	{
		int createDays =processResult.getRetCode();
		int nowDay = (int)(System.currentTimeMillis()/(MillisPerDay));
		if(nowDay-createDays>1)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 获取订单步骤关键字
	 * @param category
	 * @return
	 */
	protected String getOrderDefKey(String category)
	{
		return Prefix_key_OrderDef + ":" + category;
	}
	
	/**
	 * 从cache中获取流程定义
	 * @param category
	 * @return
	 */
	private OrderFlowDef getOrderDefFromCache(String category)
	{
		ProcessResult processResult =null;
		try {
			String key = getOrderDefKey(category);
			OrderFlowDef orderFlowDef=null;
			if(orderDefList.containsKey(key))
			{
				
				processResult=this.orderDefList.get(key);
				if(!this.isCacheExpired(processResult))
				{
					orderFlowDef =(OrderFlowDef)processResult.getResponseInfo(); 
					return orderFlowDef;
				}
			}
			//orderFlowDef  =(OrderFlowDef)redisTemplate.opsForValue().get(key);
			return orderFlowDef;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 将流程定义保存到cache
	 * @param category
	 * @param orderFlowDef
	 * @return
	 */
	public int putOrderDefToCache(String category,OrderFlowDef orderFlowDef)
	{
		try {
			
			String key = getOrderDefKey(category);
			//放入本地缓存
			ProcessResult processResult = new ProcessResult();
			processResult.setRetCode((int)(System.currentTimeMillis()/(this.MillisPerDay)));
			processResult.setResponseInfo(orderFlowDef);
			orderDefList.put(key, processResult);
			//放入redis，不能放入redis，因为需要在web侧进行路由，不同的ownerkey路由给不同的应用服务器，以支持不同的orderIDDef；
			//redisTemplate.opsForValue().set(key, orderFlowDef,7,TimeUnit.DAYS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IdGeneratorConst.RESULT_Success;
	}
	
	
	/**
	 * 获取流程定义步骤关键字
	 * @param category
	 * @return
	 */
	protected String getOrderStepDefKey(String category,String step)
	{
		return Prefix_key_OrderStepDef + ":" + category + ":" + step; 
	}
	
	protected String getOrderAllStepDefKey(String category)
	{
		return getOrderStepDefKey(category,"__@#$"); 
	}
	
	/**
	 * 从cache中获取流程步骤定义
	 * @param category
	 * @return
	 */
	private List<OrderFlowStepdef> getOrderStepDefFromCache(String category)
	{
		ProcessResult processResult=null;
		try {
			String key = getOrderAllStepDefKey(category);
			if(orderDefList.containsKey(key))
			{
				
				processResult=this.orderDefList.get(key);
				if(!this.isCacheExpired(processResult))
				{
					List<OrderFlowStepdef> lists =(List<OrderFlowStepdef>)processResult.getResponseInfo(); 
					return lists;
				}
			}
			//String jsonStr  =(String)redisTemplate.opsForValue().get(key);
			//List<OrderFlowStepdef> orderFlowStepdefs = JsonUtil.fromJson(jsonStr,new TypeToken<List<OrderFlowStepdef>>(){}.getType()); 
			//return orderFlowStepdefs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private OrderFlowStepdef getOrderStepDefFromCache(String category,String stepId)
	{
		ProcessResult processResult=null;
		try {
			String key = getOrderStepDefKey(category,stepId);
			if(orderDefList.containsKey(key))
			{
				
				processResult=this.orderDefList.get(key);
				if(!this.isCacheExpired(processResult))
				{
					OrderFlowStepdef orderFlowStepdef =(OrderFlowStepdef)processResult.getResponseInfo(); 
					return orderFlowStepdef;
				}
			}
			//String jsonStr  =(String)redisTemplate.opsForValue().get(key);
			//List<OrderFlowStepdef> orderFlowStepdefs = JsonUtil.fromJson(jsonStr,new TypeToken<List<OrderFlowStepdef>>(){}.getType()); 
			//return orderFlowStepdefs;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将流程步骤定义放入cache
	 * @param category
	 * @param orderFlowStepdef
	 * @return
	 */
	public int putOrderSetpDefToCache(String category,List<OrderFlowStepdef> orderFlowStepdefs)
	{
		try {
			String key = getOrderAllStepDefKey(category);
			String listStr = JsonUtil.toJson(orderFlowStepdefs);
			long nowTime =System.currentTimeMillis();
			//放入本地缓存
			ProcessResult processResult = new ProcessResult();
			processResult.setRetCode((int)(nowTime/(this.MillisPerDay)));
			processResult.setResponseInfo(orderFlowStepdefs);
			orderDefList.put(key, processResult);
			
			for(int i=0;i<orderFlowStepdefs.size();i++)
			{
				OrderFlowStepdef orderFlowStepdef=orderFlowStepdefs.get(i);
				orderFlowStepdef.setTaskOutDefault(orderFlowStepdef.getTaskOutDefault());
				orderFlowStepdef.setTaskOutError(orderFlowStepdef.getTaskOutError());
				orderFlowStepdef.setTaskOutSucc(orderFlowStepdef.getTaskOutSucc());
				key = getOrderStepDefKey(category,orderFlowStepdef.getStepId());
				processResult = new ProcessResult();
				processResult.setRetCode((int)(nowTime/(this.MillisPerDay)));
				processResult.setResponseInfo(orderFlowStepdef);
				orderDefList.put(key, processResult);
				
			}
			
			//放入redis，不能放入redis，因为需要在web侧进行路由，不同的ownerkey路由给不同的应用服务器，以支持不同的orderIDDef；
			
			//redisTemplate.opsForValue().set(key, listStr,7,TimeUnit.DAYS);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return IdGeneratorConst.RESULT_Success;
	}
	
	
	/**
	 * 返回默认的错误
	 * @return
	 */
	public ProcessResult returnDefaultError()
	{
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(IdGeneratorConst.RESULT_Error_Fail);
		return processResult;
	}
	/**
	 * 从数据库中获取订单定义
	 * @param category
	 * @return -- OrderFlowDef
	 */
	public  ProcessResult selectOrderDef(String category,String ownerKey)
	{
		try {
			ProcessResult result = new ProcessResult();
			result  = template.getForObject(httpOrderDbDefUrl + "/" +  category+ "/" + ownerKey + "/getOrderDef" , ProcessResult.class);
			if(result.getRetCode() == IdGeneratorConst.RESULT_Success)
			{
				String jsonStr = (String)result.getResponseInfo();
				result.setResponseInfo(JsonUtil.fromJson(jsonStr, OrderFlowDef.class));
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return returnDefaultError();
		}
	}
	/**
	 * 从数据库中获取订单步骤定义
	 * @param category
	 * @return -- OrderFlowStepdef
	 */
	public  ProcessResult selectFlowStepDef(String category,String ownerKey)
	{
		try {
			ProcessResult result = new ProcessResult();
			if(StringUtils.isEmpty(ownerKey))
			{
				ownerKey = "default";
			}
			result  = template.getForObject(httpOrderDbDefUrl + "/" + category + "/" +ownerKey+ "/getOrderStepDef"  , ProcessResult.class);
			if(result.getRetCode()==IdGeneratorConst.RESULT_Success)
			{
				List<OrderFlowStepdef> lists  =JsonUtil.fromJson((String)result.getResponseInfo(),new TypeToken<List<OrderFlowStepdef>>(){}.getType());
				result.setResponseInfo(lists);
			}
			
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return returnDefaultError();
		}
	}
	
	/**
	 * 获取订单定义
	 * @param category
	 * @return
	 */
	public OrderFlowDef getOrderDef(String category,String ownerKey)
	{
		
		OrderFlowDef orderFlowDef=null;
		try {
			orderFlowDef = getOrderDefFromCache(category);
			if(orderFlowDef==null)
			{
				ProcessResult ProcessResult = selectOrderDef(category,ownerKey);
				if(ProcessResult.getRetCode()==IdGeneratorConst.RESULT_Success)
				{
					orderFlowDef = (OrderFlowDef)ProcessResult.getResponseInfo();
					putOrderDefToCache(category, orderFlowDef);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return orderFlowDef;
	}
	
	
	
	/**
	 * 获取订单步骤定义
	 * @param category
	 * @return
	 */
	public List<OrderFlowStepdef> getOrderStepDef(String category,String ownerKey)
	{
		List<OrderFlowStepdef> lists= getOrderStepDefFromCache(category);
		if(lists==null)
		{
			ProcessResult ProcessResult = selectFlowStepDef(category,ownerKey);
			if(ProcessResult.getRetCode()==IdGeneratorConst.RESULT_Success)
			{
				lists = (List<OrderFlowStepdef>)ProcessResult.getResponseInfo();
				putOrderSetpDefToCache(category, lists);
			}
		
		}
		return lists;
	}
	
	public OrderFlowStepdef getOrderStepDef(String category,String stepId,String ownerKey)
	{
		OrderFlowStepdef orderFlowStepdef= getOrderStepDefFromCache(category,stepId);
		if(orderFlowStepdef==null)
		{
			ProcessResult ProcessResult = selectFlowStepDef(category,ownerKey);
			if(ProcessResult.getRetCode()==IdGeneratorConst.RESULT_Success)
			{
				List<OrderFlowStepdef> lists = (List<OrderFlowStepdef>)ProcessResult.getResponseInfo();
				
				putOrderSetpDefToCache(category, lists);
				 orderFlowStepdef= getOrderStepDefFromCache(category,stepId);
					
			}
		
		}
		return orderFlowStepdef;
	}
	public String getHttpOrderDbDefUrl() {
		return httpOrderDbDefUrl;
	}
	public void setHttpOrderDbDefUrl(String httpOrderDbDefUrl) {
		this.httpOrderDbDefUrl = httpOrderDbDefUrl;
	}
	
}
