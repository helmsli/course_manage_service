package com.company.videoPlay.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class MyPlayClassInfo implements Serializable {
	/**
	 * PC 还是移动端
	 */
	private String category;
	
	private String userId;
	
	/** 课程编号. */
	private String courseId;
	/**
	 * 课时编号
	 */
	private String classId;

	/**
	 * 已经播放的视频时长
	 */
	private int havePlayedSeconds;

	/**
	 * 更新数据库的时间
	 */
	private long updateDbTime;
	
	public MyPlayClassInfo()
	{
		updateDbTime = System.currentTimeMillis();
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	
	public int getHavePlayedSeconds() {
		return havePlayedSeconds;
	}
	public void setHavePlayedSeconds(int havePlayedSeconds) {
		this.havePlayedSeconds = havePlayedSeconds;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	public long getUpdateDbTime() {
		return updateDbTime;
	}
	public void setUpdateDbTime(long updateDbTime) {
		this.updateDbTime = updateDbTime;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
