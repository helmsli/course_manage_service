package com.company.courseManager.domain;



import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.videodb.domain.Courses;

public class CourseTeacher extends Courses {
	
	private transient TeacherInfo teacherInfo;

	public TeacherInfo getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(TeacherInfo teacherInfo) {
		this.teacherInfo = teacherInfo;
	}
	
	
	
}
