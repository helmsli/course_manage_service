package com.company.courseManager.domain;



import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.courseManager.teacher.domain.TeacherInfoResponse;
import com.company.videodb.domain.Courses;

public class CourseTeacher extends Courses {
	
	private  TeacherInfoResponse teacherInfo;

	public TeacherInfoResponse getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(TeacherInfoResponse teacherInfo) {
		this.teacherInfo = teacherInfo;
	}
	
	
	
}
