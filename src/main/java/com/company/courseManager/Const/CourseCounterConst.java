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
	public static String getTeacherCourseCourseId()
	{
		return "teacherCourse";
	}
	//定义教师的学生信息的courter
	public static String getTeacherStudentCounterKey(String teacherUserId,String userId)
	{
		return "teacherStud:" + teacherUserId + ":" + userId ;
	}
	
	public static String getTeacherStudentUserId(String userId)
	{
		return "teacher:" + userId;
	}
	public static String getTeacherStudentAmountId()
	{
		return "student:";
	}
	
	
	//定义课程的学生信息的courter
		public static String getCourseCounterKey(String userId,String courseId)
		{
			return "courseStudent:"  + userId + ":" +courseId;
		}
		
		public static String getCourseStudentUserId(String courseId)
		{
			return "course:" + courseId;
		}
		public static String getCourseStudentAmountId()
		{
			return "studentCourse";
		}
		
		
		/**
		 * 获取课程评价的计数器索引
		 * @param userId
		 * @param courseId
		 * @return
		 */
		public static String getCourseScoreCounterKey(String userId,String courseId)
		{
			return "cScore:" + userId + ":" + courseId;
		}
		
		public static String getCourseScoreUserId(String courseId)
		{
			return "course:" + courseId;
		}
		public static String getCourseScoreAmountId()
		{
			return "courseScore";
		}
		
		/**
		 * 获取课程评价和回复的个数的计数器
		 * @return
		 */
		public static String getCourseStarAmountId()
		{
			return "courseStarAmount";
		}
		/**
		 * 老师评价的人数的计数器
		 * @param courseId
		 * @return
		 */
		public static String getTeacherScoreUserId(String teacherUid)
		{
			return "teacherStar:" + teacherUid;
		}
		/**
		 * 获取教师的评价的总评分的计数器
		 * @return
		 */
		public static String getTeacherScoreAmountId()
		{
			return "teacherScore";
		}
		/**
		 * 获取老师的评价的总人数的计数器
		 * @return
		 */
		public static String getTeacherStarAmountId()
		{
			return "teacherScore";
		}
		
		/**
		 * 记录课程评价人数
		 * @return
		 */
		public static String getCourseStartPersonAmountId()
		{
			return "courseScore";
		}
}
