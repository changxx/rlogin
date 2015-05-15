package com.rlogin.domain.base;

public class Query {

    protected Integer id;

    protected boolean paging;

    protected int     start_;

    /** 每页默认记录数 */
    protected int     rows_ = 10;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public int getStart_() {
        return start_;
    }

    public void setStart_(int start_) {
        this.start_ = start_;
    }

    public int getRows_() {
        return rows_;
    }

    public void setRows_(int rows_) {
        this.rows_ = rows_;
    }

}
