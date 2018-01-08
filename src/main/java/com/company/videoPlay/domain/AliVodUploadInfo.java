package com.company.videoPlay.domain;

import com.company.courseManager.domain.VodUploadInfo;

public class AliVodUploadInfo extends VodUploadInfo {
	
	
	private String videoId;
	/**
	 * vod upload requestid;
	 */
	private String requestId;
	
	/**
	 * 上传鉴权参数
	 */
	private String uploadAuth;
	/**
	 * 上传地址
	 */
	private String uploadAddress;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getUploadAuth() {
		return uploadAuth;
	}
	public void setUploadAuth(String uploadAuth) {
		this.uploadAuth = uploadAuth;
	}
	public String getUploadAddress() {
		return uploadAddress;
	}
	public void setUploadAddress(String uploadAddress) {
		this.uploadAddress = uploadAddress;
	}
	
	
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	@Override
	public String toString() {
		return "AliyunVodInfo [videoId=" + videoId + ", requestId=" + requestId + ", uploadAuth=" + uploadAuth
				+ ", uploadAddress=" + uploadAddress + "]" + super.toString();
	}
	
	
	
}
