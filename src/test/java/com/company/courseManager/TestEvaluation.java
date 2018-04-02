package com.company.courseManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.web.client.RestTemplate;

import com.company.courseManager.courseevaluation.domain.CourseEvaluation;
import com.company.courseManager.courseevaluation.domain.CourseLove;
import com.company.userOrder.domain.QueryUserOrderRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public class TestEvaluation {
	public RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestEvaluation testEvaluation = new TestEvaluation();
		try {
			testEvaluation.configureEvaluation();
			testEvaluation.testPlusLove();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//testEvaluation.configureReply();
		testEvaluation.queryReply();
	}
	public void configureEvaluation()
	{
		String courseId = "000003";
		String baseUrl = "http://www.chunzeacademy.com:8080/courseEvalController/";
		CourseEvaluation courseEvaluation = new CourseEvaluation();
		courseEvaluation.setCourseId(courseId);
		courseEvaluation.setCreaterUserId("110000000");
		courseEvaluation.setCreaterIsReply(CourseEvaluation.IS_EVALUATION);
		courseEvaluation.setContent("content:" + System.currentTimeMillis());
		ProcessResult processResult = restTemplate.postForObject(baseUrl + courseId + "/configureEvaluation", courseEvaluation, ProcessResult.class);
		System.out.println(processResult);
	}
	public void configureReply()
	{
		String courseId = "000003";
		String baseUrl = "http://www.chunzeacademy.com:8080/courseEvalController/";
		CourseEvaluation courseEvaluation = new CourseEvaluation();
		courseEvaluation.setReplyParentId("20180317202841-V186224696-92");
		courseEvaluation.setCourseId(courseId);
		courseEvaluation.setCreaterIsReply(CourseEvaluation.IS_EVALUATION);
		courseEvaluation.setContent("content:" + System.currentTimeMillis());
		courseEvaluation.setCreaterIsReply(courseEvaluation.IS_REPLY);
		ProcessResult processResult = restTemplate.postForObject(baseUrl + courseId + "/configureEvaluation", courseEvaluation, ProcessResult.class);
		System.out.println(processResult);
	}
	public void testPlusLove()
	{
		String courseId = "000003";
		String baseUrl = "http://www.chunzeacademy.com:8080/courseEvalController/";
		CourseLove courseLove = new CourseLove();
		courseLove.setEvaluationId("20180325152211-V859034426-23");
		courseLove.setCourseId(courseId);
		courseLove.setCreaterUserId("aaaaa");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			courseLove.setCreateTime(format.parse("2018-03-25 15:22:11"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ProcessResult processResult = restTemplate.postForObject(baseUrl + courseId + "/addLove", courseLove, ProcessResult.class);
		System.out.println(processResult);
	}
	public void queryReply()
	{
		String courseId = "000003";
		String baseUrl = "http://www.chunzeacademy.com:8080/userOrderDb/courseEval/";
		QueryUserOrderRequest queryUserOrderRequest = new QueryUserOrderRequest();
		queryUserOrderRequest.setCategory("courseEval");
		queryUserOrderRequest.setUserId(courseId);
		queryUserOrderRequest.setPageNum(1);
		queryUserOrderRequest.setPageSize(100);
		//queryUserOrderRequest.setEndCreateTime(Calendar.getInstance().getTime());
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, -1);
		queryUserOrderRequest.setStartCreateTime(now.getTime());
		System.out.println(baseUrl + courseId + "/queryOrderSortByOrderId");
		ProcessResult processResult = restTemplate.postForObject(baseUrl + courseId + "/queryOrderSortByOrderId", queryUserOrderRequest, ProcessResult.class);
		System.out.println(processResult);
	}
	//20180317170339-173922732-79
	
	
	///evaluation
}
