package com.company.courseManager.courseevaluation.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.company.courseManager.courseevaluation.domain.CourseEvaluation;
import com.company.courseManager.courseevaluation.domain.CourseQuestion;
import com.company.courseManager.courseevaluation.domain.EvaluationErrorConst;
import com.company.courseManager.courseevaluation.domain.QuestionErrorConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.security.domain.SecurityUser;
import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

public class CourseQuestionService extends OrderClientService{
private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${course.questionCenter}")
	private String evaluationCenter;
	
	@Value("${security.userCenter}")
	private String securityUserCenter;

	
	@Value("${course.questionCenter.nodeId:1}")
	private String questionNodeId;

	
	Random rand = new Random();

	
	
	
	/**
	 * 
	 * @param courseEvaluation
	 * @return
	 */
	public ProcessResult submitQuestion(CourseQuestion courseQuestion) {
		
		/*
		if(StringUtils.isEmpty(courseQuestion.getCourseId()))
		{
			return ControllerUtils.getErrorResponse(EvaluationErrorConst.RESULT_FAILURE_CourseIdisNull, "course id is null");
		}
		SecurityUser securityUser =getUserInfo(courseQuestion.getCreaterUserId());
		if(securityUser!=null)
		{
			courseQuestion.setCreaterAvatar(securityUser.getAvatar());
			courseQuestion.setCreaterUserName(securityUser.getDisplayName());
			
		}
		else
		{
			this.logger.error("security user is null");
		}
		
		
		
		
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory("courseEval");
		userOrder.setUserId(courseQuestion.getCourseId());
		
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
		}
		else
		{
			userOrder.setOrderDataType("V");
			String evaluationId=this.createEvaluationId(userOrder.getCreateTime());
			courseEvaluation.setEvaluationId(evaluationId);
			courseEvaluation.setCreateTime(userOrder.getCreateTime());
			
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
		*/
		return null;
	}

	
	public String createQuestionId(Date createTime) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = format.format(createTime);
		return createQuestionIdbyTime(times,false);
	}
	/**
	 * 创建回复的评价ID
	 * 
	 * @param parentEvaluationId
	 * @return
	 */
	public String createQuestionIdbyTime(String parentQuestionId,boolean isReply) {
		// 生成10到99之间的随机数
		int randNumber = rand.nextInt(89) + 10;

		StringBuffer evaluationBuff = new StringBuffer();
		/**
		 * 插入YYYYMMDDmmHiSS
		 */
		evaluationBuff.append(parentQuestionId.substring(0, 14));
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
		evaluationBuff.append(questionNodeId);
		evaluationBuff.append("-");
		evaluationBuff.append(randNumber);
		return evaluationBuff.toString();
	}
	
	public SecurityUser getUserInfo(String userId)
	{
		//{routerId}/getUserInfoById
		ProcessResult result = null;
		SecurityUser securityUser = new SecurityUser();
		
		securityUser.setUserId(Long.parseLong(userId));
		logger.debug(this.securityUserCenter + "/" +  userId+ "/getUserInfoById");
		result  = restTemplate.postForObject(this.securityUserCenter + "/" +  userId+ "/getUserInfoById" ,securityUser ,ProcessResult.class);
		if(result.getRetCode()==0)
		{
			String ls = JsonUtil.toJson(result.getResponseInfo());
			return JsonUtil.fromJson(ls, SecurityUser.class);
		}
		return null;
	}

}
