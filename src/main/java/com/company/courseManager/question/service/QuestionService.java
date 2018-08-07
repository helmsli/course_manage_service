package com.company.courseManager.question.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.courseManager.courseevaluation.service.UserService;
import com.company.courseManager.question.domain.EnumQuestionStatus;
import com.company.courseManager.question.domain.Note;
import com.company.courseManager.question.domain.QueryNote;
import com.company.courseManager.question.domain.QueryQuestion;
import com.company.courseManager.question.domain.Question;
import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.domain.TeacherInfoResponse;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.platform.order.OrderClientService;
import com.company.security.domain.SecurityUser;
import com.company.userOrder.domain.QueryUserOrderRequest;
import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
@Service("questionService")
public class QuestionService extends OrderClientService{
	private String Category_MyQuestion="myquess";
	private String Category_AllQuestion="allques";
	private String Category_Question_chapter="QuesChapter";
	private String Category_Question_Course="QuesCourse";
	
	@Autowired
	private TeacherCourseManager teacherCourseManager;
	
	@Autowired
	private UserService userService;
	
	
	protected String getMyQuestionCourseCategory()
	{
		return Category_Question_Course;
	}
	protected String getMyQuestionChapterCategory()
	{
	 return Category_Question_chapter;
	}
	
	protected String getMyAllMyQuestionCategory()
	{
		return Category_AllQuestion;
	}
	/**
	 * 纯粹保存单个问题
	 * @return
	 */
	protected String getQuestionCategory()
	{
		return Category_Question_Course;
	}
	/**
	 * 老师的问题列表
	 * @return
	 */
	protected String getTeacherQuestionCategory()
	{
		return "teacherQues";
	}
	protected String getAllQuestionUserId(String userId)
	{
		return userId;
	}
	
	protected String getAllQuestionOrderId(Question question)
	{
		return question.getQuestionId();
	}
	
	protected String getCourseQuestionUserId(String userId,String courseId)
	{
		return userId+"-" + courseId;
	}
	
	protected String getClassQuestionUserId(String userId,String courseId,String classId)
	{
		return userId+"-" + courseId+"-" + classId;
	}
	
