package com.company.elasticsearch.domain;

import java.io.Serializable;

import org.springframework.data.elasticsearch.annotations.Document;

import com.company.videodb.domain.Courses;

@Document(indexName = "courseSearchIndex", type = "courseSearch", shards = 1, replicas = 0, refreshInterval = "-1")
public class CourseSearch extends Courses implements Serializable{
	private String id;
	
	/*
	 * 
	 */
	private int sellAmount;
	/**
	 * 综合排名
	 */
	private int totalRank;
	/**
	 * 信用
	 */
	
	private int credit;
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
	
}
