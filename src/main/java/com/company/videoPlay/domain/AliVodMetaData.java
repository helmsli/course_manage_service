package com.company.videoPlay.domain;

import java.io.Serializable;

public class AliVodMetaData implements Serializable{
	private String videoId;
	private String title;
	/**
	 * 单位秒
	 */
	private float duration;
	private String coverURL;
	private String status;
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getDuration() {
		return duration;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public String getCoverURL() {
		return this.coverURL;
	}
	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "AliVodMetaData [videoId=" + videoId + ", title=" + title + ", duration=" + duration + ", coverURL="
				+ coverURL + ", status=" + status + "]";
	}
	
	
}
