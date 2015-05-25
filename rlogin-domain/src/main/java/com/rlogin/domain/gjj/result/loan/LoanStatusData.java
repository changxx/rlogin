package com.rlogin.domain.gjj.result.loan;

import com.rlogin.domain.gjj.result.replay.GjjCReplayDetail;

import java.util.List;

/**
 * 贷款数据
 * Created by changxx on 15/5/23.
 */
public class LoanStatusData {

    private Integer pageSize;

    private Integer pageCount;

    private Integer currentPage;

    private Integer rows;

    private String tableid;

    private List<LoanStatus> data;

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

    public List<LoanStatus> getData() {
        return data;
    }

    public void setData(List<LoanStatus> data) {
        this.data = data;
    }
}
