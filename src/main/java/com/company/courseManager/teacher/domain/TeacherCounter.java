package com.company.courseManager.teacher.domain;

import java.io.Serializable;

/**
 * 老师的信息统计项目
 * @author helmsli
 *
 */
public class TeacherCounter implements Serializable  {
	//老师学生数量
	private int studentAmount;
	//老师课程数量
	private int courseAmount;
	/**
	 * 老师的综合评分
	 */
	private String score;
	public int getStudentAmount() {
		return studentAmount;
	}
	public void setStudentAmount(int studentAmount) {
		this.studentAmount = studentAmount;
	}
	public int getCourseAmount() {
		return courseAmount;
	}
	public void setCourseAmount(int courseAmount) {
		this.courseAmount = courseAmount;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	
	
}
