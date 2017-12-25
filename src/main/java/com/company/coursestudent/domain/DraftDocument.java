package com.company.coursestudent.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.company.videodb.domain.Courses;

public class DraftDocument implements Serializable{
	private String userId;
	private Courses courses;
	/**
	 * 视频分类
	 */
	private String category;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Courses getCourses() {
		return courses;
	}
	public void setCourses(Courses courses) {
		this.courses = courses;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