	protected String getCourseQuestionOrderId(Question question)
	{
		return question.getCourseId() + "-" + question.getClassId()+ "-" + question.getQuestionId() + "--";
	}
	
	
	public ProcessResult queryAllMyQuestion(QueryQuestion  queryQuestion )
	{
		queryQuestion.setCategory(getMyAllMyQuestionCategory());
		queryQuestion.setUserId(queryQuestion.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryQuestion.setStartCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setEndCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setUserId(getAllQuestionUserId(queryQuestion.getUserId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryQuestion);
		return processResult;

	}

	/**
	 * 查询老师的问题列表
	 * @param queryQuestion
	 * @return
	 */
	public ProcessResult queryNeedAnswer(QueryQuestion  queryQuestion )
	{
		queryQuestion.setCategory(getTeacherQuestionCategory());
		queryQuestion.setUserId(queryQuestion.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryQuestion.setStartCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setEndCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setUserId(getAllQuestionUserId(queryQuestion.getUserId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryQuestion);
		return processResult;

	}

	public ProcessResult queryQuestionWithClassId(QueryQuestion  queryQuestion)	{
		queryQuestion.setCategory(getMyQuestionCourseCategory());
		queryQuestion.setUserId(queryQuestion.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryQuestion.setStartCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setEndCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setUserId(getClassQuestionUserId(queryQuestion.getUserId(),queryQuestion.getCourseId(),queryQuestion.getClassId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryQuestion);
		return processResult;

	}
	

	public ProcessResult queryCourseChapter(QueryQuestion  queryQuestion )
	{
		queryQuestion.setCategory(getMyQuestionChapterCategory());
		queryQuestion.setUserId(queryQuestion.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryQuestion.setStartCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setEndCreateTime(userOrder.getConstCreateDate());
		queryQuestion.setUserId(getCourseQuestionUserId(queryQuestion.getUserId(),queryQuestion.getCourseId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryQuestion);
		return processResult;

	}
	/*
	public ProcessResult queryAllNoteByCourse(QueryNote  queryNote )
	{
		queryNote.setCategory(getMyNoteCourseCategory());
		
		UserOrder userOrder = new UserOrder();
		queryNote.setStartCreateTime(userOrder.getConstCreateDate());
		queryNote.setEndCreateTime(userOrder.getConstCreateDate());
		userOrder.setUserId(getCourseNoteUserId(queryNote.getUserId(),queryNote.getCourseId()));
    	
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryNote);
		return processResult;

	}
*/
	/**
	 * 
	 * @param question
	 * @return
	 */
	public ProcessResult addQuestion(Question question)
    {
		TeacherInfo teacherInfo  = new TeacherInfo();
		teacherInfo.setUserId(question.getTeacherId());
		ProcessResult retTeacher = teacherCourseManager.queryTeacher(teacherInfo);
		TeacherInfoResponse teacherInfoResponse=null;
		if(retTeacher.getRetCode()==0)
		{
			teacherInfoResponse = (TeacherInfoResponse)retTeacher.getResponseInfo();
		}
		
		SecurityUser securityUser = userService.getUserInfo(question.getCreateUserId());
	     //需要查询一下用户的信息并保存。
		
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		question.setCreateTime(Calendar.getInstance().getTime());
		question.setQuestionId(sf.format(question.getCreateTime()));
		if(teacherInfoResponse!=null)
			question.setTeacherInfo(JsonUtil.toJson(teacherInfoResponse));
		if(securityUser!=null)
			question.setCreateUserInfo(JsonUtil.toJson(securityUser));
		UserOrder userOrder = new UserOrder();
    	
		//add all my Note
		userOrder.setCategory(getMyAllMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question.getCreateUserId()));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.saveUserOrder(null, userOrder);
    	
    	/**
    	 * 保存到老师的列表
    	 */
    	userOrder.setCategory(getTeacherQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question.getTeacherId()));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.saveUserOrder(null, userOrder);
    	
    	//单个问题的列表
    	userOrder.setCategory(getQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getQuestionId());
    	userOrder.setOrderId(question.getQuestionId());
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.saveUserOrder(null, userOrder);
    
		
    	//add my course note
    	/**
    	 * queryNote.setCategory(getMyNoteCourseCategory());
		queryNote.setUserId(queryNote.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryNote.setStartCreateTime(userOrder.getConstCreateDate());
		queryNote.setEndCreateTime(userOrder.getConstCreateDate());
		queryNote.setUserId(getClassNoteUserId(queryNote.getUserId(),queryNote.getCourseId(),queryNote.getClassId()));
		
    	 */
    	userOrder.setCategory(getMyQuestionCourseCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getClassQuestionUserId(question.getCreateUserId(),question.getCourseId(),question.getClassId()));
    	userOrder.setOrderId(getCourseQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	this.saveUserOrder(null, userOrder);
    	
    	
    	//课程章节信息
    	userOrder.setCategory(getMyQuestionChapterCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getCourseQuestionUserId(question.getCreateUserId(),question.getCourseId()));
    	userOrder.setOrderId(question.getClassId());
    	userOrder.setOrderData(question.getChapterInfo());
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	ProcessResult ret = this.saveUserOrder(null, userOrder);
    	if(ret.getRetCode()==0)
    	{
    		ret.setResponseInfo(question);
    	}
    	return ret;
    		
    }
	/**
	 * 老师回复问题
	 * @param question
	 * @return
	 */
	public ProcessResult answerQuestion(Question question)
	{
		
		question.setStatus(EnumQuestionStatus.HavedAnswered.ordinal());
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		question.setCreateTime(Calendar.getInstance().getTime());
		question.setReplayId(sf.format(question.getCreateTime()));
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(getQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getQuestionId());
    	userOrder.setOrderId(question.getReplayId());
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.saveUserOrder(null, userOrder);
    	this.updateQuestionStatus(question);
		return null;
	}
	
	public ProcessResult waitAnswerquestion(Question question)
	{
		
		question.setStatus(EnumQuestionStatus.WaitAnswer.ordinal());
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		question.setCreateTime(Calendar.getInstance().getTime());
		question.setReplayId(sf.format(question.getCreateTime()));
		UserOrder userOrder = new UserOrder();
		userOrder.setCategory(getQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getQuestionId());
    	userOrder.setOrderId(question.getReplayId());
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.saveUserOrder(null, userOrder);
    	this.updateQuestionStatus(question);
		return null;
	}
	
	public ProcessResult updateQuestionStatus(Question question)
    {
		UserOrder userOrder = new UserOrder();
    	//单个问题
		userOrder.setCategory(getQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(question.getQuestionId());
    	userOrder.setOrderId(question.getReplayId());
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(question.getStatus());
    	this.updateUserOrderStatus(null, userOrder);
    	
		
		//add all my Note
		userOrder.setCategory(getMyAllMyQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question.getCreateUserId()));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(question.getStatus());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	this.updateUserOrderStatus(null, userOrder);
    	
    	/**
    	 * 保存到老师的列表
    	 */
    	userOrder.setCategory(getTeacherQuestionCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllQuestionUserId(question.getTeacherId()));
    	userOrder.setOrderId(getAllQuestionOrderId(question));
    	userOrder.setOrderData(JsonUtil.toJson(question));
    	userOrder.setStatus(question.getStatus());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	userOrder.setAmount(question.getPrice());
    	return this.updateUserOrderStatus(null, userOrder);
    	
    }
}
