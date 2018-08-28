package com.company.courseManager.question.domain;

import java.util.Date;

public class QuestionEval {
/**
 * 星级评价：
		1）问题回复质量
		2）问题回复速度
		3）教师服务态度
	评价内容
 */
	private String createUserId;
	private String courseId;
	private String classId;
	private String content;
	private String teacherId;
	private int qualityStar;
	private int replyStar;
	private int teacherKindlyStar;
	private Date createTime;
	private String replayId;
	private String questionId;
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public int getQualityStar() {
		return qualityStar;
	}
	public void setQualityStar(int qualityStar) {
		this.qualityStar = qualityStar;
	}
	public int getReplyStar() {
		return replyStar;
	}
	public void setReplyStar(int replyStar) {
		this.replyStar = replyStar;
	}
	public int getTeacherKindlyStar() {
		return teacherKindlyStar;
	}
	public void setTeacherKindlyStar(int teacherKindlyStar) {
		this.teacherKindlyStar = teacherKindlyStar;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getReplayId() {
		return replayId;
	}
	public void setReplayId(String replayId) {
		this.replayId = replayId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
}
