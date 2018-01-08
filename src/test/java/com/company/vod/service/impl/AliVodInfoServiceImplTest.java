package com.company.vod.service.impl;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.company.courseManager.utils.AliyunUtils;
import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.service.AliVodService;
import com.company.videoPlay.service.impl.AliVodInfoServiceImpl;
import com.xinwei.nnl.common.domain.ProcessResult;
@RunWith(SpringRunner.class)
@SpringBootTest
public class AliVodInfoServiceImplTest {

	@Resource(name="aliVodService")
	private AliVodService aliVodService;
	@Before
	public void setup()
	{
		
	}
	@Test
	public void testGetPlayAuthFromAli()
	{
		AliVodInfoServiceImpl aliVodInfoServiceImpl = (AliVodInfoServiceImpl)aliVodService;
		AliVodPlayInfo aliVodPlayInfo = new AliVodPlayInfo();
		aliVodPlayInfo.setVideoId("95b2c4d05e24411aa5aa6f9afffd784b");
		
		ProcessResult reponse=null;
		try {
			reponse = aliVodInfoServiceImpl.getVideoPlayAuth(aliVodPlayInfo);
			System.out.println(reponse.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
