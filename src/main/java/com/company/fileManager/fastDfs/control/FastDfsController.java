package com.company.fileManager.fastDfs.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.company.courseManager.Const.CoursemanagerConst;
import com.company.fileManager.fastDfs.FastDFSClientWrapper;
import com.company.fileManager.fastDfs.FileManagerConst;
import com.xinwei.nnl.common.domain.ProcessResult;
import com.xinwei.nnl.common.util.JsonUtil;

@RestController

@ConditionalOnProperty(name = "fdfs.serverUrl")
@RequestMapping("/images")
public class FastDfsController {
	@Autowired
	private FastDFSClientWrapper fastDFSClientWrapper;
	
	@RequestMapping(value = "/getTest", method = RequestMethod.GET)
    public ProcessResult getTest() throws Exception {
    	ProcessResult processResult = new ProcessResult();
		
    	processResult.setRetCode(FileManagerConst.RESULT_FAILURE);
		
    	try {
			// 省略业务逻辑代码。。。
			
			processResult.setResponseInfo("success");
			processResult.setResponseInfo(FileManagerConst.RESULT_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return processResult;
       

    }
	// 上传图片
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ProcessResult upload(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ProcessResult processResult = new ProcessResult();
		
    	processResult.setRetCode(FileManagerConst.RESULT_FAILURE);
		
    	try {
			// 省略业务逻辑代码。。。
			String imgUrl = fastDFSClientWrapper.uploadFile(file);
			processResult.setResponseInfo(imgUrl);
			processResult.setRetCode(FileManagerConst.RESULT_SUCCESS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return processResult;
       

    }
    @RequestMapping(value = "/uploadThumbImage", method = RequestMethod.POST)
    public ProcessResult uploadThumbImage(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	ProcessResult processResult = new ProcessResult();
		
    	processResult.setRetCode(FileManagerConst.RESULT_FAILURE);
		
    	try {
			// 省略业务逻辑代码。。。
			String imgUrlList = fastDFSClientWrapper.uploadFileAndCrtThumbImageAuto(file);
			processResult.setResponseInfo(imgUrlList);
			processResult.setRetCode(FileManagerConst.RESULT_SUCCESS);
		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return processResult;
       

    }
}
