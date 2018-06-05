package com.company.coursestudent.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.company.courseManager.domain.CourseTeacher;
import com.company.courseManager.teacher.domain.CourseClassPublish;
import com.company.courseManager.teacher.domain.TeacherCounter;
import com.company.videodb.domain.Courses;

public class StudentMyCourse {
	private CourseTeacher courseTeacher;
	private TeacherCounter teacherCounter;
	private CourseCounter courseCounter;
	private List<CourseClassPublish> courseClass;
	
	private String courseScore;
	private String courseStarPerson;
	public StudentMyCourse()
	{
		CourseCounter courseCounter = new CourseCounter();
		courseCounter.setStudentAmount(0);
		this.setCourseCounter(courseCounter);
		this.setCourseStarPerson("0");
	}
	
	
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
	
	
	public TeacherCounter getTeacherCounter() {
		return teacherCounter;
	}
	public void setTeacherCounter(TeacherCounter teacherCounter) {
		this.teacherCounter = teacherCounter;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public CourseCounter getCourseCounter() {
		return courseCounter;
	}
	public void setCourseCounter(CourseCounter courseCounter) {
		this.courseCounter = courseCounter;
	}
	public String getCourseScore() {
		return courseScore;
	}
	public void setCourseScore(String courseScore) {
		this.courseScore = courseScore;
	}


	public String getCourseStarPerson() {
		return courseStarPerson;
	}


	public void setCourseStarPerson(String courseStarPerson) {
		this.courseStarPerson = courseStarPerson;
	}
	
	
}
