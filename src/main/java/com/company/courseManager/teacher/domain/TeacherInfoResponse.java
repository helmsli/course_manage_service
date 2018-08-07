package com.company.courseManager.teacher.domain;

import com.company.security.domain.SecurityUser;
/**
 * 返回用户查询教师信息的列表
 * @author helmsli
 *
 */
public class TeacherInfoResponse extends TeacherInfo {
	private SecurityUser securityUser;
	private TeacherCounter teacherCounter;
	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	public TeacherCounter getTeacherCounter() {
		return teacherCounter;
	}

	public void setTeacherCounter(TeacherCounter teacherCounter) {
		this.teacherCounter = teacherCounter;
	}
	
	
}
