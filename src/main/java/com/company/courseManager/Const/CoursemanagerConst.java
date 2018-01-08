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
	
	
	public static final String Rsa_private_key = "_private_key";
	public static final String Rsa_public_key = "_public_key";
	
}
