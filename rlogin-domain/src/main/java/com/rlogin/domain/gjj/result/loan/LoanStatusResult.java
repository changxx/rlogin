package com.rlogin.domain.gjj.result.loan;

/**
 * 审批状态
 * Created by changxx on 15/5/23.
 */
public class LoanStatusResult {

    private LoanStatusData data;

    private Integer returnCode;

    public LoanStatusData getData() {
        return data;
    }

    public void setData(LoanStatusData data) {
        this.data = data;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }
}
