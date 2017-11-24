package com.company.courseManager.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.company.videodb.Const.VideodbConst;
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
	
	@Value("${course.serviceDbReadUrl}")
	private String courseDbReadUrl;
	
	
	@Value("${course.userOrderDbWriteUrl}")
	private String courseUserDbWriteUrl;
	
	@Resource(name="courseRedisManager")
	private CourseRedisManagerImpl courseRedisManager;
	
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
		try {
			
			result  = restTemplate.postForObject(courseDbWriteUrl + "/" +   dbId + "/" + courses.getCourseId() + "/confCourses" ,courses ,ProcessResult.class);
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ProcessResult configureTecherClass(String category, String dbId, String orderid) {
		// TODO Auto-generated method stub
				List<CourseClass> willAddClassList=new ArrayList<CourseClass>();
				List<String> keys = new ArrayList<String>();
				String courseClassKey = "courseClass";
				String courseKey = "course";
				keys.add(courseKey);
				keys.add(courseClassKey);
				Map<String,String>maps= getOrderContextMap(category, dbId, orderid, keys);
				Courses course = null;
				if(maps.containsKey(courseKey))
				{
					course =JsonUtil.fromJson(maps.get(courseKey), Courses.class);
					
				}
				
				
				if(maps.containsKey(courseClassKey))
				{					
					List<CourseClassPublish> classPublishList =JsonUtil.fromJson(maps.get(courseClassKey), new TypeToken<List<CourseClassPublish>>() {}.getType());
					
					for(int i=0;i<classPublishList.size();i++)
					{
						CourseClassPublish courseClassPublish = classPublishList.get(i);
						if(StringUtils.isEmpty(courseClassPublish.getChapterId()))
						{
							courseClassPublish.setChapterId(String.valueOf(i+1));
						}
					//CourseClassPublish courseClassPublish = JsonUtil.fromJson(maps.get(courseKey),CourseClassPublish.class);
						List<CourseClass> courseClassList=classPublishList.get(i).getCourseList();
						if(courseClassList!=null&&courseClassList.size()>0)
						{
							for(int j=0;j<courseClassList.size();j++)
							{
								CourseClass courseClass = courseClassList.get(j);
								courseClass.setOwner(course.getOwner());
								courseClass.setCourseId(course.getCourseId());
								if(StringUtils.isEmpty(courseClass.getChapterId()))
								{
									courseClass.setChapterId(courseClassPublish.getChapterId());
								}
								if(StringUtils.isEmpty(courseClass.getClassId()))
								{
									courseClass.setClassId(courseClass.getChapterId() + "_" +String.valueOf(j+1));
								}
							}
							
							willAddClassList.addAll(courseClassList);
							
						}
					}
					//配置多有的课程
					if(willAddClassList.size()>0)
					{
						return configClassToDb(dbId,willAddClassList);
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
	/**
	 * 从数据库中获取所有的calss
	 * @param dbId
	 * @param courseClasses
	 * @return
	 */
	public ProcessResult selectAllClassFromDb(String dbId,String courseId)
	{
		//{dbid}/{courseId}/getClass
		ProcessResult result = null;
		//JsonRequest jsonRequest = new JsonRequest();
		result  = restTemplate.getForObject(this.courseDbReadUrl + "/" +   dbId + "/" + courseId + "/getClass"  ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			List<CourseClass>list = JsonUtil.fromJson((String)result.getResponseInfo(), new TypeToken<List<CourseClass>>() {}.getType());
			result.setResponseInfo(list);
		}
		return result;
	}

	public ProcessResult selectOneClassFromDb(String dbId,String courseId,String classId)
	{
		//{dbid}/{courseId}/getClass
		//{dbid}/{courseId}/{classId}getOneClass
		ProcessResult result = null;
		JsonRequest jsonRequest = new JsonRequest();
		result  = restTemplate.getForObject(this.courseDbReadUrl + "/" +   dbId + "/" + courseId + "/" + classId + "/getOneClass"  ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			CourseClass courseClass = JsonUtil.fromJson((String)result.getResponseInfo(), CourseClass.class);
			result.setResponseInfo(courseClass);
		}
		return result;
	}

	public ProcessResult selectCourseFromDb(String dbId,String courseId)
	{
		//{dbid}/{courseId}/getClass
		ProcessResult result = null;
		//JsonRequest jsonRequest = new JsonRequest();
		result  = restTemplate.getForObject(this.courseDbReadUrl + "/" +   dbId + "/" + courseId + "/getCourse"  ,ProcessResult.class);
		
		if(result.getRetCode()==0)
		{
			Courses courses = JsonUtil.fromJson((String)result.getResponseInfo(), Courses.class); 
			result.setResponseInfo(courses);
		}
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
			
			return publishCourseDb(category,course);
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
	public ProcessResult publishCourseDb(String category,Courses courses)
	{
		
		ProcessResult result = null;
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(category);
		if(courses.getCreateTime()!=null)
		{
			userOrder.setCreateTime(courses.getCreateTime());
		}
		userOrder.setOrderData(JsonUtil.toJson(courses));
		userOrder.setOrderId(courses.getCourseId());
		userOrder.setStatus(0);
		userOrder.setUserId(courses.getOwner());
		userOrder.setConstCreateTime();
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

	@Override
	public ProcessResult getCourse(String courseId) {
		// TODO Auto-generated method stub
		Courses courses = this.courseRedisManager.getCourseFromCache(courseId);
		if(courses!=null)
		{
			ProcessResult processResult = new ProcessResult();
			processResult.setResponseInfo(courses);
			processResult.setRetCode(VideodbConst.RESULT_SUCCESS);
			return processResult;
		}
		String dbId = Courses.getDbId(courseId);
		ProcessResult processResult = this.selectCourseFromDb(dbId, courseId);
		if(processResult.getRetCode()==VideodbConst.RESULT_SUCCESS)
		{
			 courses = (Courses)processResult.getResponseInfo();
			this.courseRedisManager.putCourseToCache(courses);
		}
		return processResult;
	}

	@Override
	public ProcessResult getCourseClass(String courseId,String classId) {
		// TODO Auto-generated method stub
		CourseClass courseClass = this.courseRedisManager.getClassFromCache(courseId, classId);
		if(courseClass!=null)
		{
			ProcessResult processResult = new ProcessResult();
			processResult.setResponseInfo(courseClass);
			processResult.setRetCode(VideodbConst.RESULT_SUCCESS);
			return processResult;
		}
		String dbId = Courses.getDbId(courseId);
		
		ProcessResult processResult = this.selectOneClassFromDb(dbId, courseId, classId);
		if(processResult.getRetCode()==VideodbConst.RESULT_SUCCESS)
		{
			courseClass = (CourseClass)processResult.getResponseInfo();
			this.courseRedisManager.putClassToCache(courseClass);;
		}
		return processResult;
	}

	@Override
	public ProcessResult getCourseAllClass( String courseId) {
		String dbId = Courses.getDbId(courseId);
		
		ProcessResult processResult = this.selectAllClassFromDb(dbId, courseId);
		if(processResult.getRetCode()!=0)
		{
			return processResult;
		}
		List<CourseClass>list = (List<CourseClass>)processResult.getResponseInfo();
		List<CourseClassPublish> publishList =new ArrayList<CourseClassPublish>();
		String chapter = "&&&*7^&&";
		CourseClassPublish courseClassPublish = null;
		for(int i=0;i<list.size();i++)
		{
			CourseClass courseClass= list.get(i);
			if(courseClass.getChapterId().compareToIgnoreCase(chapter)==0)
			{
				courseClassPublish.getCourseList().add(courseClass);
			}
			else
			{
				
				courseClassPublish = new CourseClassPublish();
				courseClassPublish.setChapterId(courseClass.getChapterId());
				chapter=courseClass.getChapterId();
				courseClassPublish.getCourseList().add(courseClass);
				publishList.add(courseClassPublish);
				
			}
		}
		processResult.setResponseInfo(publishList);
		return processResult;
	}

	

}
