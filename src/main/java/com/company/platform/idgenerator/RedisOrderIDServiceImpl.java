package com.company.platform.idgenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderFlowDef;

@Service("redisOrderIdService")
public class RedisOrderIDServiceImpl {
	
	//default:24*3600000
	@Value("${order.orderIdDef.expireMillis:86400000}")
	private long  MillisPerDay;
	
	
	
	
	private Map<String,ProcessResult> orderDefList = new ConcurrentHashMap<String,ProcessResult>();
	
	
	/**
	 * 订单ID定义的key
	 */
	private String Prefix_key_OrderIdDef = "orderIdDef:1.0";
	
	
	/**
	 * orderid的生成规则
	 */
	private String Prefix_key_OrderIdRule = "orderIdrule:1.0";
	
	/**
	 * 已经使用的orderId的cache
	 */
	private String Prefix_key_OrderIdUsed = "orderIdUsed:1.0";
	
	private String Prefix_key_OrderDbUsed = "orderDbUsed:1.0";
	
	@Resource(name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;
	
	
	
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
	 * 订单id已经使用的cache
	 * @param category
	 * @return
	 */
	protected String getOrderIdUsedKey(String category,String orderDefId)
	{
		return Prefix_key_OrderIdUsed + ":" + category + ":" + orderDefId;
	}
	
	/**
	 * 使用的数据库DB和orderId的key
	 * @param category
	 * @param orderDefId
	 * @return
	 */
	protected String getOrderDbUsedKey(String category,String orderDefId)
	{
		
		return Prefix_key_OrderDbUsed + ":" + category + ":" + orderDefId;
	}
	
	/**
	 * 对应的category的生成规则
	 * @param category
	 * @return
	 */
	protected String getOrderIdRuleKey(String category)
	{
		return Prefix_key_OrderIdRule + ":" + category;
	}
	
	
	
	
	/**
	 * 用于生成订单ID
	 * @param category
	 * @return
	 */
	protected String getOrderIdDefKey(String category)
	{
		return Prefix_key_OrderIdDef + ":" + category;
	}
	
	public int putOrderIdRuleToCache(String category,OrderIDDef orderIDDef)
	{
		String key = getOrderIdRuleKey(category);
		redisTemplate.opsForValue().set(key, orderIDDef);
		return 0;
		
	}
	
	
	
	public int putOrderIdDefToCache(String category,OrderFlowDef orderFlowDef)
	{
		try {
			
			String key = getOrderIdDefKey(category);
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
	 * 订单ID定义的对象；
	 * @param category
	 * @return
	 */
	public OrderFlowDef getOrderIdDefFromCache(String category)
	{
		ProcessResult processResult =null;
		try {
			String key = getOrderIdDefKey(category);
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
	 * 从redis中创建orderid的起始编号
	 * @param category
	 * @param numbers
	 * @return
	 */
	public long createOrderIdFromRedis(String category,String orderIdDef,int numbers) {
		// TODO Auto-generated method stub
		String key = this.getOrderIdUsedKey(category,orderIdDef);
		ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
		Long retValue= opsForValue.increment(key, numbers);
		return retValue.longValue();
	}
	
	/**
	 * 从redis中获取分区的定义
	 * @param category
	 * @param orderIdDef
	 * @return
	 */
	protected OrderIDDef getDbIdFromRedis(String category,String orderIdDef) {
		// TODO Auto-generated method stub
		String key = this.getOrderDbUsedKey(category,orderIdDef);
		ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
		OrderIDDef orderIDDef = (OrderIDDef)opsForValue.get(key);
		return orderIDDef;
	}
	/**
	 * 将分区的定义放入到redis中
	 * @param category
	 * @param orderIdDef
	 * @param orderIDDef
	 */
	protected void putDbIdToRedis(String category,String orderIdDef,OrderIDDef orderIDDef) {
		// TODO Auto-generated method stub
		String key = this.getOrderDbUsedKey(category,orderIdDef);
		ValueOperations<Object, Object> opsForValue = redisTemplate.opsForValue();
		opsForValue.set(key, orderIDDef);
		return ;
	}
	 	
}
