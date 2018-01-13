package com.company.elasticsearch.service;

import java.util.List;

import com.company.elasticsearch.domain.CourseSearch;
import com.company.userOrderPlatform.domain.QueryPageRequest;
import com.xinwei.nnl.common.domain.ProcessResult;

public interface SearchEByDefaultService {
	String saveCourse(CourseSearch course);
	/**
     * 根据关键词，function score query 权重分分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param searchContent
     * @return
     */
	ProcessResult searchCourse(String searchContent,QueryPageRequest queryPageRequest);

}
