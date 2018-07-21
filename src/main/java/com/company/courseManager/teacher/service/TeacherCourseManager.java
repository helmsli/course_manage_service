package com.company.courseManager.teacher.service;

import com.company.courseManager.teacher.domain.TeacherInfo;
import com.company.coursestudent.domain.DraftDocument;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface TeacherCourseManager {
	
	/**
	 * 创建草稿
	 * @param draftDocument
	 * @return
	 */
	public ProcessResult createDraftDoc(DraftDocument draftDocument);

	
	public ProcessResult clearCourse(Courses course);

	/**
	 * 客户端发布课程到课程库
	 * @param draftDocument
	 * @return
	 */
	public ProcessResult clientPublishCourse(String category,String orderid);
	
	
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
	 * 查询我是否购买该课程的详细信息，如果已经购买全部课程，状态为255
	 * @param courseId
	 * @return
	 */
	public ProcessResult getMyCourse(String courseId,String userId);
	
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
	
	
	public ProcessResult configureTeacher(TeacherInfo teacherInfo);
	
	public ProcessResult queryTeacher(TeacherInfo teacherInfo);
	
	public ProcessResult queryRecommandTeacher(String userId);
	
		
}
