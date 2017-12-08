package com.company.courseManager.controller.rest;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.StudentBuyOrder;
import com.company.coursestudent.domain.StudentConst;
import com.company.coursestudent.service.CourseStudentService;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMain;

@RestController
@RequestMapping("/studentCourse")
public class CStudentCourseController {
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	@Resource(name="courseStudentService")
	private CourseStudentService courseStudentService;
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/queryCourse")
	public  ProcessResult queryCourse(@PathVariable String courseId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourse(courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/{classId}/queryOneClass")
	public  ProcessResult queryOneClass(@PathVariable String courseId,@PathVariable String classId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourseClass(courseId, classId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	@RequestMapping(method = RequestMethod.GET,value = "/{courseId}/queryAllClass")
	public  ProcessResult queryAllClass(@PathVariable String courseId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.getCourseAllClass(courseId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, processResult);
			
			
		}
		return processResult;
	}
	@RequestMapping(method = RequestMethod.POST,value = "/{userid}/{orderId}/submitBuyOrder")
	public ProcessResult submitOrder(@PathVariable String userid,@PathVariable String orderId,@RequestBody StudentBuyOrder studentBuyOrder)
	{
		
		try {
			return courseStudentService.submitBuyOrder(orderId, studentBuyOrder);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
		}
		
	}
}
