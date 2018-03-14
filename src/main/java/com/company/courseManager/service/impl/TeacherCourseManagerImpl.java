package com.company.courseManager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.domain.CourseSearch;
import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.DraftDocument;
import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.coursestudent.domain.StudentMyCourse;
import com.company.coursestudent.service.CourseStudentService;
import com.company.platform.controller.rest.ControllerUtils;
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
	
	@Resource(name="courseStudentService")
	private CourseStudentService courseStudentService;
	
	
	@Value("${course.searchUrl}")
	private String courseSearchUrl;
	
	@Value("${course.newCourseSearchUrl}")
	private String newCourseSearchUrl;
	
	@Value("${course.hotCourseSearchUrl}")
	private String hotCourseSearchUrl;
	
	
	
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
			ProcessResult processResult =  configCourseToDb(dbId,course);
			if(processResult.getRetCode()==0)
			{
				this.courseRedisManager.delCourse(course.getCourseId());
				String retMsg= processResult.getRetMsg();
				/*
				if(!StringUtils.isEmpty(retMsg)&& retMsg.contains("insert"))
				{
					//记录是新发布的课程
					Map<String,String> newMaps = new HashMap<String,String>();
					newMaps.put("courseInsert", "1"); 
					this.putContextData(category, dbId, orderid, newMaps);
				}
				*/
			}
			return processResult;
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
							String strChapterId = "";
							if(i+1<10)
							{
								strChapterId ="00" + String.valueOf(i+1);
							}
							else if(i+1<100)
							{
								strChapterId ="0" + String.valueOf(i+1);
							}
							else
							{
								strChapterId = String.valueOf(i+1);
							}
						
							courseClassPublish.setChapterId(strChapterId);
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
									String strClassId = "";
									if(j+1<10)
									{
										strClassId = "00" + String.valueOf(j+1);
									}
									else if(j+1<100)
									{
										strClassId = "0" + String.valueOf(j+1);
									}
									else
									{
										strClassId=String.valueOf(j+1);
									}
									courseClass.setClassId(courseClass.getChapterId() + "_" +strClassId);
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
			
			ProcessResult processResult =  publishCourseDb(category,orderid,course);
			if(processResult.getRetCode()==0)
			{
				return this.publishCourseToSearch(course);
			}
			return processResult;
			
		}
		return getErrorProcessResult();
	}
	
	public ProcessResult publishCourseToSearch(Courses courses)
	{
		CourseSearch courseSearch = new CourseSearch();
		courseSearch.setCourse(courses);
		ProcessResult result = null;
		//发布课程到搜索引擎
		result  = restTemplate.postForObject(courseSearchUrl + "/saveCourse"  ,courseSearch ,ProcessResult.class);
		if(result.getRetCode()!=0)
		{
			return result;
		}
		//如果时间小于48小时，发布到新课程中；
		if(System.currentTimeMillis() - courses.getCreateTime().getTime() <48 * 3600 * 1000)
		{
			result  = restTemplate.postForObject(newCourseSearchUrl + "/saveCourse"  ,courseSearch ,ProcessResult.class);
			
		}
		
		return result;
	}
	
	/**
	 * 发布订单和用户数据到数据库
	 * @param category
	 * @param dbId
	 * @param orderId
	 * @param courses
	 * @return
	 */
	public ProcessResult publishCourseDb(String category,String orderId,Courses courses)
	{
		
		ProcessResult result = null;
		/**
		 * 删除草稿
		 */
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(StudentConst.USER_DRAFT_CATEGORY);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(orderId);
		userOrder.setStatus(UserOrder.STATUS_CreateOrder);
		userOrder.setUserId(courses.getOwner());
		result  = this.delOneOrder(courseUserDbWriteUrl, userOrder);
		if(result.getRetCode()!=0)
		{
			return result;
		}
		
		userOrder = new UserOrder();
		userOrder.setCategory(StudentConst.USER_DRAFT_COURSEKey);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(courses.getCourseId());
		userOrder.setUserId(courses.getOwner());
		result  = this.delOneOrder(courseUserDbWriteUrl, userOrder);
		
		
		//end 删除草稿
		
		userOrder = new UserOrder();
		userOrder.setCategory(category);
		if(courses.getCreateTime()!=null)
		{
			userOrder.setCreateTime(courses.getCreateTime());
		}
		userOrder.setOrderData(JsonUtil.toJson(courses));
		userOrder.setOrderId(courses.getCourseId());
		userOrder.setStatus(UserOrder.STATUS_FinishOrder);
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
		else if(VideodbConst.RESULT_Error_dbNotExist==processResult.getRetCode())
		{
			processResult.setResponseInfo(VideodbConst.RESULT_SUCCESS);
			processResult.setResponseInfo(null);
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
		else if(VideodbConst.RESULT_Error_dbNotExist==processResult.getRetCode())
		{
			processResult.setResponseInfo(VideodbConst.RESULT_SUCCESS);
			processResult.setResponseInfo(null);
		}
		return processResult;
	}

	@Override
	public ProcessResult getCourseAllClass(String courseId) {
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
	@Override
	public ProcessResult getAllClass(String courseId) {
		String dbId = Courses.getDbId(courseId);
		
		ProcessResult processResult = this.selectAllClassFromDb(dbId, courseId);
		if(processResult.getRetCode()!=0)
		{
			return processResult;
		}
		return processResult;
	}

	@Override
	public ProcessResult getMyCourse(String courseId,String userId) {
		// TODO Auto-generated method stub
		ProcessResult processResult = this.getCourse(courseId);
		if(processResult.getRetCode()!=VideodbConst.RESULT_SUCCESS)
		{
			return processResult;
		}
		Courses course = (Courses)processResult.getResponseInfo();
		processResult = this.getCourseAllClass(courseId);
		if(processResult.getRetCode()!=VideodbConst.RESULT_SUCCESS)
		{
			return processResult;
		}
		List<CourseClassPublish> publishList=(List<CourseClassPublish>)processResult.getResponseInfo();
		StudentBuyOrder studentBuyOrder=null;
		try {
			studentBuyOrder = courseStudentService.getByerStudentBuyOrder(userId, courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, VideodbConst.RESULT_FAILURE, processResult);
			
		}
		/**
		 * 如果买过了，状态填写255，如果没有买过，状态保持原来的
		 */
		StudentMyCourse studentMyCourse = new StudentMyCourse();
		if(studentBuyOrder!=null)
		{
			 List<CourseClassPublish> listmyBuyClass = studentMyCourse.getCourseClass();
			 if(listmyBuyClass!=null&&listmyBuyClass.size()>0)
			 {
				 Map<String,String> buyCourseMaps = new HashMap<String,String>();
				 //遍历购买的列表
				 for(int i=0;i<listmyBuyClass.size();i++)
				 {
					 CourseClassPublish courseClassPublish = listmyBuyClass.get(i);
					 List<CourseClass> classList = courseClassPublish.getCourseList();
					 if(classList!=null)
					 {
						 for(int j=0;j<classList.size();j++)
						 {
							 buyCourseMaps.put(classList.get(j).getClassId(), "");
						 }
					 }
				 }
				 //遍历多有的课程列表
				 for(int i=0;i<publishList.size();i++)
				 {
					 CourseClassPublish courseClassPublish = publishList.get(i);
					 List<CourseClass> classList = courseClassPublish.getCourseList();
					 if(classList!=null)
					 {
						 for(int j=0;j<classList.size();j++)
						 {
							if(buyCourseMaps.containsKey(classList.get(j).getClassId()))
							{
								classList.get(j).setStatus(CoursemanagerConst.STATUS_HAVEDPAID);
							}
						 }
					 }
				 }
			 }
			 else
			 {
				 course.setStatus(CoursemanagerConst.STATUS_HAVEDPAID);
			 }
		}
		studentMyCourse.setCourseInfo(course);
		studentMyCourse.setCourseClass(publishList);
		processResult.setResponseInfo(studentMyCourse);
		return processResult;
	}

	@Override
	public ProcessResult createDraftDoc(DraftDocument draftDocument) {
		// TODO Auto-generated method stub
		String orderId=draftDocument.getOrderId();
		String requestCourseId = draftDocument.getCourses().getCourseId();
		//是否有草稿了，如果有草稿就是修改
		boolean isModifyDraft = true;
		boolean isNeedQueryPublishCourse = true;
		//是否需要申请新的订单ID
		if(StringUtils.isEmpty(orderId)||orderId.compareToIgnoreCase("000000")==0)
		{
			isModifyDraft = false;
			orderId=this.getOrderId(StudentConst.USER_DRAFT_CATEGORY,draftDocument.getUserId());
			draftDocument.getCourses().setCourseId(orderId);
		}
		
		if(StringUtils.isEmpty(orderId))
		{
			return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_ORDERID_NULL,StudentConst.RESULT_String_ORDERID_NULL);
		}
		if(StringUtils.isEmpty(draftDocument.getCourses().getCourseId()))
		{
			return ControllerUtils.getErrorResponse(StudentConst.RESULT_Error_ORDERID_NULL,"courseId is error");
					
		}
		ProcessResult result = null;
		String courseKey = "course";
		String courseClassKey = "courseClass";
		Map<String,String>contexts = new HashMap<String,String>();
		
		//判断订单中是否存在草稿，如果存在草稿，就查询发布的课程和课时了
		if(isModifyDraft)
		{
			List<String>queryKeys = new ArrayList<String>();
			queryKeys.add(courseKey);
			queryKeys.add(courseClassKey);
			contexts=getOrderContextMap(StudentConst.USER_DRAFT_CATEGORY, null, orderId, queryKeys);
			if(contexts.containsKey(courseKey))
			{
				isNeedQueryPublishCourse=false;
			}
		}
		//如果课程id不为空
		if(isNeedQueryPublishCourse && !StringUtils.isEmpty(requestCourseId) && requestCourseId.compareToIgnoreCase("000000")!=0)
		{
			//查询发布的课程和课时信息
			ProcessResult processResult = this.getCourse(requestCourseId);
			if(processResult.getRetCode()!=VideodbConst.RESULT_SUCCESS)
			{
				return processResult;
			}
		
			Courses course = (Courses)processResult.getResponseInfo();
			processResult = this.getCourseAllClass(requestCourseId);
			if(processResult.getRetCode()!=VideodbConst.RESULT_SUCCESS)
			{
				return processResult;
			}
			List<CourseClassPublish> publishList=(List<CourseClassPublish>)processResult.getResponseInfo();
			if(course!=null)
			{
			contexts.put(courseKey, JsonUtil.toJson(course));
			}
			if(publishList!=null)
			{
			contexts.put(courseClassKey, JsonUtil.toJson(publishList));
			}
			if(contexts.size()>0)
			{
				result=this.putContextData(StudentConst.USER_DRAFT_CATEGORY, null, orderId, contexts);
				if(result.getRetCode()!=0)
				{
					return result;
				}
			}
			 	
			//end 查询发布的课程和课时信息
			
		}
		//新发布的课程
		else
		{
			contexts.put(courseKey, JsonUtil.toJson(draftDocument.getCourses()));
			result=this.putContextData(StudentConst.USER_DRAFT_CATEGORY, null, orderId, contexts);
			if(result.getRetCode()!=0)
			{
				return result;
			}
		}
		
		
		
	
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(StudentConst.USER_DRAFT_COURSEKey);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(draftDocument.getCourses().getCourseId());
		userOrder.setUserId(draftDocument.getUserId());
		result = this.queryOneOrder(courseUserDbWriteUrl, userOrder);
		if(result.getRetCode()==0)
		{
			try {
				userOrder = (UserOrder)result.getResponseInfo();
				if(userOrder!=null)
				{
					String oldorderId = userOrder.getOrderData();
					userOrder.setCategory(StudentConst.USER_DRAFT_CATEGORY);
					userOrder.setConstCreateTime();
					userOrder.setOrderId(oldorderId);
					userOrder.setUserId(draftDocument.getUserId());
					this.delOneOrder(courseUserDbWriteUrl, userOrder);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		userOrder = new UserOrder();
		userOrder.setCategory(StudentConst.USER_DRAFT_CATEGORY);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(orderId);
		userOrder.setStatus(UserOrder.STATUS_CreateOrder);
		userOrder.setUserId(draftDocument.getUserId());
		userOrder.setOrderDataType(draftDocument.getCourses().getCourseId());
		userOrder.setOrderData(contexts.get(courseKey));
		result  = restTemplate.postForObject(courseUserDbWriteUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/configUserOrder" ,userOrder ,ProcessResult.class);
		result.setResponseInfo(orderId);
		
	
		userOrder = new UserOrder();
		userOrder.setCategory(StudentConst.USER_DRAFT_COURSEKey);
		userOrder.setConstCreateTime();
		userOrder.setOrderId(draftDocument.getCourses().getCourseId());
		userOrder.setStatus(UserOrder.STATUS_CreateOrder);
		userOrder.setUserId(draftDocument.getUserId());
		userOrder.setOrderData(orderId);
		result  = restTemplate.postForObject(courseUserDbWriteUrl + "/" +  userOrder.getCategory()+ "/" + userOrder.getUserId() + "/configUserOrder" ,userOrder ,ProcessResult.class);
		result.setResponseInfo(orderId);
		return result;
		
	}

	@Override
	public ProcessResult configureTeacher(TeacherInfo teacherInfo) {
		// TODO Auto-generated method stub
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory("teacher");
		userOrder.setOrderId(teacherInfo.getuserId());
		userOrder.setUserId(teacherInfo.getuserId());
		userOrder.setConstCreateTime();
		userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		return this.saveUserOrder(this.courseUserDbWriteUrl, userOrder);
		
	}

	@Override
	public ProcessResult queryTeacher(TeacherInfo teacherInfo) {
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory("teacher");
		userOrder.setOrderId(teacherInfo.getuserId());
		userOrder.setUserId(teacherInfo.getuserId());
		userOrder.setConstCreateTime();
		//userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryOneOrder(courseUserDbWriteUrl, userOrder);
		if(processResult.getRetCode()==0)
		{
			UserOrder UserOrder = (UserOrder)processResult.getResponseInfo();
			TeacherInfo teacherRet = JsonUtil.fromJson(UserOrder.getOrderData(), TeacherInfo.class);
			processResult.setResponseInfo(teacherRet);
		}
		return processResult;
	}
	

}
