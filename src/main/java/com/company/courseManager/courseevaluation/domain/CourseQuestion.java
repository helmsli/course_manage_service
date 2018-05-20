package com.company.courseManager.courseevaluation.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CourseQuestion extends InterActBase{
	
	/**
	 * 课程ID
	 */
	private String courseId;

	//问题价格
	private String price;

	
	public String getCourseId() {
		return courseId;
	}


	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
	

