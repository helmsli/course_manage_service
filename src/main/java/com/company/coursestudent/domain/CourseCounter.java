package com.company.coursestudent.domain;

import java.io.Serializable;
/**
 * 课程的计数器
 * @author helmsli
 *
 */
public class CourseCounter implements Serializable{
	private int studentAmount;

	public int getStudentAmount() {
		return studentAmount;
	}

	public void setStudentAmount(int studentAmount) {
		this.studentAmount = studentAmount;
	}
	
}
