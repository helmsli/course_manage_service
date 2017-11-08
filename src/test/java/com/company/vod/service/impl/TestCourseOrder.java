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

public class TestCourseOrder {
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
		TestCourseOrder testCourse = new TestCourseOrder();
		testCourse.getOrderId();
		//testCourse.orderId="4001000001";
		
		testCourse.createCourseOrder();
		testCourse.startOrder();
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
	
	
	
	public void createCourseOrder()
	{
		String baseUrl = "http://127.0.0.1:9088/orderGateway";
		//this.orderId="3001030001";	 
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
		OrderMainContext orderMainContext  = new OrderMainContext();
		orderMainContext.setContextDatas(maps);
		orderMainContext.setCatetory(category);
		orderMainContext.setOrderId(this.orderId);
		System.out.println(maps.toString());
		ProcessResult processResult=orderClientService.createOrder(orderMainContext);		
		System.out.println(processResult);
	}
	public void startOrder()
	{
		///{category}/{dbId}/{orderId}/startOrder
		String baseUrl = "http://127.0.0.1:9088/orderGateway";
		String dbId=OrderMainContext.getDbId(orderId);
		
		ProcessResult processResult = restTemplate.getForObject(
				baseUrl + "/" + category + "/" + dbId + "/" +orderId +    "/startOrder",
				ProcessResult.class);
		System.out.println("startOrder:" + processResult);
	}
	


	
}
