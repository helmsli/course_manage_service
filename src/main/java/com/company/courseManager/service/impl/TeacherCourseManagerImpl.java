package com.company.courseManager.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.domain.CourseClass;
import com.company.videodb.domain.Courses;
import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
@Service("teacherCourseManager")
public class TeacherCourseManagerImpl extends OrderClientService implements TeacherCourseManager {
	@Value("${course.serviceDbWriteUrl}")
	private String courseDbWriteUrl;
	
	@Value("${course.userOrderDbWriteUrl}")
	private String courseUserDbWriteUrl;
	
	
	
	@Override
	public ProcessResult configureTecherCourses(String category, String dbId, String orderid) {
		// TODO Auto-generated method stub
		List<String> keys = new ArrayList<String>();
		String courseKey = "course";
		keys.add(courseKey);
		Map<String,String>maps= getOrderContextMap(category, dbId, orderid, keys);
		if(maps.containsKey(courseKey))
		{
			Courses course = JsonUtil.fromJson(maps.get(courseKey), Courses.class);
			return configCourseToDb(dbId,course);
		}
		return getErrorProcessResult();
	}
	
	public ProcessResult configCourseToDb(String dbId,Courses courses)
	{
		
		ProcessResult result = null;
		result  = restTemplate.postForObject(courseDbWriteUrl + "/" +   dbId + "/" + courses.getCourseId() + "/confCourses" ,courses ,ProcessResult.class);
		return result;
	}

	@Override
	public ProcessResult configureTecherClass(String category, String dbId, String orderid) {
		// TODO Auto-generated method stub
				List<String> keys = new ArrayList<String>();
				String courseKey = "courseClass";
				keys.add(courseKey);
				Map<String,String>maps= getOrderContextMap(category, dbId, orderid, keys);
				if(maps.containsKey(courseKey))
				{					
					List<CourseClass> courseClassList =JsonUtil.fromJson(maps.get(courseKey), new TypeToken<List<CourseClass>>() {}.getType());
					if(courseClassList!=null&&courseClassList.size()>0)
					{
						return configClassToDb(dbId,courseClassList);
					}
				}
				return getErrorProcessResult();
	}
	public ProcessResult configClassToDb(String dbId,List<CourseClass> courseClasses)
	{
		//{dbid}/{courseId}/confClass
		CourseClass courseClass =  courseClasses.get(0);
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		jsonRequest.setJsonString(JsonUtil.toJson(courseClasses));
		result  = restTemplate.postForObject(courseDbWriteUrl + "/" +   dbId + "/" + courseClass.getCourseId() + "/confClass" ,jsonRequest ,ProcessResult.class);
		return result;
	}
	
	@Override
	public ProcessResult publishCourse(String category, String dbId, String orderid) {
		List<String> keys = new ArrayList<String>();
		String courseKey = "course";
		keys.add(courseKey);
		Map<String,String>maps= getOrderContextMap(category, dbId, orderid, keys);
		if(maps.containsKey(courseKey))
		{
			Courses course = JsonUtil.fromJson(maps.get(courseKey), Courses.class);
			return publishCourseDb(category,dbId,orderid,course);
		}
		return getErrorProcessResult();
	}
	
	/**
	 * 发布订单和用户数据到数据库
	 * @param category
	 * @param dbId
	 * @param orderId
	 * @param courses
	 * @return
	 */
	public ProcessResult publishCourseDb(String category,String dbId,String orderId,Courses courses)
	{
		
		ProcessResult result = null;
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(category);
		if(courses.getCreateTime()!=null)
		{
			userOrder.setCreateTime(courses.getCreateTime());
		}
		userOrder.setOrderData(JsonUtil.toJson(courses));
		userOrder.setOrderId(orderId);
		userOrder.setStatus(0);
		userOrder.setUserId(courses.getOwner());
		result  = restTemplate.postForObject(courseUserDbWriteUrl + "/" +  category+ "/" + userOrder.getUserId() + "/configUserOrder" ,userOrder ,ProcessResult.class);
		return result;
	}
	/**
	 * 
	 * @return
	 */
	protected ProcessResult getErrorProcessResult()
	{
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		return processResult;
	}

	

}
