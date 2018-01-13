package com.company.courseManager;

import java.util.Calendar;

import org.springframework.web.client.RestTemplate;

import com.company.elasticsearch.domain.CourseSearch;
import com.xinwei.nnl.common.domain.ProcessResult;

public class TestCourseSearch {
	private String baseUrl = "http://www.chunzeacademy.com:8080/courseSearch/saveCourse";
	private RestTemplate restTemplate = new RestTemplate();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestCourseSearch testCourseSearch  =new TestCourseSearch();
		testCourseSearch.saveCourseSearch();
	}
	
	public void saveCourseSearch()
	{
		CourseSearch courseSearch = new CourseSearch();
		courseSearch.setCategory("category1");
		courseSearch.setCheckCrc("checkCrc");
		courseSearch.setCourseAvatar("courseAvatar");
		courseSearch.setCourseChapter("chapter");
		courseSearch.setCourseId("courseId");
		courseSearch.setCourseInfo("wo shi course Info ");
		courseSearch.setCreateTime(Calendar.getInstance().getTime());
		courseSearch.setCredit(100);
		courseSearch.setDetail("detail");
		courseSearch.setDifficultyLevel("defficultylevel");
		courseSearch.setExpireDate(Calendar.getInstance().getTime());
		courseSearch.setFitPeople("fitPeople");
		courseSearch.setOriginalPrice(12.12f);
		courseSearch.setOwner("liguogiang");
		courseSearch.setPriceVer(100);
		courseSearch.setRealPrice(12.11f);
		courseSearch.setSearchKeys("人工智能 大数据 java");
		courseSearch.setSellAmount(100);
		courseSearch.setStatus(1);
		courseSearch.setTeacherName("李国强 刘凤芹");
		courseSearch.setTitle(" 人工智能title 大数据title java");
		courseSearch.setTotalRank(200);
		baseUrl = "http://www.chunzeacademy.com:8080/courseSearch/saveCourse";
		ProcessResult processResult = restTemplate.postForObject(baseUrl, courseSearch, ProcessResult.class);
		System.out.println(processResult);
		
	}

}
