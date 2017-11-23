package com.company.courseManager.service;

import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface StudentCourseService {
	/**
	 * 查询单个课程信息
	 * @param courseId
	 * @return
	 */
	public ProcessResult queryCourses(String courseId);
	
	/**
	 * 查询单个课程
	 * @param userId -- 用户ID
	 * @param courseId -- 课程id，不能为空，
	 * @param chapterId -- 章节ID，如果为空，查询所有章节
	 * @param classId  -- 课时ID，如果为空，查询所有课时
	 * @return
	 */
	public ProcessResult queryClass(String courseId,String chapterId,String classId);
	
	/**
	 * 
	 * @param courseId
	 * @return
	 */
	public ProcessResult queryAllClass(String courseId);
	
	
}
