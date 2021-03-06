package com.company.courseManager.courseevaluation.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.company.courseManager.courseevaluation.domain.CourseEvaluation;
import com.company.courseManager.courseevaluation.domain.CourseLove;
import com.company.courseManager.courseevaluation.domain.EvaluationErrorConst;
import com.company.courseManager.domain.CourseTeacher;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.courseManager.teacher.service.TeacherCourseStatService;
import com.company.courseManager.utils.JsonUtilSuper;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
import com.company.security.domain.SecurityUser;

@Service("courseEvaluationService")
public class CourseEvaluationService extends OrderClientService{
	 private Logger logger = LoggerFactory.getLogger(getClass());

	 
	 
	 
	 
	
	@Value("${course.evaluationCenter}")
	private String evaluationCenter;
	
	@Value("${course.evaluationCenter.nodeId:1}")
	private String evaluationCenterNodeId;
	
	@Resource(name="teacherCourseStatService")
	private TeacherCourseStatService teacherCourseStatService;
	
	@Resource(name="teacherCourseManager")
	private TeacherCourseManager teacherCourseManager;
	
	@Autowired
	private UserService userService;
	Random rand = new Random();

	
	
	public ProcessResult configureCourseLove(CourseLove courseLove) {
		if(StringUtils.isEmpty(courseLove.getEvaluationId())||StringUtils.isEmpty(courseLove.getCourseId())||StringUtils.isEmpty(courseLove.getCreaterUserId()))
		{
			return ControllerUtils.getErrorResponse(EvaluationErrorConst.RESULT_FAILURE_ReplyCourseIdisNull, "course id or evaluation is null");
		}
		//从回复的父亲帖子中获取时间，为了保持排序的时候按照一组排序
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory("courseEval");
		userOrder.setUserId(courseLove.getCourseId());
		Date createTime =this.getReplayCreateDate(courseLove.getEvaluationId());
		userOrder.setCreateTime(createTime);
		userOrder.setOrderId(courseLove.getEvaluationId());
		userOrder.setAmount(1);
		ProcessResult ret  = plusUserOrderAmount(evaluationCenter, userOrder);
		if(ret.getRetCode()!=0)
		{
			logger.error("course Love error:" + userOrder.toString());
		}
		userOrder = new UserOrder();
		userOrder.setCategory("courseLove");
		userOrder.setUserId(courseLove.getCreaterUserId());
		//userOrder.setCreateTime();
		userOrder.setOrderId(courseLove.getEvaluationId());
		userOrder.setAmount(1);
		ret=this.saveUserOrder(evaluationCenter, userOrder);
		if(ret.getRetCode()!=0)
		{
			logger.error("course Love save user error:" + userOrder.toString());
		}
		return ControllerUtils.getSuccessResponse(null);
		
		
	}	
	
