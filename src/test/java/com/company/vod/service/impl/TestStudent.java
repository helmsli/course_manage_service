package com.company.vod.service.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public class TestStudent {
	private String orderId=null;
	//private String baseUrl = "http://127.0.0.1:9200/studentCourse";
	private String baseUrl = "http://www.chunzeacademy.com:8080/studentCourse";

	private RestTemplate restTemplate = new RestTemplate();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			
		
		
		TestStudent testStudent = new TestStudent();
		testStudent.requestPay();
		
		testStudent.getPayInfo();
	}
	public void requestPay()
	{
		String userid = "234567891";
		
		getOrderId(userid);
		if(this.orderId==null)
		{
			System.out.println("error get orderid");
			return;
		}
		String requestUrl = this.baseUrl;
		//String orderId= "";
		StudentBuyOrder studentBuyOrder = new StudentBuyOrder();	
		studentBuyOrder.setUserId(userid);
		studentBuyOrder.setCourseId("4011010001");
		studentBuyOrder.setTotalRealPrice(25.0f);
		
		ProcessResult processResult = restTemplate.postForObject(
				requestUrl + "/" + userid + "/" + orderId + "/submitBuyOrder", studentBuyOrder,
				ProcessResult.class);
		 if(processResult.getRetCode()==0)
		 {
			 
		 }
		 System.out.println(processResult);
		
	}

	public void getPayInfo()
	{
		String userid = "23456789";
		
		
		if(this.orderId==null)
		{
			System.out.println("error get orderid");
			return;
		}
		String requestUrl = this.baseUrl;
		ProcessResult processResult = restTemplate.getForObject(
				requestUrl + "/" + userid + "/" + orderId + "/getOrderPayInfo",
				ProcessResult.class);
		 if(processResult.getRetCode()==0)
		 {
			 
		 }
		 System.out.println(processResult);

	}
	
	
	public void getOrderId(String ownerKey)
	{
	   String baseUrl = "http://www.chunzeacademy.com:8080/orderId";
		//String baseUrl = "http://127.0.0.1:9088/orderId";
		   
		///{category}/{ownerKey}/createOrderId
	   JsonRequest jsonRequest =new JsonRequest();
		System.out.println(baseUrl + "/" + StudentConst.ORDER_BUYER_CATEGORY + "/" + ownerKey +  "/createOrderId");
		ProcessResult processResult = restTemplate.postForObject(
				baseUrl + "/" + StudentConst.ORDER_BUYER_CATEGORY + "/" + ownerKey +  "/createOrderId", jsonRequest,
				ProcessResult.class);
		
		 if(processResult.getRetCode()==0)
		 {
			 orderId  = (String)processResult.getResponseInfo();
		 }
		 System.out.println(processResult);
		
	}
	
}
