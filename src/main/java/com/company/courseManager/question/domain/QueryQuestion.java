package com.company.courseManager.question.domain;

import com.company.userOrder.domain.QueryUserOrderRequest;

public class QueryQuestion extends QueryUserOrderRequest {
	private String courseId;
	private String createUserId;
	private String classId;
	/*
	 * 1：待支付 2：新问题，3：待回复 4：已回复 ，5问题失效 6：问题结束

	 */
	private int status = 0;
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public String toString() {
		return "QueryNote [courseId=" + courseId + ", createUserId=" + createUserId + ", classId=" + classId + "]";
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	
}
