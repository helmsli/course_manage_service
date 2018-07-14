/**
 * 
 */
package com.company.review.constants;

/**
 * @notes 
 * 
 * @author wangboc
 * 
 * @version 2018年6月30日 下午2:22:53
 */
public interface ReviewConst {
	
	/** 俱乐部审核结果码 */
	
	int CLUB_REVIEW_AGREE = 500;
		
	int CLUB_REVIEW_REJECT = 501;
	
	/** 俱乐部审核响应码 */
	
	int RET_SUCCESS = 0;
	
	int RET_ERROR = -1;
	
	int RET_PUSH_TO_REVIEWER_ERROR = 1001;
	
	int RET_PERFORM_REVIEW_ERROR = 1002;
	
	int RET_REVIEW_AGREE_ERROR = 1003;
	
	int RET_REVIEW_REJECT_ERROR = 1004;
	
	int RET_PUSH_TO_APPLICANT_ERROR = 1005;
	
	/** 审核上下文 */
	String REVIEW_DATA_KEY = "jisuReviewContextData";

	/** 审核的当前taskId */
	String REVIEW_CURRENT_TASK_ID = "reviewCurrentTaskId";
}
