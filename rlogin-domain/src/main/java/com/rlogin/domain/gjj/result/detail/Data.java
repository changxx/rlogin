package com.rlogin.domain.gjj.result.detail;

import java.util.List;

public class Data {
	private Integer pageSize;

	private Integer pageCount;

	private Integer currentPage;

	private Integer rows;

	private String tableid;

	private List<Detail> data;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getTableid() {
		return tableid;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public List<Detail> getData() {
		return data;
	}

	public void setData(List<Detail> data) {
		this.data = data;
	}

}
