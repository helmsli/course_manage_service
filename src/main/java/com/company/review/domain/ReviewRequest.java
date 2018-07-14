/**
 * 
 */
package com.company.review.domain;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @notes 审核请求封装
 * 
 * @author wangboc
 * 
 * @version 2018年7月7日 下午5:40:22
 */
public class ReviewRequest implements Serializable {

	private static final long serialVersionUID = -2171884897570557080L;

	// 审核通过
	public static final int Result_accept = 500;
	// 审核驳回
	public static final int Result_reject = 501;
	// 跳转到其他步骤
	public static final int Result_jumpOthers = 502;

	private String orderId;// 审核的订单ID
	

	private String category; // 订单category

	private String taskId;// 审核的任务ID

	private int reviewResult;// 审核结果

	private String remarks;// 备注信息

	private String reviewer;// 审核人

	private Date reviewDate;// 审核时间

	private Object otherData;// 审核的其他信息

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId
	 *            the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the reviewResult
	 */
	public int getReviewResult() {
		return reviewResult;
	}

	/**
	 * @param reviewResult
	 *            the reviewResult to set
	 */
	public void setReviewResult(int reviewResult) {
		this.reviewResult = reviewResult;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks
	 *            the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the reviewer
	 */
	public String getReviewer() {
		return reviewer;
	}

	/**
	 * @param reviewer
	 *            the reviewer to set
	 */
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	/**
	 * @return the reviewDate
	 */
	public Date getReviewDate() {
		return reviewDate;
	}

	/**
	 * @param reviewDate
	 *            the reviewDate to set
	 */
	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	/**
	 * @return the otherData
	 */
	public Object getOtherData() {
		return otherData;
	}

	/**
	 * @param otherData
	 *            the otherData to set
	 */
	public void setOtherData(Object otherData) {
		this.otherData = otherData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
