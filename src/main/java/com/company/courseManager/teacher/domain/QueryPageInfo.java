package com.company.courseManager.teacher.domain;

import java.io.Serializable;
import java.util.List;

import com.company.userOrder.domain.UserOrder;

public class QueryPageInfo<T> implements Serializable {
	private int endRow;
	private int firstPage;
	private boolean hasNextPage;
	private boolean hasPreviousPage;
	private boolean isFirstPage;
	private boolean isLastPage;
	private int lastPage;
	private int navigateFirstPage;
	private int navigateLastPage;
	private int navigatePages;
	private int nextPage;
	private int pageNum;
	private int pageSize;
	private int pages;
	private int prePage;
	private int size;
	private int startRow;
	private int total;
	private List<T> queryResult;
	public int getEndRow() {
		return endRow;
	}
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	public int getFirstPage() {
		return firstPage;
	}
	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}
	public boolean isHasNextPage() {
		return hasNextPage;
	}
	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;
	}
	public boolean isHasPreviousPage() {
		return hasPreviousPage;
	}
	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;
	}
	public boolean isFirstPage() {
		return isFirstPage;
	}
	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}
	public boolean isLastPage() {
		return isLastPage;
	}
	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	public int getLastPage() {
		return lastPage;
	}
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
	public int getNavigateFirstPage() {
		return navigateFirstPage;
	}
	public void setNavigateFirstPage(int navigateFirstPage) {
		this.navigateFirstPage = navigateFirstPage;
	}
	public int getNavigateLastPage() {
		return navigateLastPage;
	}
	public void setNavigateLastPage(int navigateLastPage) {
		this.navigateLastPage = navigateLastPage;
	}
	public int getNavigatePages() {
		return navigatePages;
	}
	public void setNavigatePages(int navigatePages) {
		this.navigatePages = navigatePages;
	}
	public int getNextPage() {
		return nextPage;
	}
	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getPrePage() {
		return prePage;
	}
	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<T> getQueryResult() {
		return queryResult;
	}
	public void setQueryResult(List<T> queryResult) {
		this.queryResult = queryResult;
	}
	
	
}
