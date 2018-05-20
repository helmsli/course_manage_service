package com.company.courseManager.courseevaluation.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 互动内容基础信息
 * @author helmsli
 *
 */
public class InterActBase implements Serializable{

	public static int STATUS_INIT = 0;
	//已经支付
	public static int STATUS_PAID = 1;
	//回复
	public static int STATUS_REPAY = 2;
	//再次提问
	public static int STATUS_Submit_again = 3;
	//关闭
	public static int STATUS_Submit_closed = 255;

	
	/**
	 * 互动ID ，唯一的
	 */
	private String id;
	
	/** 评价时间. */
	private Date createTime;

	/** 评价人ID. */
	private String createrUserId;
	
	

	/** 评价人名字. */
	private String createrUserName;

	/** 评价人头像. */
	private String createrAvatar;

	/** 评价人级别. */
	private String createrLevel;

	/** 评价人职称. */
	private String createrRank;

	/** 评价人类型(教师/学员). */
	private int createrType;

	/** 是否是课程发布者. */
	private int createrIsOwner;

	//问题标题
	private String title;
		
	/** 评论内容. */
	private String content;

	/**
	 * 多个附件用，隔开
	 */
	private String attachment;
	
	/** 评论还是回复. 0--评论 1-- 回复 */
	private int createrIsReply;

	/** 回复发言者的名字. */
	private String replyName;

	/** 回复帖子的ID. */
	private String replyParentId;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreaterUserId() {
		return createrUserId;
	}

	public void setCreaterUserId(String createrUserId) {
		this.createrUserId = createrUserId;
	}

	public String getCreaterUserName() {
		return createrUserName;
	}

	public void setCreaterUserName(String createrUserName) {
		this.createrUserName = createrUserName;
	}

	public String getCreaterAvatar() {
		return createrAvatar;
	}

	public void setCreaterAvatar(String createrAvatar) {
		this.createrAvatar = createrAvatar;
	}

	public String getCreaterLevel() {
		return createrLevel;
	}

	public void setCreaterLevel(String createrLevel) {
		this.createrLevel = createrLevel;
	}

	public String getCreaterRank() {
		return createrRank;
	}

	public void setCreaterRank(String createrRank) {
		this.createrRank = createrRank;
	}

	public int getCreaterType() {
		return createrType;
	}

	public void setCreaterType(int createrType) {
		this.createrType = createrType;
	}

	public int getCreaterIsOwner() {
		return createrIsOwner;
	}

	public void setCreaterIsOwner(int createrIsOwner) {
		this.createrIsOwner = createrIsOwner;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCreaterIsReply() {
		return createrIsReply;
	}

	public void setCreaterIsReply(int createrIsReply) {
		this.createrIsReply = createrIsReply;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public String getReplyParentId() {
		return replyParentId;
	}

	public void setReplyParentId(String replyParentId) {
		this.replyParentId = replyParentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	
	
	
}
