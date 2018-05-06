package com.company.videoPlay.service;

import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.domain.AliVodUploadInfo;
import com.xinwei.nnl.common.domain.ProcessResult;


public interface AliVodService {
	/**
	 * 阿里云视频服务上传请求
	 * @param aliyunVodinfo
	 * @return
	 */
	public  int  createUploadVideo(AliVodUploadInfo aliyunVodinfo);
	
	/**
	 * 阿里云视频服务更新上传请求
	 * @param aliyunVodinfo
	 * @return
	 */
	public  int  refreshUploadVideo(AliVodUploadInfo aliyunVodinfo);
	
	/**
	 * 请求播放视频
	 * @param aliVodPlayInfo
	 * @return
	 */
	public ProcessResult   requestPlayVideo(AliVodPlayInfo aliVodPlayInfo);

	/**
	 * 获取上传的视频的信息
	 * @param aliVodPlayInfo
	 * @return
	 */
	public ProcessResult  getVideoInfo(AliVodPlayInfo aliVodPlayInfo);
	
	
}
