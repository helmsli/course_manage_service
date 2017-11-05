package com.company.platform.idgenerator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderFlowDef;
@Component("orderIDService")
@ConditionalOnProperty(name = "order.idService.enable")
//@Service("orderIDService")
public class OrderIDServiceImpl extends OrderDefService implements IDGeneratorService,InitializingBean {
	
	@Resource(name="redisOrderIdService")
	private RedisOrderIDServiceImpl redisOrderIdService;
	/**
	 * 定义创建订单ID的数据库url
	 */
	@Value("${order.createIdDbUrl}")  
	private String orderCreateIdDbUrl;
	
	/**
	 * 保存本地的cache信息
	 */
	private Map<String,OrderIdCache> orderIdCacheMap = new ConcurrentHashMap<String,OrderIdCache>();

	
	
	/**
	 * 本地初始化的orderId 前缀，如果cache出错，会使用这个变量
	 */
	@Value("${order.createIdPrefix:000A}")  
	private String localOrderIdPrefix;
	
	
	
	
	
	
	
	@Override
	public ProcessResult createId(String category, String ownerKey, JsonRequest jsonRequest) {
		// TODO Auto-generated method stub
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(IdGeneratorConst.RESULT_Error_Fail);
		if(ownerKey==null)
		{
			processResult.setRetCode(IdGeneratorConst.RESULT_ERROR_ownerIdKeyNull);
			return processResult;
		}
		OrderFlowDef orderFlowDef= this.getOrderIdDef(category,ownerKey);
		if(orderFlowDef==null)
		{
			processResult.setRetCode(IdGeneratorConst.RESULT_ERROR_OrderNotExist);
			return processResult;
		}
		OrderIDDef orderIDDef = JsonUtil.fromJson(orderFlowDef.getOrderidCategory(),OrderIDDef.class);  
		
		/**
		 * 获取分区ID和数据库ID
		 */
		/*
		 * 1.从redis获取当前orderid使用的数据库ID和分区ID
		 * 2.获取当前定义ID对应的ID；
		 */
		String orderId = this.createFullOrderId(category, orderIDDef);
		processResult.setResponseInfo(orderId);
		processResult.setRetCode(IdGeneratorConst.RESULT_Success);
		return processResult;
	}
	
	
	/**
	 * 创建带数据库ID和分区ID的orderID，格式为 xxxxxxxSpartitionIdDbid 
	 * @param newId
	 * @param orderIDDef
	 * @return
	 */
	public synchronized String createFullOrderId(String category,OrderIDDef orderIDDef)
	{
		/**
		 * 如果没有剩余的id，从数据库中获取orderID；
		 * 
		 */
		
		//获取分区ID
		boolean isCreateFromLocal = false;
		//从redis中获取全局的订单ID，不包括分区ID和数据库ID
		long lNewOrderId = createGOrderIdPrefix(category,orderIDDef.getDefId());
		if(lNewOrderId==0)
		{
			isCreateFromLocal = true;
			//获取本地的ID，是毫秒数累加，，不包括分区ID和数据库ID
			lNewOrderId = this.createLocalOrderIdPrefix(category);
		}
		//构造分区ID
		int partitionMod = orderIDDef.getPartitionNum();
		int partitionId = (int)(lNewOrderId%partitionMod) + orderIDDef.getPartitionStartId();
		String spartitionId = String.valueOf(partitionId); 
		
		if(spartitionId.length()!=orderIDDef.getPartitionIdLength())
		{
			if(spartitionId.length()>orderIDDef.getPartitionIdLength())
			{
				spartitionId = spartitionId.substring(0, orderIDDef.getPartitionIdLength());
			}
			else
			{
				while(spartitionId.length()<orderIDDef.getPartitionIdLength())
				{
					spartitionId = "0" + spartitionId;
				}
			}
		}
		//根据分区ID重构订单ID
		long retNewId = lNewOrderId/(partitionMod);
		String sRetNewOrderId = "";
		
		if(isCreateFromLocal)
		{
			//订单id+本地前缀+分区ID+dbID
			sRetNewOrderId = String.valueOf(retNewId) + localOrderIdPrefix + spartitionId + orderIDDef.getDbId(); 
			
		}
		else
		{
			//订单id+分区ID+dbID
			sRetNewOrderId = String.valueOf(retNewId) + spartitionId + orderIDDef.getDbId(); 
			
		}
		
		return sRetNewOrderId;
	}
	
	
	/**
	 * 创建orderId的前缀，不带数据库ID和分区ID
	 * @param category
	 * @return
	 */
	protected synchronized long createGOrderIdPrefix(String category,String orderIDDef)
	{
		long newOrderId = 0;
		OrderIdCache orderIdCache= null;
		if(orderIdCacheMap.containsKey(category))
		{
			orderIdCache = orderIdCacheMap.get(category);
		}
		else
		{
			//创建对应的生成orderid的规则，并放入map中
			orderIdCache = new OrderIdCache();
			orderIdCache.setRemainOrderidNumber(0);
			orderIdCache.setStartNewOrderId(0);
			orderIdCache.setLocalNewOrderId(0);
			orderIdCacheMap.put(category, orderIdCache);
			
		}
		int remainOrderidNumber = orderIdCache.getRemainOrderidNumber();
		long startNewOrderId = orderIdCache.getStartNewOrderId();
		//如果本地还剩余orderid，直接使用
		if(remainOrderidNumber>0)
		{
			newOrderId= ++startNewOrderId;
			remainOrderidNumber--;
			orderIdCache.setRemainOrderidNumber(remainOrderidNumber);;
			orderIdCache.setStartNewOrderId(startNewOrderId);
			return newOrderId;
		}
		else
		{
			//从缓存中申请orderid，每次申请1000个
			int queryNums = 1000;
			try {
				startNewOrderId = redisOrderIdService.createOrderIdFromRedis(category, orderIDDef,queryNums);
				//从缓存中申请成功
				if(startNewOrderId>0)
				{
					newOrderId=startNewOrderId - queryNums;
					startNewOrderId=startNewOrderId - queryNums + 1;
					remainOrderidNumber=queryNums - 1;
					
					orderIdCache.setRemainOrderidNumber(remainOrderidNumber);;
					orderIdCache.setStartNewOrderId(startNewOrderId);
					
				}	
				//从缓存中申请失败，在本地申请一个
				else
				{
					newOrderId=0;
					//return 0;
					//return createLocalOrderIdPrefix();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//如果从缓存中申请失败，在本地申请一个
						
			}
			
		}
		return newOrderId;
	}
	
	/**
	 * 如果cache出错，创建本地的orderid，不带本地节点号和分区ID和数据库ID
	 * @return
	 */
	protected synchronized long createLocalOrderIdPrefix(String category)
	{
		/**
		 * long 最大19为，因此如果是自己生成的UID
		 * 系统生成的最大位数为11为，本地生成的最大为14
		 * 本地生成的id为注入的前缀左移11位，在加上当前时间的毫秒数。
		 * 1年的毫秒数 31536000 
		 */
		OrderIdCache orderIdCache= null;
		if(orderIdCacheMap.containsKey(category))
		{
			orderIdCache = orderIdCacheMap.get(category);
		}
		else
		{
			//创建对应的生成orderid的规则，并放入map中
			orderIdCache = new OrderIdCache();
			orderIdCache.setRemainOrderidNumber(0);
			orderIdCache.setStartNewOrderId(0);
			orderIdCache.setLocalNewOrderId(0);
			orderIdCacheMap.put(category, orderIdCache);
			
		}
		//获取1970年以来的秒数，14位   3153600000000  1504369881000
		long localNewOrderId = orderIdCache.getLocalNewOrderId();
		if(localNewOrderId==0)
		{
			//当前时间减去1970年的基准时间
			localNewOrderId =  System.currentTimeMillis()-1504369881000l;
			orderIdCache.setLocalNewOrderId(localNewOrderId);
		}
		//前缀时间+本地开始uid+偏移量
		//return String.valueOf(localNewOrderId++)+this.localOrderIdPrefix;
		localNewOrderId++;
		orderIdCache.setLocalNewOrderId(localNewOrderId);
		return orderIdCache.getLocalNewOrderId();
		
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		setHttpOrderDbDefUrl(this.orderCreateIdDbUrl);
	}
	
	/**
	 * 用于订单ID的定义，注意订单ID定义和订单流程定义是分开的
	 * @param category
	 * @param ownerKey
	 * @return
	 */
	public OrderFlowDef getOrderIdDef(String category,String ownerKey)
	{
		
		OrderFlowDef orderFlowDef=null;
		try {
			
			orderFlowDef = redisOrderIdService.getOrderIdDefFromCache(category);
			if(orderFlowDef==null)
			{
				ProcessResult ProcessResult = selectOrderDef(category,ownerKey);
				if(ProcessResult.getRetCode()==IdGeneratorConst.RESULT_Success)
				{
					orderFlowDef = (OrderFlowDef)ProcessResult.getResponseInfo();
					redisOrderIdService.putOrderIdDefToCache(category, orderFlowDef);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return orderFlowDef;
	}
}
