package com.company.courseManager.domain;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.company.videodb.domain.Courses;

public class CourseSearch extends Courses implements Serializable {
private String id;
	
	/*
	 * 
	 */
	private int sellAmount=1;
	/**
	 * 综合排名
	 */
	private int totalRank=1;
	/**
	 * 信用
	 */
	
	private int credit=1;
	
	@Override
	public void setCourseId(String courseId)
	{
		super.setCourseId(courseId);
		this.setId(courseId);
	}
	/**
	 * 
	 */
	public int getSellAmount() {
		return sellAmount;
	}
	public void setSellAmount(int sellAmount) {
		this.sellAmount = sellAmount;
	}
	public int getTotalRank() {
		return totalRank;
	}
	public void setTotalRank(int totalRank) {
		this.totalRank = totalRank;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCourse(Courses courses)
	{
		BeanUtils.copyProperties(courses, this);
		this.setCourseId(courses.getCourseId());
	}
	
}
