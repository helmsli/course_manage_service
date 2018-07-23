package com.company.courseManager.courseevaluation.controller.rest;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.courseevaluation.domain.CourseEvaluation;
import com.company.courseManager.courseevaluation.domain.CourseLove;
import com.company.courseManager.courseevaluation.service.CourseEvaluationService;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.xinwei.nnl.common.domain.ProcessResult;
@RestController
@RequestMapping("/courseEvalController")
public class EvaluationController {
	@Resource(name="courseEvaluationService")
	private CourseEvaluationService courseEvaluationService;
	@PostMapping(value = "{courseId}/configureEvaluation")
	public  ProcessResult configureEvaluation(@PathVariable String courseId,@RequestBody CourseEvaluation CourseEvaluation) {
		try {
			return courseEvaluationService.configureEvaluation(CourseEvaluation);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
	/**
	 * 课程评价的点赞
	 * @param courseId
	 * @param courseLove
	 * @return
	 */
	@PostMapping(value = "{courseId}/addLove")
	public  ProcessResult addLove(@PathVariable String courseId,@RequestBody CourseLove courseLove) {
		try {
			return courseEvaluationService.configureCourseLove(courseLove);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
			
		}
	}
	
}
