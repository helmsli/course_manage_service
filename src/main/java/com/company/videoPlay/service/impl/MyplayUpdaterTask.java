package com.company.videoPlay.service.impl;

import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.service.MyPlayClassService;

public class MyplayUpdaterTask implements Runnable {
	private MyPlayClassService myPlayClassService;
	private AliVodPlayInfo aliVodPlayInfo;
	public  MyplayUpdaterTask(MyPlayClassService myPlayClassService,AliVodPlayInfo aliVodPlayInfo)
	{
		this.myPlayClassService = myPlayClassService;
		this.aliVodPlayInfo = aliVodPlayInfo;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
        //更新播放进度，包括内存和数据库
		try {
			this.myPlayClassService.updateMyPlayClass(aliVodPlayInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
