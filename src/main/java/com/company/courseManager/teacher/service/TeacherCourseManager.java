package com.company.courseManager.teacher.service;

import com.xinwei.nnl.common.domain.ProcessResult;

public interface TeacherCourseManager {
	/**
	 * 根据发布订单，创建教师中心的课程信息
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @return
	 */
	public ProcessResult configureTecherCourses(String category,String dbId,String orderid);
	
	/**
	 * 根据发布的订单，创建教师中心的课时信息
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @return
	 */
	public ProcessResult configureTecherClass(String category,String dbId,String orderid);
	
	/**
	 * 
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @return
	 */
	public ProcessResult publishCourse(String category,String dbId,String orderid);
	/**
	 * 
	 * @param category
	 * @param dbId
	 * @param orderid
	 * @return
	 */
	public ProcessResult getCourse(String courseId);

	/**
	 * 
	 * @param category
	 * @param dbId
	 * @param courseId
	 * @return
	 */
	public ProcessResult getCourseClass(String courseId,String classId);
	
	/**
	 * 
	 * @param category
	 * @param dbId
	 * @param courseId
	 * @return 按照章节保存的CourseClassPublish
	 */
	public ProcessResult getCourseAllClass(String courseId);
	
	/**
	 * 
	 * @param courseId
	 * @return CourseClass 列表
	 */
	public ProcessResult getAllClass(String courseId);
		
}
