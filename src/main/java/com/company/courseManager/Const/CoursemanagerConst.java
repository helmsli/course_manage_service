package com.company.courseManager.Const;
/**
 * session 中保存的常量
 * @author helmsli
 *
 */

public class CoursemanagerConst {
	/**
	 * 课程已经购买
	 */
	public static final int STATUS_HAVEDPAID=255;
	
	//11001:11100
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_FAILURE = 11100;
	    
	
	public static final int RESULT_FAILURE_ORDER_NULL = RESULT_FAILURE+1;
	public static final String RESULT_FAILURE_STRING_ORDER_NULL = "orderId is null";
	
	public static final int RESULT_FAILURE_MYPLAY_NULL = RESULT_FAILURE+2;
	public static final String RESULT_FAILURE_STRING_MYPLAY_NULL = "orderId is null";
	
	public static final int RESULT_FAILURE_COURSEPRICE = RESULT_FAILURE+3;
	public static final String RESULT_FAILURE_STRING_COURSEPRICE = "course price large than all class";
	
	public static final int RESULT_FAILURE_COURSTeacherSTAT = RESULT_FAILURE+4;
	public static final String RESULT_FAILURE_STRING_TeacherSTAT = "course teacher amount stat lock error";
	
	public static final int RESULT_FAIL_NotExitCourseTeaRedsAmount = RESULT_FAILURE+5;
	public static final String RESULT_FAIL_STRING_FAIL_NotExitCourseTeaRedsAmount = "not exist course teacher amount in redis";
	
	
	public static final String Rsa_private_key = "_private_key";
	public static final String Rsa_public_key = "_public_key";
	
}
