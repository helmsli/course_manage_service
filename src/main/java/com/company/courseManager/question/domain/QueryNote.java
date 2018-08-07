package com.company.courseManager.question.domain;

import com.company.userOrder.domain.QueryUserOrderRequest;

public class QueryNote extends QueryUserOrderRequest {
	private String courseId;
	private String createUserId;
	private String classId;
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
	
	
	
	
}
