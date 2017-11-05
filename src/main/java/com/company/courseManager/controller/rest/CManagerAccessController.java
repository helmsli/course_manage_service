package com.company.courseManager.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.courseManager.domain.AliVodUploadInfo;
import com.company.videodb.Const.VideodbConst;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

/**
 * course manager access controller
 * @author helmsli
 *
 */
@RestController
@RequestMapping("/courseManager")
public class CManagerAccessController {
	
	
	@RequestMapping(method = RequestMethod.POST,value = "{dbid}/{courseId}/confCourses")
	public  ProcessResult configureCourses(@PathVariable String dbid,@PathVariable String courseId,@RequestBody Courses courses) {
		ProcessResult processResult = new ProcessResult();
		processResult.setRetCode(VideodbConst.RESULT_FAILURE);
		try {
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return processResult;
	}
	
}
