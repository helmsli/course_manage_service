package com.company.courseManager.Const;

public class CourseCounterConst {
	/**
	 * 老师的课程计数器路由前缀
	 */
	public static final String Category_Teacher_course="courseTeacher";
	
	/**
	 * 获取教师发布课程数目计数器索引
	 * @param userId
	 * @param courseId
	 * @return
	 */
	public static String getTeacherCourseCounterKey(String userId,String courseId)
	{
		return "course:" + userId + ":" + courseId;
	}
	
	public static String getTeacherCourseUserId(String userId)
	{
		return "teacher:" + userId;
	}
	public static String getTeacherCourseCourseId(String courseId)
	{
		return "course:" + courseId;
	}
	//定义教师的学生信息的courter
	public static String getTeacherStudentCounterKey(String teacherUserId,String userId,String courseId)
	{
		return "course:" + teacherUserId + ":" + userId + ":" +courseId;
	}
	
	public static String getTeacherStudentUserId(String userId)
	{
		return "teacher:" + userId;
	}
	public static String getTeacherStudentCourseId(String courseId)
	{
		return "student:" + courseId;
	}
	
}