	/**
	 * 
	 * @param courseEvaluation
	 * @return
	 */
	public ProcessResult configureEvaluation(CourseEvaluation courseEvaluation) {
		
		if(StringUtils.isEmpty(courseEvaluation.getCourseId()))
		{
			return ControllerUtils.getErrorResponse(EvaluationErrorConst.RESULT_FAILURE_CourseIdisNull, "course id is null");
		}
		SecurityUser securityUser =userService.getUserInfo(courseEvaluation.getCreaterUserId());
		if(securityUser!=null)
		{
			courseEvaluation.setCreaterAvatar(securityUser.getAvatar());
			courseEvaluation.setCreaterUserName(securityUser.getDisplayName());
			
			//courseEvaluation.setCreaterLevel(securityUser.get);
		}
		else
		{
			this.logger.error("security user is null");
		}
		
		
		
		
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory("courseEval");
		userOrder.setUserId(courseEvaluation.getCourseId());
		
		if(courseEvaluation.isReply())
		{
			if(StringUtils.isEmpty(courseEvaluation.getReplyParentId()))
			{
				return ControllerUtils.getErrorResponse(EvaluationErrorConst.RESULT_FAILURE_ReplyCourseIdisNull, "reply parent id is null");
			}
			//从回复的父亲帖子中获取时间，为了保持排序的时候按照一组排序
			Date createTime =this.getReplayCreateDate(courseEvaluation.getReplyParentId());
			userOrder.setCreateTime(createTime);
			
			String replyEvaluationId = createReplyEvaluationId(courseEvaluation.getReplyParentId(),true);
			courseEvaluation.setEvaluationId(replyEvaluationId);
			courseEvaluation.setCreateTime(Calendar.getInstance().getTime());
			userOrder.setOrderDataType("R");
			teacherCourseStatService.plusCourseStarCounter(courseEvaluation.getCreaterUserId(), courseEvaluation.getCourseId());
		}
		//是课程评价，不是回复
		else
		{
			
			//课程评价的分数
			userOrder.setOrderDataType("V");
			String evaluationId=this.createEvaluationId(userOrder.getCreateTime());
			courseEvaluation.setEvaluationId(evaluationId);
			courseEvaluation.setCreateTime(userOrder.getCreateTime());
			//异步调用系统框架，异步进行相关课程积分计算
			teacherCourseStatService.plusCourseScore(courseEvaluation.getCreaterUserId(), courseEvaluation.getCourseId(), courseEvaluation.getStarLevel());
			plusTeacherScore(courseEvaluation.getCourseId(),courseEvaluation.getCreaterUserId(),courseEvaluation.getStarLevel());
			teacherCourseStatService.plusCourseStarCounter(courseEvaluation.getCreaterUserId(), courseEvaluation.getCourseId());
		}
		userOrder.setOrderId(courseEvaluation.getEvaluationId());
		userOrder.setOrderData(JsonUtil.toJson(courseEvaluation));
		userOrder.setStatus(0);
		ProcessResult ret = this.saveUserOrder(evaluationCenter, userOrder);
		if(ret.getRetCode()==0)
		{
			ret.setResponseInfo(userOrder);
		}
		return ret;
	}
	/**
	 * 通过对课程的评价，计算对老师的评分
	 */
	@Async
	protected void plusTeacherScore(String courseId,String starUserId,int startLevel)
	{
		
		ProcessResult ret = teacherCourseManager.getCourse(courseId);
		if(ret.getRetCode()==0)
		{
			CourseTeacher courses =(CourseTeacher) ret.getResponseInfo();
			;
			teacherCourseStatService.plusTeacherScore(courses.getTeacherInfo().getUserId(), startLevel, starUserId, courseId);
		}
		else
		{
			logger.error("error caculate teacher Score:" + courseId + ":" +  ret.toString());
		}
	  
	}
	
	
	protected CourseEvaluation configureReply(CourseEvaluation courseEvaluation)
	{
		
		return null;
	}

	
	public Date getReplayCreateDate(String parentEvaluationId)
	{

		try {
			String times = parentEvaluationId.substring(0, 14);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			return format.parse(times);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建回复的评价ID
	 * 
	 * @param parentEvaluationId
	 * @return
	 */
	public String createReplyEvaluationId(String parentEvaluationId,boolean isReply) {
		// 生成10到99之间的随机数
		int randNumber = rand.nextInt(89) + 10;

		StringBuffer evaluationBuff = new StringBuffer();
		/**
		 * 插入YYYYMMDDmmHiSS
		 */
		evaluationBuff.append(parentEvaluationId.substring(0, 14));
		evaluationBuff.append("-");
		if(isReply)
		{
			evaluationBuff.append("R");
		}
		else
		{
			evaluationBuff.append("V");
			
		}
		// 计算当前时间到2018年3月15日的时间差数值，
		long timeInterval = System.currentTimeMillis() - 1521103496703l;
		evaluationBuff.append(timeInterval);
		/**
		 * 增加-是为了因为timeInterval长度不一样，确保长度长的在后面，因此需要将随机数和时间分割
		 */
		
		evaluationBuff.append("-");
		evaluationBuff.append(evaluationCenterNodeId);
		evaluationBuff.append("-");
		evaluationBuff.append(randNumber);

		return evaluationBuff.toString();
	}
	/**
	 * 根据创建时间获取ID
	 * @param createTime
	 * @return
	 */
	public String createEvaluationId(Date createTime) {
	
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(createTime);
		return createReplyEvaluationId(times,false);
	}
	
	
	
}
