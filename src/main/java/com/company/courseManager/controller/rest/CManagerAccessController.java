package com.company.courseManager.controller.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.DraftDocument;
import com.company.coursestudent.domain.UserInfo;
import com.company.videoPlay.domain.AliVodUploadInfo;
import com.company.videodb.Const.VideodbConst;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMain;

/**
 * course manager access controller
 * @author helmsli
 *
 */
@RestController
@RequestMapping("/courseTeacherManager")
public class CManagerAccessController {
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	
	/**
	 * 发布创建草稿
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "{category}/createDraft")
	public  ProcessResult createDraft(@PathVariable String category,@RequestBody DraftDocument draftDocument) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult =teacherCourseManager.createDraftDoc(draftDocument);
					/*
			 * 构造一个teacher对象，将其传递给useridorder；
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult,e);
			
		}
		return processResult;
	}
	
	/**
	 * 将数据从教师的课程发布到学员库
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	
	@RequestMapping(method = RequestMethod.POST,value = "{category}/{dbid}/{orderid}/confTeacherCourse")
	public  ProcessResult configureTecherCourses(@PathVariable String category,@PathVariable String dbid,@PathVariable String orderid,@RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			String dbId = OrderMain.getDbId(orderid);
			processResult =teacherCourseManager.configureTecherCourses(category, dbId, orderid);
			/**
			 * 构造一个teacher对象，将其传递给useridorder；
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult,e);
			
		}
		return processResult;
	}
	
	
	
	
	/**
	 * 将数据从教师的课时发布到学员库
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "{category}/{dbid}/{orderid}/confTeacherClass")
	public  ProcessResult configureTecherClass(@PathVariable String category,@PathVariable String dbid,@PathVariable String orderid,@RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			String dbId = OrderMain.getDbId(orderid);
			processResult =teacherCourseManager.configureTecherClass(category, dbId, orderid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult,e);
		}
		return processResult;
	}

	/**
	 * 设置发布后的数据到老师中心的数据库
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "{category}/{dbid}/{orderid}/publishCourse")
	public  ProcessResult publishCourse(@PathVariable String category,@PathVariable String dbid,@PathVariable String orderid,@RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			/**
			 * 1.通过订单查询对应的数据(course，list<courseClasss>)
			 * 2.将数据保存到
			 */
			String dbId = OrderMain.getDbId(orderid);
			processResult =teacherCourseManager.publishCourse(category, dbId, orderid);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult,e);
		}
		return processResult;
	}
	
	protected void saveExceptionToResult(ProcessResult processResult,Exception e)
	{
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String errorStr = errors.toString();
		if(!StringUtils.isEmpty(errorStr))
		{
			processResult.setRetMsg(errorStr.substring(0,1000));
		}
		return ;
	}

}
