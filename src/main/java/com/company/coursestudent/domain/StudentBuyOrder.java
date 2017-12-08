package com.company.coursestudent.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class StudentBuyOrder extends Coursebuyerorder {
	/**
	 * 如果list为空，就是购买全部课程
	 */
	private List<Classbuyerorder> courseClassList;

	public List<Classbuyerorder> getCourseClasses() {
		return courseClassList;
	}

	public void setCourseClasses(List<Classbuyerorder> courseClasses) {
		this.courseClassList = courseClasses;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
