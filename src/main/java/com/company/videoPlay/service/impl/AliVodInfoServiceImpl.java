package com.company.videoPlay.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.company.courseManager.Const.VodServiceConst;
import com.company.courseManager.domain.AliyunSignature;
import com.company.courseManager.utils.AliyunUtils;
import com.company.videoPlay.domain.AliVodMetaData;
import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.domain.AliVodUploadInfo;
import com.company.videoPlay.service.AliVodService;
import com.company.videoPlay.service.MyPlayClassService;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;
@Service("aliVodService")
public class AliVodInfoServiceImpl implements AliVodService,InitializingBean {
	@Value("${alidayuVod.accessKeyId}")
	private String accessKeyId;
	
	@Value("${alidayuVod.playAuthUrl:http://vod.cn-shanghai.aliyuncs.com/}")
	private String playAuthUrl;
	
	
	@Value("${alidayuVod.accessKeySecret}")
	private String accessKeySecret;
	
	
	@Value("${student.myplaytask.maxSchedulerThread:5}")
	private int myplayTaskmaxThread;
	//后续需要保证每一步的任务调度是均等的，即如果某一步的任务过多会侵占别的任务
	@Value("${student.myplaytask.initSchedulerThread:2}")
	private int myplayTaskinitThread;
	
	@Value("${student.myplaytask.SchedulerkeepAliveTime:300}")
	private int myplayTaskkeepAliveTime;
	
	@Value("${student.myplaytask.SchedulerQueneSize:30000}")
	private int myplayTaskQueneSize;
	
	@Resource(name="myPlayClassService")
	private MyPlayClassService myPlayClassService;
	
	private RestTemplate restTemplate=new RestTemplate();
	
	private DefaultAcsClient aliyunClient;
	
