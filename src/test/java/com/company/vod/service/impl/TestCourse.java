package com.company.vod.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;
import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.xinwei.orderDb.domain.OrderMainContext;

public class TestCourse {
	private Logger log = LoggerFactory.getLogger(getClass());
	  
	protected int RESULT_Success = 0;
	
	private String category = "tcoursepub";
	private String ownerKey = "1234567890";
	private String orderId = "1234567890";
	public OrderClientService orderClientService = new OrderClientService();
	private RestTemplate restTemplate = new RestTemplate();
	private Courses gCourses = null;
	List<CourseClass> lists=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestCourse testCourse = new TestCourse();
		//testCourse.getOrderId();
		testCourse.createCourse();
		testCourse.getCourse();
		testCourse.publishCourse();
		testCourse.publishClass();
		testCourse.configUserOrder();
	}
	
	public void getOrderId()
	{
	   String baseUrl = "http://127.0.0.1:9088/orderId";
	   ///{category}/{ownerKey}/createOrderId
	   JsonRequest jsonRequest =new JsonRequest();
		System.out.println(baseUrl + "/" + category + "/" + ownerKey +  "/createOrderId");
		ProcessResult processResult = restTemplate.postForObject(
				baseUrl + "/" + category + "/" + ownerKey +  "/createOrderId", jsonRequest,
				ProcessResult.class);
		 if(processResult.getRetCode()==0)
		 {
			 orderId  = (String)processResult.getResponseInfo();
		 }
		 System.out.println(processResult);
		
	}
	
	public void createCourse()
	{
		String baseUrl = "http://127.0.0.1:9088/orderGateway";
		this.orderId="3001030001";	 
		orderClientService.setRestTemplate(restTemplate);
		orderClientService.setOrderServiceUrl(baseUrl);
		String dbId=OrderMainContext.getDbId(orderId);
		Map<String,String>maps = new HashMap<String,String>();
		Courses courses = new Courses();
		courses.setCourseId(orderId);
		courses.setRealPrice(1.12f);
		courses.setOwner(ownerKey);
		gCourses = courses;
		maps.put("course", JsonUtil.toJson(courses));
		 lists = new ArrayList<CourseClass>();
		for(int i=0;i<10;i++)
		{
			CourseClass courseClass= new CourseClass();
			courseClass.setCourseId(orderId);
			courseClass.setChapterId("1");
			courseClass.setClassId(String.valueOf(i+1));
			courseClass.setOriginalPrice(i);
			courseClass.setOwner(ownerKey);
			lists.add(courseClass);
		}
		maps.put("courseClass", JsonUtil.toJson(lists));
		System.out.println(maps.toString());
		ProcessResult processResult=orderClientService.putContextData(category, dbId, orderId, maps);		
		System.out.println(processResult);
	}


	public void getCourse()
	{
		  String baseUrl = "http://127.0.0.1:9088/orderGateway";
		this.orderId="3001030001";	 
		orderClientService.setRestTemplate(restTemplate);
		orderClientService.setOrderServiceUrl(baseUrl);
		String dbId=OrderMainContext.getDbId(orderId);
		List<String>querykeys = new ArrayList<String>();
		querykeys.add("courseClass");
		querykeys.add("course");
		ProcessResult processResult=orderClientService.getContextData(category, dbId, orderId, querykeys);
		System.out.println("getCourse:" + processResult.toString());
	}
	
	
	public void publishCourse()
	{
		String baseUrl = "http://127.0.0.1:9200/courseTeacherManager";
		String dbId=OrderMainContext.getDbId(orderId);
		this.orderId="3001030001";	 
		//{category}/{dbid}/{orderid}/confTeacherCourse
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		//jsonRequest.setJsonString(JsonUtil.toJson(keys));
		result  = restTemplate.postForObject(baseUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/confTeacherCourse" ,jsonRequest ,ProcessResult.class);
		if(result.getRetCode() == RESULT_Success)
		{
		}	
		System.out.println("publishCourse:" + result.toString());
	
	}
	public void publishClass()
	{
		String baseUrl = "http://127.0.0.1:9200/courseTeacherManager";
		String dbId=OrderMainContext.getDbId(orderId);
		this.orderId="3001030001";	 
		//{category}/{dbid}/{orderid}/confTeacherCourse
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		//jsonRequest.setJsonString(JsonUtil.toJson(keys));
		result  = restTemplate.postForObject(baseUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/confTeacherClass" ,jsonRequest ,ProcessResult.class);
		if(result.getRetCode() == RESULT_Success)
		{
		}	
		System.out.println("publishClass:" + result.toString());
	
	}
	public void configUserOrder()
	{
		//{category}/{dbid}/{orderid}/publishCourse
		String baseUrl = "http://127.0.0.1:9200/courseTeacherManager";
		String dbId=OrderMainContext.getDbId(orderId);
		this.orderId="3001030001";	 
		//{category}/{dbid}/{orderid}/confTeacherCourse
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		//jsonRequest.setJsonString(JsonUtil.toJson(keys));
		result  = restTemplate.postForObject(baseUrl + "/" +  category+ "/" + dbId + "/" + orderId + "/publishCourse" ,jsonRequest ,ProcessResult.class);
		if(result.getRetCode() == RESULT_Success)
		{
		}	
		System.out.println("publishClass:" + result.toString());
	
	}
	
	
	public void configUserOrderMaual()
	{
		//@RequestMapping(method = RequestMethod.POST, value = "{category}/{userid}/configUserOrder")
		String baseUrl = "http://127.0.0.1:9201/userOrderDb";
		String dbId=OrderMainContext.getDbId(orderId);
		this.orderId="3001030001";	 
		//{category}/{dbid}/{orderid}/confTeacherCourse
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(category);
		userOrder.setCreateTime(Calendar.getInstance().getTime());
		userOrder.setOrderData(JsonUtil.toJson(this.gCourses));
		userOrder.setOrderId(orderId);
		userOrder.setStatus(0);
		userOrder.setUpdateTime(Calendar.getInstance().getTime());
		userOrder.setUserId(ownerKey);
		//jsonRequest.setJsonString(JsonUtil.toJson(keys)); {category}/{userid}/configUserOrder
		result  = restTemplate.postForObject(baseUrl + "/" +  category+ "/" + userOrder.getUserId() + "/configUserOrder" ,userOrder ,ProcessResult.class);
		if(result.getRetCode() == RESULT_Success)
		{
		}	
		System.out.println("configUserOrder:" + result.toString());
		
	}
}
