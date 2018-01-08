package com.company.videoPlay.controller.rest;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
/**
 * 点播业务接入请求
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.courseManager.Const.VodServiceConst;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.videoPlay.domain.AccessContext;
import com.company.videoPlay.domain.AliVodPlayInfo;
import com.company.videoPlay.domain.AliVodUploadInfo;
import com.company.videoPlay.domain.RequestAjax;
import com.company.videoPlay.service.AliVodService;
import com.company.videoPlay.service.MyPlayClassService;
import com.google.gson.reflect.TypeToken;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

@RestController
@RequestMapping("/vod")
public class VodController {
	@Resource(name="aliVodService")
	private AliVodService aliVodService;
	
	@Resource(name="myPlayClassService")
	private MyPlayClassService myPlayClassService;
	
	/**
	 * 单个文件的上传请求
	 * @param request
	 * @param aliyunVodInfo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "requestUploadVideo")
	public  ProcessResult createUploadVideo(HttpServletRequest request,@RequestBody AliVodUploadInfo aliyunVodInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		try {
			AccessContext AccessContext = new AccessContext();
			int iRet = aliVodService.createUploadVideo(aliyunVodInfo);
			processResult.setRetCode(iRet);
			if(iRet == VodServiceConst.RESULT_Success)
			{
				processResult.setResponseInfo(aliyunVodInfo);	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
	@RequestMapping(method = RequestMethod.POST,value = "requestUpVideoList")
	public  ProcessResult createUploadVideos(HttpServletRequest request,@RequestBody RequestAjax requestAjax) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		try {
			//从客户端请求的字符串中解析出对象列表
			List<AliVodUploadInfo> retList = new ArrayList<AliVodUploadInfo>();
			List<AliVodUploadInfo> aliyunVodInfoList =  JsonUtil.fromJson(requestAjax.getReqParms(), new TypeToken<List<AliVodUploadInfo>>() {}.getType());
			int iRet  = VodServiceConst.RESULT_Success;
			for(AliVodUploadInfo aliVodUploadInfo:aliyunVodInfoList)
			{
				int iSingleRet = VodServiceConst.RESULT_Success;
				for(int i=0;i<3;i++)
				{
					iSingleRet = aliVodService.createUploadVideo(aliVodUploadInfo);
					if(iSingleRet==VodServiceConst.RESULT_Success)
					{
						break;
					}
					else
					{
						Thread.sleep(100);
					}
				}//end for i=3
				
				if(iSingleRet != VodServiceConst.RESULT_Success)
				{
					iRet = 	iSingleRet;
					retList.add(null);
				}
				else
				{
					retList.add(aliVodUploadInfo);
				}
				
			}//end for
			processResult.setRetCode(iRet);
			processResult.setResponseInfo(retList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
	/**
	 * 请求单个文件的播放请求
	 * @param request
	 * @param aliVodPlayInfo
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "requestPlayAuth")
	public  ProcessResult requestPlayAuth(HttpServletRequest request,@RequestBody AliVodPlayInfo aliVodPlayInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		try {
			processResult = aliVodService.requestPlayVideo(aliVodPlayInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	/**
	 * 请求多个文件的播放请求token
	 * @param request
	 * @param requestAjax
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = "requestPlayAuthList")
	public  ProcessResult requestPlayAuthList(HttpServletRequest request,@RequestBody RequestAjax requestAjax) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		
		
		try {
			int iRet = VodServiceConst.RESULT_Success;
			//从客户端请求的字符串中解析出对象列表
			List<AliVodPlayInfo> retList = new ArrayList<AliVodPlayInfo>();
			List<AliVodPlayInfo> aliVodPlayInfos =  JsonUtil.fromJson(requestAjax.getReqParms(), new TypeToken<List<AliVodPlayInfo>>() {}.getType());
			for(AliVodPlayInfo aliVodPlayInfo:aliVodPlayInfos)
			{
			//List<Person> persons =gson.fromJson(json, new TypeToken<List<Person>>() {}.getType());
				ProcessResult singlepResult = null;
				//如果失败，重发三次；
				for(int i=0;i<3;i++)
				{
					
					singlepResult = aliVodService.requestPlayVideo(aliVodPlayInfo);
					if(singlepResult.getRetCode()==VodServiceConst.RESULT_Success)
					{
						break;
					}
					else
					{
						Thread.sleep(100);
					}
				}
				if(singlepResult.getRetCode()!=VodServiceConst.RESULT_Success)
				{
					iRet = singlepResult.getRetCode();
				}
				if(singlepResult.getResponseInfo()==null)
				{
					retList.add(null);
				}
				else
				{
					retList.add((AliVodPlayInfo)singlepResult.getResponseInfo());
				}
				
			}//end of for
			processResult.setResponseInfo(retList);
			processResult.setRetCode(iRet);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	@RequestMapping(method = RequestMethod.POST,value = "getMyPlayClass")
	public  ProcessResult getMyPlayClass(HttpServletRequest request,@RequestBody AliVodPlayInfo aliVodPlayInfo) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VodServiceConst.RESULT_Error_Fail);
		
		
		try {
			int iRet = VodServiceConst.RESULT_Success;
			AliVodPlayInfo queryAliVodPlayInfo = myPlayClassService.queryMyPlayClass(aliVodPlayInfo);
			if(queryAliVodPlayInfo==null)
			{
				iRet = CoursemanagerConst.RESULT_FAILURE_MYPLAY_NULL;
				return ControllerUtils.getErrorResponse(iRet, CoursemanagerConst.RESULT_FAILURE_STRING_MYPLAY_NULL);
			}
			processResult.setRetCode(iRet);
			processResult.setResponseInfo(queryAliVodPlayInfo);
			return processResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, CoursemanagerConst.RESULT_FAILURE, null);
		}
		
	}
}
