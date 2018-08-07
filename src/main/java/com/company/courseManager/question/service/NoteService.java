package com.company.courseManager.question.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.courseManager.question.domain.EnumQuestionStatus;
import com.company.courseManager.question.domain.Note;
import com.company.courseManager.question.domain.QueryNote;
import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.domain.TeacherInfoResponse;
import com.company.courseManager.teacher.service.TeacherCourseManager;
import com.company.platform.order.OrderClientService;
import com.company.userOrder.domain.QueryUserOrderRequest;
import com.company.userOrder.domain.UserOrder;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
@Service("noteService")
public class NoteService extends OrderClientService{
	private String Category_MyNote="myNotes";
	private String Category_AllNote="allNote";
	private String Category_Note_chapter="cQuesChapter";
	private String Category_Note_Course="cQuesCourse";
	
	@Autowired
	private TeacherCourseManager teacherCourseManager;
	
	
	protected String getMyNoteCourseCategory()
	{
		return Category_Note_Course;
	}
	protected String getMyNoteChapterCategory()
	{
	 return Category_Note_chapter;
	}
	
	protected String getMyAllMyNoteCategory()
	{
		return Category_AllNote;
	}
	
	protected String getAllNoteUserId(String userId)
	{
		return userId;
	}
	
	protected String getAllNoteOrderId(Note note)
	{
		return note.getNoteId();
	}
	
	protected String getCourseNoteUserId(String userId,String courseId)
	{
		return userId+"-" + courseId;
	}
	
	protected String getClassNoteUserId(String userId,String courseId,String classId)
	{
		return userId+"-" + courseId+"-" + classId;
	}
	
	protected String getCourseNoteOrderId(Note note)
	{
		return note.getCourseId() + "-" + note.getClassId()+ "-" + note.getNoteId() + "--";
	}
	
	
	public ProcessResult queryAllMyNote(QueryNote  queryNote )
	{
		queryNote.setCategory(getMyAllMyNoteCategory());
		queryNote.setUserId(queryNote.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryNote.setStartCreateTime(userOrder.getConstCreateDate());
		queryNote.setEndCreateTime(userOrder.getConstCreateDate());
		queryNote.setUserId(getAllNoteUserId(queryNote.getUserId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryNote);
		return processResult;

	}

	public ProcessResult queryNodesWithClassId(QueryNote  queryNote )
	{
		queryNote.setCategory(getMyNoteCourseCategory());
		queryNote.setUserId(queryNote.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryNote.setStartCreateTime(userOrder.getConstCreateDate());
		queryNote.setEndCreateTime(userOrder.getConstCreateDate());
		queryNote.setUserId(getClassNoteUserId(queryNote.getUserId(),queryNote.getCourseId(),queryNote.getClassId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryNote);
		return processResult;

	}
	

	public ProcessResult queryCourseChapter(QueryNote  queryNote )
	{
		queryNote.setCategory(getMyNoteChapterCategory());
		queryNote.setUserId(queryNote.getCreateUserId());
		UserOrder userOrder = new UserOrder();
		queryNote.setStartCreateTime(userOrder.getConstCreateDate());
		queryNote.setEndCreateTime(userOrder.getConstCreateDate());
		queryNote.setUserId(getCourseNoteUserId(queryNote.getUserId(),queryNote.getCourseId()));
		// userOrder.setOrderData(JsonUtil.toJson(teacherInfo));
		ProcessResult processResult = this.queryAllOrderReturnPage(null, queryNote);
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
	public ProcessResult addNote(Note note)
    {
		TeacherInfo teacherInfo  = new TeacherInfo();
		teacherInfo.setUserId(note.getTeacherId());
		ProcessResult retTeacher = teacherCourseManager.queryTeacher(teacherInfo);
		TeacherInfoResponse teacherInfoResponse=null;
		if(retTeacher.getRetCode()==0)
		{
			teacherInfoResponse = (TeacherInfoResponse)retTeacher.getResponseInfo();
		}
	     //需要查询一下用户的信息并保存。
		
		SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmss");
		note.setCreateTime(Calendar.getInstance().getTime());
		note.setNoteId(sf.format(note.getCreateTime()));
		if(teacherInfoResponse!=null)
		note.setTeacherInfo(JsonUtil.toJson(teacherInfoResponse));
		UserOrder userOrder = new UserOrder();
    	
		//add all my Note
		userOrder.setCategory(getMyAllMyNoteCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getAllNoteUserId(note.getCreateUserId()));
    	userOrder.setOrderId(getAllNoteOrderId(note));
    	userOrder.setOrderData(JsonUtil.toJson(note));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
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
    	userOrder.setCategory(getMyNoteCourseCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getClassNoteUserId(note.getCreateUserId(),note.getCourseId(),note.getClassId()));
    	userOrder.setOrderId(getCourseNoteOrderId(note));
    	userOrder.setOrderData(JsonUtil.toJson(note));
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	userOrder.setOrderDataType(String.valueOf(EnumQuestionStatus.NewQuestion.ordinal()));
    	this.saveUserOrder(null, userOrder);
    	
    	
    	//课程章节信息
    	userOrder.setCategory(getMyNoteChapterCategory());
    	userOrder.setConstCreateTime();
    	userOrder.setUserId(getCourseNoteUserId(note.getCreateUserId(),note.getCourseId()));
    	userOrder.setOrderId(note.getClassId());
    	userOrder.setOrderData(note.getChapterInfo());
    	userOrder.setStatus(EnumQuestionStatus.NewQuestion.ordinal());
    	ProcessResult ret = this.saveUserOrder(null, userOrder);
    	if(ret.getRetCode()==0)
    	{
    		ret.setResponseInfo(note);
    	}
    	return ret;
    		
    }
}
