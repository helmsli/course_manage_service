package com.company.elasticsearch.service;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.company.elasticsearch.domain.CourseSearch;
import com.company.elasticsearch.repositories.CourseSearchRepository;
import com.company.platform.controller.rest.ControllerUtils;
import com.company.userOrderPlatform.domain.QueryPageRequest;
import com.xinwei.nnl.common.domain.ProcessResult;
@Service("searchEByDefaultService")
public class SearchEByDefaultServiceImpl implements SearchEByDefaultService {
	 private static final Logger LOGGER = LoggerFactory.getLogger(SearchEByDefaultServiceImpl.class);

	  
	@Autowired
	private CourseSearchRepository courseEByDefaultRepository;

	@Override
	public String saveCourse(CourseSearch course) {
		// TODO Auto-generated method stub
		courseEByDefaultRepository.save(course);
		return null;
	}

	@Override
	public ProcessResult searchCourse(String searchContent, QueryPageRequest queryPageRequest) {
		  @SuppressWarnings("deprecation")
		Pageable pageable = PageRequest.of(queryPageRequest.getPageNum(), queryPageRequest.getPageSize());
		  
		  FilterFunctionBuilder[] lists = new FilterFunctionBuilder[5];
		  lists[0] = new FilterFunctionBuilder(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("searchKeys", searchContent)),
                  ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"));
		  lists[1] = new FilterFunctionBuilder(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("category", searchContent)),
          		ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"));
		  lists[2] = new FilterFunctionBuilder(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", searchContent)),
	        		ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"));
		  lists[3] = new FilterFunctionBuilder(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("courseInfo", searchContent)),
				  ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"));
		  lists[4] = new FilterFunctionBuilder(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("teacherName", searchContent)),
				  ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"));
	        // Function Score Query
		  FunctionScoreQueryBuilder functionScoreQueryBuilder = new FunctionScoreQueryBuilder(lists);
		  /*
	        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery()
	                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("searchKeys", searchContent)),
	                    ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"))
	                .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("category", searchContent)),
	                		ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"))
	        .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", searchContent)),
	        		ScoreFunctionBuilders.fieldValueFactorFunction("totalRank"))
	        .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("courseInfo", searchContent)),
                    ScoreFunctionBuilders.weightFactorFunction(100))
	        .add(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("teacherName", searchContent)),
                    ScoreFunctionBuilders.weightFactorFunction(100));
*/
	        // 创建搜索 DSL 查询
	        SearchQuery searchQuery = new NativeSearchQueryBuilder()
	                .withPageable(pageable)
	                .withQuery(functionScoreQueryBuilder).build();

	        LOGGER.info("\n searchCity(): searchContent [" + searchContent + "] \n DSL  = \n " + searchQuery.getQuery().toString());

	        Page<CourseSearch> searchPageResults = courseEByDefaultRepository.search(searchQuery);
	        ProcessResult result = ControllerUtils.getSuccessResponse(null);
	        result.setResponseInfo(searchPageResults);
	        //return searchPageResults.getContent();
	        return result;
	}

}
