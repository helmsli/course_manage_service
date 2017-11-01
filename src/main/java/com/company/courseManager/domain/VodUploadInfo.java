package com.company.courseManager.domain;

import java.io.Serializable;

public class VodUploadInfo implements Serializable{
	/**
	 * 本地视频文件名
	 */
	private String localFilename;
	/**
	 * 视频分类
	 */
	private String category;
	/**
	 * 视频标题
	 */
	private String fileTitle;
	/**
	 * 标签，以,分割
	 */
	private String tag;
	/**
	 * 视频描述 
	 */
	private String desc;
	public String getLocalFilename() {
		return localFilename;
	}
	public void setLocalFilename(String localFilename) {
		this.localFilename = localFilename;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFileTitle() {
		return fileTitle;
	}
	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String toString() {
		return "VodInfo [localFilename=" + localFilename + ", category=" + category + ", fileTitle=" + fileTitle
				+ ", tag=" + tag + ", desc=" + desc + "]";
	}
	
	
	
	
}
