package com.company.courseManager.teacher.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.company.videodb.domain.CourseClass;

public class CourseClassPublish implements Serializable {

	/** 章节. */
	private String chapterId;

	private String chapterTitle;
	
	private String label;
	
	
	
	
	
	
	List<CourseClass> courseList = new ArrayList<CourseClass>();

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public List<CourseClass> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<CourseClass> courseList) {
		this.courseList = courseList;
	}

	
	
}
