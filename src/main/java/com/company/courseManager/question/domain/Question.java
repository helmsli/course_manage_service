package com.company.courseManager.question.domain;
/**
 * 问答
 */
import java.util.Date;

public class Question {
	private String category;
	private String createUserId;
	private String createUserInfo;
	private String courseId;
	private String classId;
	private String chapterInfo;
	private String title;
	private String content;
	private int    price;
	private Date createTime;
	private Date expireTime;
	private int  expiredHours;
	private int status;
	private String questionId;
	private String replayId;
	private String atttchFile;
	private String teacherId;
	private String teacherInfo;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
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
	public String getChapterInfo() {
		return chapterInfo;
	}
	public void setChapterInfo(String chapterInfo) {
		this.chapterInfo = chapterInfo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getReplayId() {
		return replayId;
	}
	public void setReplayId(String replayId) {
		this.replayId = replayId;
	}
	public String getAtttchFile() {
		return atttchFile;
	}
	public void setAtttchFile(String atttchFile) {
		this.atttchFile = atttchFile;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherInfo() {
		return teacherInfo;
	}
	public void setTeacherInfo(String teacherInfo) {
		this.teacherInfo = teacherInfo;
	}
	public String getCreateUserInfo() {
		return createUserInfo;
	}
	public void setCreateUserInfo(String createUserInfo) {
		this.createUserInfo = createUserInfo;
	}
	public int getExpiredHours() {
		return expiredHours;
	}
	public void setExpiredHours(int expiredHours) {
		this.expiredHours = expiredHours;
	}
	
	
}
