package com.company.coursestudent.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.company.videodb.domain.Courses;

public class StudentMyCourse {
	private Courses courseInfo;
	private List<CourseClassPublish> courseClass;
	public Courses getCourseInfo() {
		return courseInfo;
	}
	public void setCourseInfo(Courses courseInfo) {
		this.courseInfo = courseInfo;
	}
	public List<CourseClassPublish> getCourseClass() {
		return courseClass;
	}
	public void setCourseClass(List<CourseClassPublish> courseClass) {
		this.courseClass = courseClass;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
