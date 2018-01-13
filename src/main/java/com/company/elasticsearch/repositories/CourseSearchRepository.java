package com.company.elasticsearch.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import com.company.elasticsearch.domain.CourseSearch;
@Repository
public interface CourseSearchRepository extends ElasticsearchRepository<CourseSearch, String> {

	
}
