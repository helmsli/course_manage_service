package com.company.elasticsearch.domain;

import com.company.userOrderPlatform.domain.QueryPageRequest;

public class SearchRequest extends QueryPageRequest {
	String searchContent;

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	
}
