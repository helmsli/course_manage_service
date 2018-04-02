package com.company.courseManager.courseevaluation.domain;

import java.io.Serializable;
import java.util.Date;

public class CourseLove implements Serializable {
	private String evaluationId;

	/** 课程ID. */
	private String courseId;

	/** 评价时间. */
	private Date createTime;

	/** 评价人ID. */
	private String createrUserId;

	/** 评价人名字. */
	private String createrUserName;

	/** 评价人头像. */
	private String createrAvatar;

	public String getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreaterUserId() {
		return createrUserId;
	}

	public void setCreaterUserId(String createrUserId) {
		this.createrUserId = createrUserId;
	}

	public String getCreaterUserName() {
		return createrUserName;
	}

	public void setCreaterUserName(String createrUserName) {
		this.createrUserName = createrUserName;
	}

	public String getCreaterAvatar() {
		return createrAvatar;
	}

	public void setCreaterAvatar(String createrAvatar) {
		this.createrAvatar = createrAvatar;
	}

	@Override
	public String toString() {
		return "CourseLove [evaluationId=" + evaluationId + ", courseId=" + courseId + ", createTime=" + createTime
				+ ", createrUserid=" + createrUserId + ", createrUsername=" + createrUserName + ", createrAvatar="
				+ createrAvatar + "]";
	}

	
}
