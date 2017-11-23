package com.company.courseManager.controller.rest;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMain;

@RestController
@RequestMapping("/studentCourse")
public class CStudentCourseController {
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
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
		}
		return processResult;
	}
}
