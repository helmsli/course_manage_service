package com.company.coursestudent.domain;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.company.videodb.domain.Courses;

public class DraftDocument implements Serializable{
	private String userId;
	private Courses courses;
	/**
	 * 首次创建草稿，orderID填写 000000
	 * 从草稿箱进入编辑，客户端将orderID发送给服务器；
	 */
	private String   orderId;
	/**
	 * 视频分类
	 */
	private String category;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public Courses getCourses() {
		return courses;
	}
	public void setCourses(Courses courses) {
		this.courses = courses;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
