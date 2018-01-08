package com.company.videoPlay.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.domain.MyPlayClassInfo;
import com.company.videoPlay.service.MyPlayClassService;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
@Service("myPlayClassService")
public class MyPlayClassServiceImpl extends OrderClientService implements MyPlayClassService {

	@Resource (name = "redisTemplate")
	protected RedisTemplate<Object, Object> redisTemplate;
	
	@Value("${student.myplaytask.cache.expireHours:240}")
	private int myplayClassCacheExpireHours;

	
	@Value("${student.myplaytask.dbWriteUrl}")
	private String myPlayClassDbWriteUrl;

	@Override
	public int updateMyPlayClass(AliVodPlayInfo aliVodPlayInfo) {
		// TODO Auto-generated method stub
		boolean isNeedSave = false;
		try {
			MyPlayClassInfo myPlayClassInfo = new MyPlayClassInfo();
			myPlayClassInfo.setCategory(aliVodPlayInfo.getCategory());
			myPlayClassInfo.setClassId(aliVodPlayInfo.getClassId());
			myPlayClassInfo.setCourseId(aliVodPlayInfo.getCourseId());
			myPlayClassInfo.setHavePlayedSeconds(aliVodPlayInfo.getHavePlayedSeconds());
			myPlayClassInfo.setUserId(aliVodPlayInfo.getUserId());
			MyPlayClassInfo oldPlayClassInfo =(MyPlayClassInfo)this.redisTemplate.opsForValue().get(this.getMyPlayTaskRedisKey(aliVodPlayInfo));
			if(oldPlayClassInfo==null)
			{
				isNeedSave = true;
			}
			else
			{
				if(oldPlayClassInfo.getUpdateDbTime() - myPlayClassInfo.getUpdateDbTime()>300000)
				{
					isNeedSave = true;
				}
			}
			this.redisTemplate.opsForValue().set(this.getMyPlayTaskRedisKey(aliVodPlayInfo), myPlayClassInfo,myplayClassCacheExpireHours,TimeUnit.HOURS);;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isNeedSave)
		{
			UserOrder userOrder = new UserOrder();
			userOrder.setCategory(this.getMyPlayTaskCategory());
			userOrder.setConstCreateTime();
			userOrder.setOrderData(JsonUtil.toJson(aliVodPlayInfo));
			userOrder.setUserId(aliVodPlayInfo.getUserId());
			userOrder.setOrderId(aliVodPlayInfo.getCourseId());
			this.saveUserOrder(myPlayClassDbWriteUrl, userOrder);
		}
		return 0;
	}

	@Override
	public AliVodPlayInfo queryMyPlayClass(AliVodPlayInfo aliVodPlayInfo) {
		// TODO Auto-generated method stub
		AliVodPlayInfo getAliVodPlayInfo=aliVodPlayInfo;
		try {
			MyPlayClassInfo myPlayClassInfo = (MyPlayClassInfo)this.redisTemplate.opsForValue().get(this.getMyPlayTaskRedisKey(aliVodPlayInfo));
		
			
			getAliVodPlayInfo.setCategory(myPlayClassInfo.getCategory());
			getAliVodPlayInfo.setClassId(myPlayClassInfo.getClassId());
			getAliVodPlayInfo.setCourseId(myPlayClassInfo.getCourseId());
			getAliVodPlayInfo.setHavePlayedSeconds(myPlayClassInfo.getHavePlayedSeconds());
			getAliVodPlayInfo.setUserId(myPlayClassInfo.getUserId());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(getAliVodPlayInfo==null)
		{
			/*
			UserOrder userOrder = new UserOrder();
			
			userOrder.setCategory(this.getMyPlayTaskCategory());
			userOrder.setConstCreateTime();
			userOrder.setOrderData(JsonUtil.toJson(aliVodPlayInfo));
			userOrder.setUserId(aliVodPlayInfo.getUserId());
			userOrder.setOrderId(this.getMyPlayTaskOrderId());
			
			ProcessResult result = this.queryOneOrder(myPlayClassDbWriteUrl, userOrder);
			if(result.getRetCode()==0)
			{
				userOrder = (UserOrder)result.getResponseInfo();
				return JsonUtil.fromJson(userOrder.getOrderData(),AliVodPlayInfo.class);
			}
			*/
		}
		return getAliVodPlayInfo;
	}

	public static String getMyPlayTaskRedisKey(AliVodPlayInfo aliVodPlayInfo)
	{
		return "myplay:" + aliVodPlayInfo.getUserId() + ":" + aliVodPlayInfo.getCourseId() ;
	}
	public static String getMyPlayTaskCategory()
	{
		return "myPlayClass";
	}
	public static String getMyPlayTaskOrderId()
	{
		return "000000";
	}
}
