package com.company.courseManager.teacher.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.company.videodb.domain.CourseClass;

public class CourseClassPublish implements Serializable {

	/** 章节. */
	private String chapterId;

	List<CourseClass> courseClasses = new ArrayList<CourseClass>();

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public List<CourseClass> getCourseClasses() {
		return courseClasses;
	}

	public void setCourseClasses(List<CourseClass> courseClasses) {
		this.courseClasses = courseClasses;
	}
	
}
