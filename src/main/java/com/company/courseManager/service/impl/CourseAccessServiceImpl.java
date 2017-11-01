package com.company.courseManager.service.impl;

import com.company.courseManager.service.CourseAccessService;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

public class CourseAccessServiceImpl implements CourseAccessService {

	@Override
	public ProcessResult configureCourses(long userId, Courses courses) {
		// TODO Auto-generated method stub
		/**
		 * 1.需要生成订单系统，
		 * 2.如果已经发布，将状态修改为已经发布修改后待发布；
		 * 
		 */
		return null;
	}

}
