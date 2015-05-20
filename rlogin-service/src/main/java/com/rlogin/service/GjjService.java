package com.rlogin.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.rlogin.common.frame.json.Result;
import com.rlogin.domain.gjj.GjjAccDetail;
import com.rlogin.domain.gjj.GjjDetail;
import com.rlogin.domain.gjj.GjjUser;

public interface GjjService {

    /**
     * 获取用户数据
     * @param certinum
     * @param pass
     * @param cookie2
     * @return
     * @throws UnsupportedEncodingException
     */
    public Result fetchService(String certinum, String pass, String cookie);

    /**
     * 获取用户信息
     * @param certinum
     * @return
     */
    public GjjUser getGjjUser(String certinum);

    /**
     * 获取详情
     * @param certinum
     * @return
     */
    public GjjAccDetail getGjjAccDetail(String certinum);

    /**
     * 获取近一年详情
     * @param certinum
     * @return
     */
    public List<GjjDetail> getRecentGjjDetails(String certinum);

}
