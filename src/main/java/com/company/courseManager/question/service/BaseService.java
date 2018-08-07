package com.company.courseManager.question.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.courseManager.question.domain.EnumQuestionStatus;
import com.company.courseManager.question.domain.Question;
import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

public class BaseService extends OrderClientService{
	
	@Autowired
	private TeacherCourseManager teacherCourseManager;
	
	
	protected String getAllQuestionUserId(Question question)
	{
		return question.getCreateUserId();
	}
	
	protected String getAllQuestionOrderId(Question question)
	{
		return question.getQuestionId();
	}
	protected String getNewQuestionUserId(Question question)
	{
		return question.getCreateUserId()+"-" + question.getCourseId();
	}
	
	protected String getNewQuestionOrder(Question question)
	{
		return question.getCourseId() + "-" + question.getClassId()+ "-" + question.getQuestionId() + "--";
	}
	protected String getAnswerQuestionOrder(Question question)
	{
		return question.getCourseId() + "-" + question.getClassId()+ "-" + question.getQuestionId() + "-"+ question.getReplayId();
	}
	
	protected String getMyAllMyQuestionCategory()
	{
		return "";
		
	}
	
	protected String getMyQuestionCategory()
	{
		return "";
		
	}
	
	protected String getMyQuesCourseCategory()
	{
		return "";
	}
	protected String getMyQuesChapterCategory()
	{
		return "";
	}
	/**
	 * 
	 * @param question
	 * @return
	 */
	public ProcessResult addQuestion(Question question)
    {
		TeacherInfo teacherInfo  = new TeacherInfo();
		teacherInfo.setUserId(question.getCreateUserId());
		ProcessResult retTeacher = teacherCourseManager.queryTeacher(teacherInfo);
		
	     //需要查询一下用户的信息并保存。
		
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		question.setCreateTime(Calendar.getInstance().getTime());
		question.setQuestionId(sf.format(question.getCreateTime()));
		UserOrder userOrder = new UserOrder();
    	
		//add all my question
		userOrder.setCategory(getMyAllMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    
    	//teacher all question
    	userOrder.setCategory(getMyAllMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getTeacherId());
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    
    
		
    	//add my course question
    	userOrder.setCategory(getMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getNewQuestionUserId(question));
    	userOrder.setOrderId(getNewQuestionOrder(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    
    	
    	//课程信息
    	userOrder.setCategory(getMyQuesCourseCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getCreateUserId());
    	userOrder.setOrderId(question.getCourseId());
    	userOrder.setOrderData(question.getChapterInfo());
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	this.saveUserOrder(null, userOrder);
    	
    	//保存章节信息
    	userOrder.setCategory(getMyQuesChapterCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getCreateUserId() + "-" + question.getCourseId());
    	userOrder.setOrderId(question.getClassId());
    	userOrder.setOrderData(question.getChapterInfo());
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	return this.saveUserOrder(null, userOrder);
    	
    }
	
	
	public ProcessResult queryAllQuestion(Question question)
    {
		
		return null;
    	
    }
    /**
     * 
     * @param question
     * @return
     */
	public ProcessResult answerQuestion(Question question)
    {
		question.setCreateTime(Calendar.getInstance().getTime());
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHH:mm:ss");
		question.setReplayId(sf.format(question.getCreateTime()));
    	
		UserOrder userOrder = new UserOrder();
		//add all my question
		userOrder.setCategory(getMyAllMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	
		//userOrder.setCategory(Category_question);
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getNewQuestionUserId(question));
    	userOrder.setOrderId(getAnswerQuestionOrder(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	return this.saveUserOrder(null, userOrder);
    }
}
