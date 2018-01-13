package com.company.elasticsearch.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.coursestudent.domain.DraftDocument;
import com.company.elasticsearch.domain.CourseSearch;
import com.company.elasticsearch.domain.SearchRequest;
import com.company.elasticsearch.service.SearchEByDefaultService;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.videodb.domain.Courses;
import com.xinwei.nnl.common.domain.ProcessResult;

@RestController
@RequestMapping("/courseSearch")
public class CourseSearchController {
	@Autowired
	private SearchEByDefaultService searchEByDefaultService;
	
	@PostMapping(value = "/saveCourse")
	public  ProcessResult saveCourse(@RequestBody CourseSearch courses) {
		try {
			searchEByDefaultService.saveCourse(courses);
			return ControllerUtils.getSuccessResponse(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, -1, null);
		}
	}
	
	@PostMapping(value = "/search")
	public  ProcessResult searchCourse(@RequestBody SearchRequest searchContent) {
		try {
			return searchEByDefaultService.searchCourse(searchContent.getSearchContent(), searchContent);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ControllerUtils.getFromResponse(e, -1, null);
		}
	}
	
}
