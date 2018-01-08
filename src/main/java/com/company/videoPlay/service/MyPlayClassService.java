package com.company.videoPlay.service;

import com.company.videoPlay.domain.AliVodPlayInfo;

public interface MyPlayClassService {
	
	public  int  updateMyPlayClass(AliVodPlayInfo aliVodPlayInfo);
	
	public  AliVodPlayInfo  queryMyPlayClass(AliVodPlayInfo aliVodPlayInfo);
	
}
