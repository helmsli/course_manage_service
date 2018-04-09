package com.company.coursestudent.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.company.courseManager.domain.CourseTeacher;
import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.company.videodb.domain.Courses;

public class StudentMyCourse {
	private CourseTeacher courseTeacher;
	private List<CourseClassPublish> courseClass;
	
	public CourseTeacher getCourseTeacher() {
		return courseTeacher;
	}
	public void setCourseTeacher(CourseTeacher courseTeacher) {
		this.courseTeacher = courseTeacher;
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
