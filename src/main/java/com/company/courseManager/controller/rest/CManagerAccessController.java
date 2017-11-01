package com.company.courseManager.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.domain.AliVodUploadInfo;
import com.xinwei.nnl.common.domain.ProcessResult;

/**
 * course manager access controller
 * @author helmsli
 *
 */
@RestController
@RequestMapping("/vodManager")
public class CManagerAccessController {
	@RequestMapping(method = RequestMethod.POST,value = "{userId}/{courseId}/confCourses")
	public  ProcessResult createUploadVideo(HttpServletRequest request,@RequestBody AliVodUploadInfo aliyunVodInfo) {
		return null;
		
	}
	
}
