package com.company.courseManager.service;

import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface CourseAccessService {
	/**
	 * 
	 * @param userId
	 * @param courses
	 * @return
	 */
	public ProcessResult configureCourses(long userId,Courses courses);
	
	
}
