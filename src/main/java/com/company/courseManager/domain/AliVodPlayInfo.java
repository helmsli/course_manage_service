package com.company.courseManager.domain;

import java.io.Serializable;

public class AliVodPlayInfo implements Serializable {
	/**
	 * 视频ID
	 */
	private String videoId;
	/**
	 * 请求ID
	 */
	private String requestId;
	/**
	 * 视频Meta信息
	 */
	private AliVodMetaData videoMeta;
	/**
	 * 视频播放凭证
	 */
	private String playAuth;
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public AliVodMetaData getVideoMeta() {
		return videoMeta;
	}
	public void setVideoMeta(AliVodMetaData videoMeta) {
		this.videoMeta = videoMeta;
	}
	public String getPlayAuth() {
		return playAuth;
	}
	public void setPlayAuth(String playAuth) {
		this.playAuth = playAuth;
	}
	@Override
	public String toString() {
		return "AliVodPlayInfo [videoId=" + videoId + ", requestId=" + requestId + ", videoMeta=" + videoMeta
				+ ", playAuth=" + playAuth + "]";
	}
	
	
	
}