	/**
	 * 更新学员学习中心的课时信息任务配置
	 */
	//java.util.concurrent.ConcurrentSkipListSet<E>
	private BlockingQueue<Runnable> myplayTaskworkQueue =null;
	private ThreadPoolExecutor myplayTaskPool = null;//new ThreadPoolExecutor(myplayTaskinitThread, myplayTaskmaxThread, myplayTaskkeepAliveTime, TimeUnit.SECONDS,myplayTaskworkQueue);

	
    /**
     * aliyunClient = new DefaultAcsClient(
    DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));
	
     */
	@Override
	public int createUploadVideo(AliVodUploadInfo aliyunVodinfo) {
		// TODO Auto-generated method stub
		int iRet =  VodServiceConst.RESULT_Error_Fail;
		CreateUploadVideoRequest request = new CreateUploadVideoRequest();
	        CreateUploadVideoResponse response = null;
	        try {
	              /*必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", ".dv", ".flv", ".f4v", ".gif", ".m2t", ".m3u8", ".m4v", ".mj2", ".mjpeg", ".mkv", ".mov", ".mp4", ".mpe", ".mpg", ".mpeg", ".mts", ".ogg", ".qt", ".rm", ".rmvb", ".swf", ".ts", ".vob", ".wmv", ".webm"".aac", ".ac3", ".acm", ".amr", ".ape", ".caf", ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）*/
	              request.setFileName(aliyunVodinfo.getLocalFilename());
	              //必选，视频标题
	              request.setTitle(aliyunVodinfo.getFileTitle());
	              //可选，分类ID
	              try {
	            	  if(!StringUtils.isEmpty(aliyunVodinfo.getCategory()))
	            	  {
	            		  request.setCateId(Integer.parseInt(aliyunVodinfo.getCategory()));
	            	  }
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 request.setCateId(0);
				}
	              //可选，视频标签，多个用逗号分隔
	              request.setTags(aliyunVodinfo.getTag());
	              //可选，视频描述
	              request.setDescription(aliyunVodinfo.getDesc());
	              //可选，视频源文件字节数
	              request.setFileSize(0l);
	              response = aliyunClient.getAcsResponse(request);
					aliyunVodinfo.setRequestId(response.getRequestId());
					aliyunVodinfo.setUploadAddress(response.getUploadAddress());
					aliyunVodinfo.setUploadAuth(response.getUploadAuth());
					aliyunVodinfo.setVideoId(response.getVideoId());
					
	              iRet = VodServiceConst.RESULT_Success;
	        } catch (ServerException e) {
	              System.out.println("CreateUploadVideoRequest Server Exception:");
	              e.printStackTrace();
	        } catch (ClientException e) {
	              System.out.println("CreateUploadVideoRequest Client Exception:");
	              e.printStackTrace();
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        
	        return iRet;
	}

	@Override
	public int refreshUploadVideo(AliVodUploadInfo aliyunVodinfo) {
		// TODO Auto-generated method stub
		 RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
	        RefreshUploadVideoResponse response = null;
	        try {
	              request.setVideoId(aliyunVodinfo.getVideoId());
	              response = aliyunClient.getAcsResponse(request);
	        } catch (ServerException e) {
	              System.out.println("RefreshUploadVideoRequest Server Exception:");
	              e.printStackTrace();
	        } catch (ClientException e) {
	              System.out.println("RefreshUploadVideoRequest Client Exception:");
	              e.printStackTrace();
	        }
	        System.out.println("RequestId:" + response.getRequestId());
	        System.out.println("UploadAuth:" + response.getUploadAuth());
		return 0;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		aliyunClient = new DefaultAcsClient(
		DefaultProfile.getProfile("cn-shanghai",accessKeyId,accessKeySecret));
				
		
		myplayTaskworkQueue =new ArrayBlockingQueue<Runnable>(myplayTaskQueneSize);
		
		myplayTaskPool = new ThreadPoolExecutor(myplayTaskinitThread, myplayTaskmaxThread, myplayTaskkeepAliveTime, TimeUnit.SECONDS,myplayTaskworkQueue);

	}

	@Override
	public ProcessResult requestPlayVideo(AliVodPlayInfo aliVodPlayInfo) {
		// TODO Auto-generated method stub
		MyplayUpdaterTask myplayUpdaterTask=  new MyplayUpdaterTask(this.myPlayClassService,aliVodPlayInfo);
		this.myplayTaskPool.execute(myplayUpdaterTask);
		return getVideoPlayAuth(aliVodPlayInfo);
	}
	
	@Override
	public ProcessResult   getVideoInfo(AliVodPlayInfo aliVodPlayInfo)
	{
		ProcessResult processResult= new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		GetVideoInfoRequest request = new GetVideoInfoRequest();
		request.setVideoId(aliVodPlayInfo.getVideoId());
		
		GetVideoInfoResponse response = null;
		try {
			response = this.aliyunClient.getAcsResponse(request);
			processResult.setResponseInfo(JsonUtil.toJson(response.getVideo()));
			processResult.setRetCode(VodServiceConst.RESULT_Success);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return processResult;
		
	}
	
	/**
	 * 从阿里服务器查询播放授权
	 * @param aliVodPlayInfo
	 * @return
	 */
	public ProcessResult getVideoPlayAuth(AliVodPlayInfo aliVodPlayInfo) {
		ProcessResult processResult= new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
		request.setVideoId(aliVodPlayInfo.getVideoId());
		
		GetVideoPlayAuthResponse response = null;
		try {
			response = this.aliyunClient.getAcsResponse(request);
			AliVodPlayInfo retAliVodPlayInfo = new AliVodPlayInfo();
			
			retAliVodPlayInfo.setPlayAuth(response.getPlayAuth());
			retAliVodPlayInfo.setRequestId(response.getRequestId());
			retAliVodPlayInfo.setVideoId(aliVodPlayInfo.getVideoId());
			AliVodMetaData aliVodMetaData = new AliVodMetaData();
			aliVodMetaData.setCoverURL(response.getVideoMeta().getCoverURL());
			aliVodMetaData.setDuration(response.getVideoMeta().getDuration());
			aliVodMetaData.setStatus(response.getVideoMeta().getStatus());
			aliVodMetaData.setTitle(response.getVideoMeta().getTitle());
			aliVodMetaData.setVideoId(response.getVideoMeta().getVideoId());
			retAliVodPlayInfo.setVideoMeta(aliVodMetaData);
			processResult.setResponseInfo(retAliVodPlayInfo);
			processResult.setRetCode(VodServiceConst.RESULT_Success);
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return processResult;
	}
	
	/**
	 * 从阿里的服务器获取playauth,还没有调试通过
	 * @param aliVodPlayInfo
	 * @return
	 */
	public ProcessResult getPlayAuthFromAliByAjax(AliVodPlayInfo aliVodPlayInfo)
	{
		
		
		HttpHeaders headers = new HttpHeaders();
		/* 这个对象有add()方法，可往请求头存入信息 */       
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		
		
		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		
		AliyunSignature aliyunSignature = new AliyunSignature();
		aliyunSignature.setAccessKeyId(this.accessKeyId);
		aliyunSignature.setAction(AliyunUtils.Action_GetVideoPlayAuth);
		aliyunSignature.setFormat(AliyunUtils.Format_JSON);
		aliyunSignature.setSignatureMethod(AliyunUtils.Sign_method_HMACSHA1);
		ResponseEntity<AliVodPlayInfo> results=null;
		try {
			String signatureUrl = AliyunUtils.formatSignatureUrl(AliyunUtils.HttpMethod_Get, accessKeySecret, aliyunSignature);
			String url = playAuthUrl + "?" + signatureUrl;
			results = restTemplate.exchange(url,HttpMethod.GET, null, AliVodPlayInfo.class);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		/*上面这句返回的是往 url发送 post请求 请求携带信息为entity时返回的结果信息
		String.class 是可以修改的，例如User.class，这样你就可以有 User resultUser接返回结果了*/
		return null;
	}
	
	
}
