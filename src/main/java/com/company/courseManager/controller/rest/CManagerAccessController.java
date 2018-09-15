package com.company.courseManager.controller.rest;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.Resource;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.coursestudent.domain.DraftDocument;
import com.company.coursestudent.domain.StudentConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.userOrder.domain.QueryUserOrderRequest;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.JsonRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.orderDb.domain.OrderMain;

/**
 * course manager access controller
 * 
 * @author helmsli
 *
 */
@RestController
@RequestMapping("/courseTeacherManager")
public class CManagerAccessController {
	@Resource(name = "teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;

	/**
	 * 发布课程 tcoursepub
	 * 
	 * @param category
	 * @param draftDocument
	 * @return
	 */
	@GetMapping(value = "{orderId}/published")
	public ProcessResult clientPublishCourse(@PathVariable String orderId) {
		try {
			return teacherCourseManager.clientPublishCourse(StudentConst.USER_DRAFT_CATEGORY, orderId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, StudentConst.RESULT_Error_Fail, null);
		}
	}

	/**
	 * 发布创建草稿
	 * 
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{category}/createDraft")
	public ProcessResult createDraft(@PathVariable String category,
			@RequestBody DraftDocument draftDocument) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.createDraftDoc(draftDocument);
			/*
			 * 构造一个teacher对象，将其传递给useridorder；
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);

		}
		return processResult;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/clearCourse")
	public ProcessResult clearCourse(@RequestBody Courses course) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.clearCourse(course);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);

		}
		return processResult;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/clearDraft")
	public ProcessResult clearDraft(@RequestBody Courses course) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.delDraftDoc(course);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);

		}
		return processResult;
	}

	@RequestMapping(method = RequestMethod.GET, value = "{category}/{dbid}/{orderid}/teacherpublishCourse")
	public ProcessResult teacherPublishCourse(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			processResult = teacherCourseManager.teacherPublishCourse(category, dbid, orderid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);

		}
		return processResult;
	}

	/**
	 * 将数据从教师的课程发布到学员库
	 * 
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */

	@RequestMapping(method = RequestMethod.POST, value = "{category}/{dbid}/{orderid}/confTeacherCourse")
	public ProcessResult configureTecherCourses(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid, @RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			String dbId = OrderMain.getDbId(orderid);
			processResult = teacherCourseManager.configureTecherCourses(category, dbId, orderid);
			/**
			 * 构造一个teacher对象，将其传递给useridorder；
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);

		}
		return processResult;
	}

	/**
	 * 将数据从教师的课时发布到学员库
	 * 
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{category}/{dbid}/{orderid}/confTeacherClass")
	public ProcessResult configureTecherClass(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid, @RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			String dbId = OrderMain.getDbId(orderid);
			processResult = teacherCourseManager.configureTecherClass(category, dbId, orderid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 设置发布后的数据到老师中心的数据库
	 * 
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{category}/{dbid}/{orderid}/publishCourse")
	public ProcessResult publishCourse(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid, @RequestBody JsonRequest jsonRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			/**
			 * 1.通过订单查询对应的数据(course，list<courseClasss>) 2.将数据保存到
			 */
			String dbId = OrderMain.getDbId(orderid);
			processResult = teacherCourseManager.publishCourse(category, dbId, orderid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 配置教师信息
	 * 
	 * @param userId
	 * @param teacherInfo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{userId}/confTeacherInfo")
	public ProcessResult configureTecher(@PathVariable String userId, @RequestBody TeacherInfo teacherInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			teacherInfo.setUserId(userId);
			processResult = teacherCourseManager.configureTeacher(teacherInfo, "");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 申请成为老师
	 * @param userId
	 * @param teacherInfo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{userId}/teacherApplication")
	public ProcessResult techerApplication(@PathVariable String userId,
			@RequestBody TeacherInfo teacherInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			teacherInfo.setUserId(userId);
			processResult = teacherCourseManager.teacherApplication(teacherInfo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	@RequestMapping(method = RequestMethod.POST, value = "{userId}/applicationResult")
	public ProcessResult getTecherApplicationResult(@PathVariable String userId,
			@RequestBody TeacherInfo teacherInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			teacherInfo.setUserId(userId);
			processResult = teacherCourseManager.teacherApplication(teacherInfo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 订单系统调用审批通过后升级为老师
	 * @param category
	 * @param dbid
	 * @param orderid
	 * @param jsonRequest
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "{category}/{dbid}/{orderid}/autoBeTeacher")
	public ProcessResult autoBeTeacher(@PathVariable String category, @PathVariable String dbid,
			@PathVariable String orderid, @RequestBody JsonRequest jsonRequest) {

		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {

			processResult = teacherCourseManager.configureTeacher(null, orderid);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;

	}

	/**
	 * 查询教师信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{userId}/getTeacherInfo")
	public ProcessResult getTecherInfo(@PathVariable String userId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			TeacherInfo teacherInfo = new TeacherInfo();
			teacherInfo.setUserId(userId);
			processResult = teacherCourseManager.queryTeacher(teacherInfo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 查询推荐的老师教师信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "{userId}/getRecommandTeacher")
	public ProcessResult getRecommandTecherInfo(@PathVariable String userId) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			TeacherInfo teacherInfo = new TeacherInfo();
			teacherInfo.setUserId(userId);
			processResult = teacherCourseManager.queryRecommandTeacher(userId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	/**
	 * 查询教师列表
	 * @param queryUserOrderRequest
	 * @return
	 */
	@PostMapping(value = "{userId}/getTeacherList")
	public ProcessResult getTecherList(@PathVariable String userId,
			@RequestBody QueryUserOrderRequest queryUserOrderRequest) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(CoursemanagerConst.RESULT_FAILURE);
		try {
			return teacherCourseManager.queryTeacherList(queryUserOrderRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveExceptionToResult(processResult, e);
		}
		return processResult;
	}

	protected void saveExceptionToResult(ProcessResult processResult, Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		String errorStr = errors.toString();
		if (!StringUtils.isEmpty(errorStr)) {
			String msg = errorStr.length() > 1000 ? errorStr.substring(0, 1000) : errorStr;
			processResult.setRetMsg(msg);
		}
		return;
	}

}
