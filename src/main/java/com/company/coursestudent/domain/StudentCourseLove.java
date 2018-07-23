package com.company.coursestudent.domain;

import java.util.Date;

public class StudentCourseLove {
	/** 课程ID. */
	private String courseId;

	/** 评价时间. */
	private Date createTime;

	/** 评价人ID. */
	private String userId;

	/** 评价人名字. */
	private String createrUserName;

	/** 评价人头像. */
	private String createrAvatar;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		return "StudentCourseLove [courseId=" + courseId + ", createTime=" + createTime + ", userId=" + userId
				+ ", createrUserName=" + createrUserName + ", createrAvatar=" + createrAvatar + "]";
	}

}
