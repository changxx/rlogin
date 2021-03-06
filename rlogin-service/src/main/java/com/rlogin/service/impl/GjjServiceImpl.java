package com.rlogin.service.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;

import com.rlogin.dao.mapper.gjj.*;
import com.rlogin.domain.gjj.*;
import com.rlogin.domain.gjj.result.loan.LoanStatus;
import com.rlogin.domain.gjj.result.loan.LoanStatusResult;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rlogin.common.frame.json.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.NJReserveResponseHandler;
import com.rlogin.common.util.DateUtils;
import com.rlogin.common.util.JSONUtils;
import com.rlogin.domain.gjj.result.GjjResult;
import com.rlogin.domain.gjj.result.detail.GjjCDetail;
import com.rlogin.domain.gjj.result.detail.GjjDetailResult;
import com.rlogin.domain.gjj.result.replay.GjjCReplayDetail;
import com.rlogin.domain.gjj.result.replay.GjjReplayDetailResult;
import com.rlogin.service.GjjService;

import javax.annotation.PostConstruct;

@Service
public class GjjServiceImpl implements GjjService {

    private static final Logger log = LoggerFactory.getLogger(GjjServiceImpl.class);

    @Autowired
    private GjjUserMapper gjjUserMapper;

    @Autowired
    private GjjAccDetailMapper gjjAccDetailMapper;

    @Autowired
    private GjjDetailMapper gjjDetailMapper;

    @Autowired
    private GjjRepayDetailMapper gjjRepayDetailMapper;

    @Autowired
    private GjjLoanMapper gjjLoanMapper;

    @Autowired
    private GjjRepayPlanMapper gjjRepayPlanMapper;

    @Autowired
    private GjjLoanStatusMapper gjjLoanStatusMapper;

    private static BlockingQueue<GjjAsynDomain> gjjAsynDomainQuery = new LinkedBlockingQueue<GjjAsynDomain>();

    @Override
    public Result fetchService(String certinum, String pass, String cookie) {
        Result result = new Result();
        // 拿到数据总线
        PoolSelect poolSelect = this.getPoolSelectByHtml(cookie);

        HttpClient httpClient = HttpClientSupport.getHttpClient();
        HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("accname", poolSelect.get_ACCNAME()));
        params.add(new BasicNameValuePair("accnum", poolSelect.get_ACCNUM()));
        params.add(new BasicNameValuePair("certinum", certinum));
        params.add(new BasicNameValuePair("prodcode", "1"));
        params.add(new BasicNameValuePair("_PROCID", "80000003"));
        params.add(new BasicNameValuePair("_PAGEID", "step1"));

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e1) {
            log.error("", e1);
        }
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        log.info("公积金详情json：{}", responseBody);
        GjjResult gjjResult = JSONUtils.jsonToObject(responseBody, GjjResult.class);
        //  纪录公积金用户
        GjjUser gjjUser = this.recordUser(certinum, pass, poolSelect, gjjResult);
        gjjAsynDomainQuery.add(new GjjAsynDomain(gjjUser, cookie));

        result.setCode(gjjUser != null ? Result.SUCCESS : Result.ERROR);
        return result;
    }


    @PostConstruct
    public void recrodTask() {
        log.info("启动纪录线程池");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.submit(new GjjAsynRecorder(this));
        }
    }

    /**
     * 纪录动作
     *
     * @throws InterruptedException
     */
    public void record() throws InterruptedException {
        GjjAsynDomain gjjAsynDomain = this.getGjjAsynDomainQuery().take();

        // 记录公积金明细
        this.recordGjjDetail(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());

        // 记录贷款明细
        this.recordLoanDetail(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());

        // 纪录贷款信息
        Set<String> loanAccses = this.recordLoan(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());

        // 纪录还款计划
        this.recordRepayPlan(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser(), loanAccses);

        // 纪录贷款审批状态
        this.recordLoanStatus(gjjAsynDomain.getCookie(), gjjAsynDomain.getGjjUser());
    }


    /**
     * 记录公积金明细
     *
     * @param cookie
     * @param gjjUser
     * @return
     */
    public Integer recordGjjDetail(String cookie, GjjUser gjjUser) {
        log.info("记录公积金明细 start, cookie: {}, gjjUser: {}", cookie, gjjUser);
        Map<String, String> extParms = new HashMap<String, String>();
        extParms.put("begdate", "2000-01-01");
        extParms.put("enddate", new DateTime().toString("yyyy-MM-dd"));
        extParms.put("accnum", gjjUser.getGjjAcc());
        extParms.put("accname", gjjUser.getName());

        String dataPool = this.getTabelDataPool(cookie, gjjUser, "70000002", extParms);
        // 纪录公积金详情
        String gjjDetailListStr = this.getDetailList(cookie, dataPool, gjjUser, extParms);

        // 公积金明细列表
        GjjDetailResult result = JSONUtils.jsonToObject(gjjDetailListStr, GjjDetailResult.class);

        List<GjjDetail> gjjDetails = new ArrayList<GjjDetail>();
        if (result != null && result.getData() != null && result.getData().getData() != null) {
            for (GjjCDetail detail : result.getData().getData()) {
                gjjDetails.add(this.resultDetailToGjjDetail(detail, gjjUser));
            }
            if (gjjDetails != null && gjjDetails.size() > 0) {
                // 重新插入以前缴纳纪录
                GjjDetailExample detailExample = new GjjDetailExample();
                detailExample.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId());
                Integer delDetailNum = gjjDetailMapper.deleteByExample(detailExample);
                log.info("删除" + gjjUser.getAccId() + ":" + delDetailNum + "条");
            }
            this.insertDetailBatch(gjjDetails);
        }
        log.info("记录公积金明细结束, 记录{}条, 用户: {}", gjjDetails.size(), gjjUser.getName());
        return gjjDetails.size();
    }

    /**
     * 记录贷款明细
     *
     * @param cookie
     * @param gjjUser
     */
    public void recordLoanDetail(String cookie, GjjUser gjjUser) {
        log.info("记录贷款明细开始, cookie: {}, gjjUser: {}", cookie, gjjUser);
        Map<String, String> extParms = new HashMap<String, String>();
        extParms.put("begdate", "2000-01-01");
        extParms.put("enddate", new DateTime().toString("yyyy-MM-dd"));
        extParms.put("accnum", gjjUser.getGjjAcc());
        extParms.put("accname", gjjUser.getName());
        extParms.put("certinum", gjjUser.getAccId());
        extParms.put("loanaccnum", "");

        String dataPool = this.getTabelDataPool(cookie, gjjUser, "60000001", extParms);

        String loanDetailListStr = this.getLoanDetailList(cookie, dataPool, gjjUser);
        log.info("贷款明细json: {}", loanDetailListStr);

        GjjReplayDetailResult result = JSONUtils.jsonToObject(loanDetailListStr, GjjReplayDetailResult.class);

        List<GjjRepayDetail> repayDetails = new ArrayList<GjjRepayDetail>();

        if (result != null && result.getData() != null && result.getData().getData() != null) {
            for (GjjCReplayDetail repayDetail : result.getData().getData()) {
                repayDetails.add(this.resultToGjjRepayDetail(repayDetail, gjjUser));
            }
            if (repayDetails.size() > 0) {
                // 把以前的删除了
                GjjRepayDetailExample example = new GjjRepayDetailExample();
                example.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId());
                Integer delRepayDetailNum = gjjRepayDetailMapper.deleteByExample(example);
                log.info("删除" + gjjUser.getAccId() + ":" + delRepayDetailNum + "条");
            }
            this.insertRepayDetailBatch(repayDetails);
        }

        log.info("记录贷款明细结束, 记录{}条, 用户: {}", repayDetails.size(), gjjUser.getName());
    }

    /**
     * 纪录贷款信息
     *
     * @param cookie
     * @param gjjUser
     */
    public Set<String> recordLoan(String cookie, GjjUser gjjUser) {

        GjjRepayDetailExample gjjRepayDetailExample = new GjjRepayDetailExample();
        gjjRepayDetailExample.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId());
        List<GjjRepayDetail> gjjRepayDetails = gjjRepayDetailMapper.selectByExample(gjjRepayDetailExample);

        Set<String> loanAccses = new HashSet<String>();
        for (GjjRepayDetail detail : gjjRepayDetails) {
            loanAccses.add(detail.getLoanAcc());
        }

        String url = "http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("$page", "/ydpx/60000005/600005_01.ydpx"));
        params.add(new BasicNameValuePair("CURRENT_SYSTEM_DATE", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_ACCNAME", gjjUser.getName()));
        params.add(new BasicNameValuePair("_ACCNUM", gjjUser.getGjjAcc()));
        params.add(new BasicNameValuePair("BRANCHKIND", "0"));
        params.add(new BasicNameValuePair("_DEPUTYIDCARDNUM", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("_IS", "-1709955"));
        params.add(new BasicNameValuePair("_ISCROP", gjjUser.getName()));
        params.add(new BasicNameValuePair("_LOGIP", "20150521193613278"));
        params.add(new BasicNameValuePair("_PAGEID", "step1"));
        params.add(new BasicNameValuePair("_PORCNAME", "个贷分户查询"));
        params.add(new BasicNameValuePair("_PROCID", "60000005"));
        params.add(new BasicNameValuePair("_RW", "w"));
        params.add(new BasicNameValuePair("_SENDDATE", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_SENDOPERID", new DateTime().minusDays(1).toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_TYPE", "init"));
        params.add(new BasicNameValuePair("_UNITACCNAME", ""));
        params.add(new BasicNameValuePair("_WITHKEY", "0"));
        params.add(new BasicNameValuePair("certinum5", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("isSamePer", "false"));
        params.add(new BasicNameValuePair("termnum", ""));
        params.add(new BasicNameValuePair("transdate", ""));
        params.add(new BasicNameValuePair("unitaccname", ""));
        params.add(new BasicNameValuePair("usebal", ""));
        params.add(new BasicNameValuePair("yearrpykind", ""));
        params.add(new BasicNameValuePair("cardno", ""));
        params.add(new BasicNameValuePair("lmcardno", ""));

        for (String loanAccse : loanAccses) {
            log.info("纪录贷款信息开始，贷款账号：{}", loanAccse);

            List<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>(params);
            params2.add(new BasicNameValuePair("loanaccnum", loanAccse));

            String loanResult = HttpClientSupport.post(url, cookie, params2);

            log.info("纪录贷款信息，贷款信息json：{}", loanResult);

            GjjResult gjjResult = JSONUtils.jsonToObject(loanResult, GjjResult.class);

            if (gjjResult != null && gjjResult.getData() != null) {
                GjjLoanExample gjjLoanExample = new GjjLoanExample();
                gjjLoanExample.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId()).andLoanAccEqualTo(loanAccse);
                gjjLoanMapper.deleteByExample(gjjLoanExample);
            }

            GjjLoan gjjLoan = this.gjjResultToGjjLoan(gjjResult, gjjUser, loanAccse);

            gjjLoanMapper.insert(gjjLoan);

            log.info("纪录贷款信息结束");

        }
        return loanAccses;
    }

    /**
     * 转化成公积金贷款详情
     *
     * @param gjjResult
     * @param gjjUser
     * @param loanAccse
     * @return
     */
    private GjjLoan gjjResultToGjjLoan(GjjResult gjjResult, GjjUser gjjUser, String loanAccse) {
        GjjLoan gjjLoan = new GjjLoan();
        gjjLoan.setUserAccId(gjjUser.getAccId());
        gjjLoan.setLoanAcc(loanAccse);
        gjjLoan.setLoanUserName(gjjUser.getName());
        gjjLoan.setLoanUserId(gjjUser.getIdCard());
        gjjLoan.setLoanUserCompany(gjjResult.getData().get("unitaccname"));
        gjjLoan.setLoanUserPhone(gjjResult.getData().get("handset"));
        gjjLoan.setLoanUserGjjAcc(gjjResult.getData().get("cardaccnum"));
        gjjLoan.setLoanUserGjjMAmount(this.getDouble(gjjResult.getData().get("monpaysum")));
        gjjLoan.setLoanUserGjjStatus(gjjResult.getData().get("indiaccstate"));
        gjjLoan.setLoanUserBalance(this.getDouble(gjjResult.getData().get("curbal")));
        gjjLoan.setLoanUserAddAcc(gjjResult.getData().get("btaccnum"));
        gjjLoan.setLoanUserAddBlance(this.getDouble(gjjResult.getData().get("amt8")));
        gjjLoan.setLoanSpouseName(gjjResult.getData().get("matename"));
        gjjLoan.setLoanSpouseId(gjjResult.getData().get("matecertinum"));
        gjjLoan.setLoanSpouseCompany(gjjResult.getData().get("oldunitaccname"));
        gjjLoan.setLoanSpousePhone(gjjResult.getData().get("linkphone"));
        gjjLoan.setLoanSpouseGjjAcc(gjjResult.getData().get("accnum3"));
        gjjLoan.setLoanSpouseGjjMAmount(this.getDouble(gjjResult.getData().get("repayamt")));
        gjjLoan.setLoanSpouseGjjStatus(gjjResult.getData().get("accstate"));
        gjjLoan.setLoanSpouseBalance(this.getDouble(gjjResult.getData().get("usebal")));
        gjjLoan.setLoanSpouseAddAcc(gjjResult.getData().get("btaccnum1"));
        gjjLoan.setLoanSpouseAddBlance(this.getDouble(gjjResult.getData().get("amt9")));
        gjjLoan.setLoanAmount(this.getDouble(gjjResult.getData().get("loanamt")));
        gjjLoan.setLoanRate(this.getDouble(gjjResult.getData().get("loanrate")));
        gjjLoan.setmAmount(this.getDouble(gjjResult.getData().get("repayprin")));
        gjjLoan.setmRate(this.getDouble(gjjResult.getData().get("repayint")));
        gjjLoan.setLoanBeginTime(DateUtils.strToDate(gjjResult.getData().get("begdate")));
        gjjLoan.setLoanEndTime(DateUtils.strToDate(gjjResult.getData().get("enddate")));
        gjjLoan.setLastPlan(DateUtils.strToDate(gjjResult.getData().get("lasttransdate")));
        gjjLoan.setLastReal(DateUtils.strToDate(gjjResult.getData().get("enddate2")));
        gjjLoan.setAccBank(gjjResult.getData().get("earnstbankaccnum"));
        gjjLoan.setLeftMonth(this.getInteger(gjjResult.getData().get("remainterm")));
        gjjLoan.setmMonth(gjjResult.getData().get("termnum"));
        gjjLoan.setCurrentTermAmount(this.getDouble(gjjResult.getData().get("plandedprin")));
        gjjLoan.setCurrentTermTime(DateUtils.strToDate(gjjResult.getData().get("repaydate")));
        gjjLoan.setCurrentTermRate(this.getDouble(gjjResult.getData().get("planint")));
        gjjLoan.setCurrentTermPrincipal(this.getDouble(gjjResult.getData().get("planprin")));
        gjjLoan.setRepayType(gjjResult.getData().get("repaymode"));
        gjjLoan.setOverM(this.getInteger(gjjResult.getData().get("owecnt")));
        gjjLoan.setOverdueRate(this.getDouble(gjjResult.getData().get("oweprin")));
        gjjLoan.setOverDuePrincipal(this.getDouble(gjjResult.getData().get("amt3")));
        gjjLoan.setOverShouldRate(this.getDouble(gjjResult.getData().get("oweint")));
        gjjLoan.setOverdueRealRate(this.getDouble(gjjResult.getData().get("repaypun")));
        gjjLoan.setTrustLoanType(gjjResult.getData().get("fundrpykind"));
        gjjLoan.setTrustLoanUse(gjjResult.getData().get("yearrpykind"));
        gjjLoan.setTrustTime(DateUtils.strToDate(gjjResult.getData().get("transdate")));
        gjjLoan.setTrustLoanAcc(gjjResult.getData().get("accnum4"));
        gjjLoan.setTrustAddAcc(gjjResult.getData().get("btaccnum2"));
        gjjLoan.setTrustSpouseAcc(gjjResult.getData().get("accnum5"));
        gjjLoan.setTrustSpouseAddAcc(gjjResult.getData().get("btaccnum3"));
        gjjLoan.setLoanSum(this.getDouble(gjjResult.getData().get("amt1")));
        gjjLoan.setPrincipalSum(this.getDouble(gjjResult.getData().get("amt2")));
        gjjLoan.setRateSum(this.getDouble(gjjResult.getData().get("amt5")));
        gjjLoan.setNormalBalanceSum(this.getDouble(gjjResult.getData().get("autpayamt")));
        gjjLoan.setOverdueSum(this.getDouble(gjjResult.getData().get("amt6")));
        gjjLoan.setParseTime(new Date());
        return gjjLoan;
    }

    /**
     * 纪录还款计划
     *
     * @param cookie
     * @param loanAccses
     */
    public void recordRepayPlan(String cookie, GjjUser gjjUser, Set<String> loanAccses) {
        log.info("纪录还款计划开始: {}", gjjUser.getName());
        String url = "http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("$page", "/ydpx/60000002/600002_01.ydpx"));
        params.add(new BasicNameValuePair("CURRENT_SYSTEM_DATE", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_ACCNAME", gjjUser.getName()));
        params.add(new BasicNameValuePair("_ACCNUM", gjjUser.getGjjAcc()));
        params.add(new BasicNameValuePair("BRANCHKIND", "0"));
        params.add(new BasicNameValuePair("_DEPUTYIDCARDNUM", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("_IS", "-1709955"));
        params.add(new BasicNameValuePair("_ISCROP", "0"));
        params.add(new BasicNameValuePair("_LOGIP", "20150521193613278"));
        params.add(new BasicNameValuePair("_PAGEID", "step1"));
        params.add(new BasicNameValuePair("_PORCNAME", "贷款还款计划查询"));
        params.add(new BasicNameValuePair("_PROCID", "60000002"));
        params.add(new BasicNameValuePair("_RW", "w"));
        params.add(new BasicNameValuePair("_SENDDATE", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_SENDOPERID", gjjUser.getGjjAcc()));
        params.add(new BasicNameValuePair("_SENDTIME", new DateTime().minusDays(2).toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_TYPE", "init"));
        params.add(new BasicNameValuePair("_UNITACCNAME", ""));
        params.add(new BasicNameValuePair("_WITHKEY", "0"));
        params.add(new BasicNameValuePair("accname", "0"));
        params.add(new BasicNameValuePair("certinum", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("certinum5", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("curbal", ""));
        params.add(new BasicNameValuePair("enddate", ""));
        params.add(new BasicNameValuePair("isSamePer", "false"));
        params.add(new BasicNameValuePair("lasttransdate", ""));
        params.add(new BasicNameValuePair("loanamt", ""));
        params.add(new BasicNameValuePair("loanterm", ""));
        params.add(new BasicNameValuePair("oldrepayaccnum", ""));
        params.add(new BasicNameValuePair("remainterm", ""));

        for (String loanAccse : loanAccses) {
            log.info("获取贷款计划，贷款账号：{} 用户: {}", loanAccse, gjjUser.getName());

            List<BasicNameValuePair> params2 = new ArrayList<BasicNameValuePair>(params);
            params2.add(new BasicNameValuePair("loanaccnum", loanAccse));

            String repayPlanResultJson = HttpClientSupport.post(url, cookie, params2);
            log.info("贷款计划json：{}", repayPlanResultJson);

            GjjResult gjjResult = JSONUtils.jsonToObject(repayPlanResultJson, GjjResult.class);
            if (gjjResult != null && gjjResult.getData() != null) {
                GjjRepayPlanExample example = new GjjRepayPlanExample();
                example.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId()).andLoanIdEqualTo(loanAccse);
                gjjRepayPlanMapper.deleteByExample(example);
            }

            GjjRepayPlan gjjRepayPlan = this.resultToGjjRepayPlan(gjjResult, gjjUser);
            gjjRepayPlanMapper.insert(gjjRepayPlan);
        }
        log.info("纪录贷款计划结束");
    }

    /**
     * 纪录贷款审批状态
     *
     * @param cookie
     * @param gjjUser
     */
    private void recordLoanStatus(String cookie, GjjUser gjjUser) {
        log.info("记录贷款审批状态 start, cookie: {}, 用户: {}", cookie, gjjUser.getName());
        Map<String, String> extParms = new HashMap<String, String>();
        extParms.put("accnum", "");
        extParms.put("accname", gjjUser.getName());
        extParms.put("certinum", gjjUser.getAccId());
        extParms.put("certitype", "");
        extParms.put("apprnum", "");
        extParms.put("loanaccnum", "");
        extParms.put("bankcode", "");
        extParms.put("begappdate", "");
        extParms.put("begappfinishdate", "");
        extParms.put("begchkdate", "");
        extParms.put("begguarcontrdate", "");
        extParms.put("begguarcreadate", "");
        extParms.put("comstate", "");
        extParms.put("endappdate", "");
        extParms.put("endappfinishdate", "");
        extParms.put("endchkdate", "");
        extParms.put("endguarcontrdate", "");
        extParms.put("endguarcreadate", "");
        extParms.put("instcode", "");

        String dataPool = this.getTabelDataPool(cookie, gjjUser, "60000004", extParms);
        // 纪录贷款审批信息
        String loanStatusStr = this.getLoanStatusList(cookie, dataPool, gjjUser, extParms);

        // 纪录贷款审批状态列表
        LoanStatusResult result = JSONUtils.jsonToObject(loanStatusStr, LoanStatusResult.class);

        List<GjjLoanStatus> gjjLoanStatus = new ArrayList<GjjLoanStatus>();
        if (result != null && result.getData() != null && result.getData().getData() != null) {
            for (LoanStatus loanStatus : result.getData().getData()) {
                gjjLoanStatus.add(this.resultDetailToGjjDetail(loanStatus, gjjUser));
            }
            if (gjjLoanStatus != null && gjjLoanStatus.size() > 0) {
                // 重新插入以前贷款审批纪录
                GjjLoanStatusExample example = new GjjLoanStatusExample();
                example.createCriteria().andUserAccIdEqualTo(gjjUser.getAccId());
                Integer delDetailNum = gjjLoanStatusMapper.deleteByExample(example);
                log.info("删除贷款审批纪录" + gjjUser.getAccId() + ":" + delDetailNum + "条");
            }
            this.insertLoanStatusBatch(gjjLoanStatus);
        }
        log.info("记录贷款审批纪录结束, 记录{}条, 用户: {}", gjjLoanStatus.size(), gjjUser.getName());
    }

    /**
     * 转换成贷款计划
     *
     * @param result
     * @param gjjUser
     * @return
     */
    private GjjRepayPlan resultToGjjRepayPlan(GjjResult gjjResult, GjjUser gjjUser) {
        GjjRepayPlan gjjRepayPlan = new GjjRepayPlan();
        gjjRepayPlan.setUserAccId(gjjUser.getAccId());
        gjjRepayPlan.setLoanId(gjjResult.getData().get("oldrepayaccnum"));
        gjjRepayPlan.setName(gjjUser.getName());
        gjjRepayPlan.setAccId(gjjUser.getAccId());
        gjjRepayPlan.setLoanDeadline(DateUtils.strToDate(gjjResult.getData().get("enddate")));
        gjjRepayPlan.setLoanBalance(this.getDouble(gjjResult.getData(), "curbal"));
        gjjRepayPlan.setLoanAmount(this.getDouble(gjjResult.getData(), "loanamt"));
        gjjRepayPlan.setLoanTermAmount(this.getInteger(gjjResult.getData(), "loanterm"));
        gjjRepayPlan.setLoanTermLeft(this.getInteger(gjjResult.getData(), "remainterm"));
        gjjRepayPlan.setLastTradeTime(DateUtils.strToDate(gjjResult.getData().get("lasttransdate")));
        gjjRepayPlan.setParseTime(new Date());
        return gjjRepayPlan;
    }


    /**
     * 获取页面中的数据总线
     *
     * @return
     */
    private PoolSelect getPoolSelectByHtml(String cookie) {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=80000032");

        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        return this.getPoolSelect(responseBody);
    }

    /**
     * 获取页面中的数据总线
     *
     * @return
     */
    private PoolSelect getPoolSelect(String responseBody) {
        String str = "var poolSelect = ";
        String poolSelectAfter = responseBody.substring(responseBody.indexOf(str) + str.length());
        String poolSelectStr = poolSelectAfter.substring(0, poolSelectAfter.indexOf("}") + 1);

        PoolSelect poolSelect = new PoolSelect();
        for (Field field : poolSelect.getClass().getDeclaredFields()) {
            String val = this.getPoolSelectItem(field.getName(), poolSelectStr);
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), poolSelect.getClass());
                Method md = pd.getWriteMethod();
                try {
                    md.invoke(poolSelect, val);
                } catch (IllegalArgumentException e) {
                    log.error("", e);
                } catch (IllegalAccessException e) {
                    log.error("", e);
                } catch (InvocationTargetException e) {
                    log.error("", e);
                }
            } catch (IntrospectionException e) {
                log.error("", e);
            }
        }

        return poolSelect;
    }

    private String getPoolSelectItem(String key, String poolSelect) {
        String fenge = "'";
        if (key.equals("$page")) {
            // $page的标识是"
            fenge = "\"";
        }
        String mark = fenge + key + fenge + ": " + fenge;
        String val = poolSelect.substring(poolSelect.indexOf(mark) + mark.length());
        val = val.substring(0, val.indexOf(fenge));
        return val;
    }

    /**
     * 登陆后纪录用户
     *
     * @param certinum
     * @param pass
     * @param poolSelect
     * @return
     */
    private GjjUser recordUser(String certinum, String pass, PoolSelect poolSelect, GjjResult gjjResult) {
        // 公积金用户表
        GjjUser gjjUser = new GjjUser();
        gjjUser.setAccId(certinum);
        gjjUser.setAccPwd(pass);
        gjjUser.setName(poolSelect.get_ACCNAME());
        gjjUser.setIdCard(certinum);
        gjjUser.setGjjAcc(poolSelect.get_ACCNUM());
        gjjUser.setLoginTime(new Date());
        gjjUser.setBalance(this.getDouble(gjjResult.getData(), "amt1"));
        gjjUser.setmContribute(this.getDouble(gjjResult.getData(), "amt2"));
        gjjUser.setGjjRate(this.getDouble(gjjResult.getData(), "unitprop"));
        gjjUser.setAccStatus(this.getInteger(gjjResult.getData(), "unitaccstate"));
        gjjUser.setLoginTime(new Date());

        GjjUserExample userExample = new GjjUserExample();
        userExample.createCriteria().andAccIdEqualTo(certinum);

        GjjUser dbGjjUser = this.getGjjUser(userExample);
        if (dbGjjUser == null) {
            // 没该身份证的账号，插入用户基本信息
            gjjUserMapper.insert(gjjUser);
        } else {
            gjjUser.setId(dbGjjUser.getId());
            gjjUserMapper.updateByPrimaryKey(gjjUser);
        }

        // 公积金详情表
        GjjAccDetail accDetail = new GjjAccDetail();
        accDetail.setUserAccId(certinum);
        accDetail.setProductCode(this.getInteger(gjjResult.getData(), "prodcode"));
        accDetail.setTeamAcc(gjjResult.getData().get("unitaccnum"));
        accDetail.setTeamName(gjjResult.getData().get("unitaccname"));
        accDetail.setAccCreateTime(DateUtils.strToDate(gjjResult.getData().get("opnaccdate")));// 开户日期
        accDetail.setAccStatus(this.getInteger(gjjResult.getData(), "indiaccstate"));// 账户状态
        accDetail.setUserMContribute(this.getDouble(gjjResult.getData(), "amt2"));// 月缴纳额
        accDetail.setBalance(gjjUser.getBalance());// 当前余额
        String lastTimeStr = gjjResult.getData().get("lpaym");
        lastTimeStr = lastTimeStr.substring(0, 4) + "-" + lastTimeStr.substring(4);
        accDetail.setLastTime(DateUtils.strToDate(lastTimeStr));//
        // 最后汇款月
        accDetail.setUserRate(this.getDouble(gjjResult.getData(), "indiprop"));// 缴存比例
        accDetail.setTeamRate(this.getDouble(gjjResult.getData(), "unitprop"));// 单位比例
        accDetail.setTeamCreateTime(DateUtils.strToDate(gjjResult.getData().get("begdate")));// 单位开户日期
        accDetail.setTeamStatus(this.getInteger(gjjResult.getData(), "unitaccstate"));// 单位账户状态
        accDetail.setBank(gjjResult.getData().get("instcode"));
        accDetail.setParseTime(new Date());// 数据解析时间

        GjjAccDetailExample accDetailExample = new GjjAccDetailExample();
        accDetailExample.createCriteria().andUserAccIdEqualTo(certinum);
        List<GjjAccDetail> dbGjjAccDetails = gjjAccDetailMapper.selectByExample(accDetailExample);
        if (dbGjjAccDetails != null && dbGjjAccDetails.size() > 0) {
            // 已经存在，更新
            accDetail.setId(dbGjjAccDetails.get(0).getId());
            gjjAccDetailMapper.updateByPrimaryKey(accDetail);
        } else {
            gjjAccDetailMapper.insert(accDetail);
        }

        return gjjUser;
    }

    private String getTabelDataPool(String cookie, GjjUser gjjUser, String procid,
                                    Map<String, String> extParms) {

        // 获取初始化数据池的html
        String dataPoolResponseBody = this.getDataPool(cookie, procid, gjjUser, extParms);
        // 数据池
        String dataPool = this.getDataPool(dataPoolResponseBody);
        // 数据总线
        PoolSelect poolSelect = this.getPoolSelect(dataPoolResponseBody);
        try {
            // 发请求，使datapool生效
            this.commandSummer(poolSelect, cookie, extParms);
        } catch (ClientProtocolException e1) {
            log.error("", e1);
        } catch (IOException e1) {
            log.error("", e1);
        }

        return dataPool;
    }

    /**
     * 获取查询使用的数据池
     *
     * @param cookie
     * @param procid
     * @param gjjUser
     * @return
     */
    private String getDataPool(String cookie, String procid, GjjUser gjjUser, Map<String, String> extParms) {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=" + procid);
        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }

        return responseBody;
    }

    /**
     * 发请求，使datapool生效
     *
     * @param poolSelect
     * @param extParms
     * @throws ClientProtocolException
     * @throws IOException
     */
    public void commandSummer(PoolSelect poolSelect, String cookie, Map<String, String> extParms)
            throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());

        post.addHeader("Cookie", cookie);

        post.setEntity(new UrlEncodedFormEntity(this.getCommandSummerParams(poolSelect, extParms)));
        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        log.info("commandSummer----------------------------------------start");
        String responseBody = httpClient.execute(post, responseHandler);
        log.info(responseBody);
        log.info("commandSummer----------------------------------------end");
    }

    /**
     * 根据数据总线获取post参数
     *
     * @param poolSelect
     * @param extParms
     * @return
     */
    private List<BasicNameValuePair> getCommandSummerParams(PoolSelect poolSelect,
                                                            Map<String, String> extParms) {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (Field field : poolSelect.getClass().getDeclaredFields()) {
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), poolSelect.getClass());
            } catch (IntrospectionException e) {
                log.error("", e);
            }
            Method md = pd.getReadMethod();
            String val = null;
            try {
                val = (String) md.invoke(poolSelect);
            } catch (IllegalArgumentException e) {
                log.error("", e);
            } catch (IllegalAccessException e) {
                log.error("", e);
            } catch (InvocationTargetException e) {
                log.error("", e);
            }
            params.add(new BasicNameValuePair(field.getName(), val));
        }

        if (extParms != null && extParms.size() > 0) {
            for (String key : extParms.keySet()) {
                params.add(new BasicNameValuePair(key, extParms.get(key)));
            }
        }

        return params;
    }

    @Transactional
    private void insertDetailBatch(List<GjjDetail> gjjDetails) {
        for (GjjDetail gjjDetail : gjjDetails) {
            gjjDetailMapper.insert(gjjDetail);
        }
    }

    @Transactional
    private void insertRepayDetailBatch(List<GjjRepayDetail> details) {
        for (GjjRepayDetail gjjDetail : details) {
            gjjRepayDetailMapper.insert(gjjDetail);
        }
    }

    @Transactional
    private void insertLoanStatusBatch(List<GjjLoanStatus> statues) {
        for (GjjLoanStatus status : statues) {
            gjjLoanStatusMapper.insert(status);
        }
    }

    /**
     * 获取到的公积金详情转化
     *
     * @param detail
     * @return
     */
    private GjjDetail resultDetailToGjjDetail(GjjCDetail detail, GjjUser gjjUser) {
        GjjDetail gjjDetail = new GjjDetail();
        gjjDetail.setBalance(this.getDouble(detail.getPayvouamt()));// 余额
        gjjDetail.setIdCard(gjjUser.getAccId());// 身份证号码
        gjjDetail.setParseTime(new Date());// 数据解析时间
        gjjDetail.setTeamAcc(detail.getUnitaccnum1());// 单位账号
        gjjDetail.setTeamName(detail.getUnitaccname());// 单位名称
        gjjDetail.setTradeTime(DateUtils.strToDate(detail.getTransdate()));// 交易日期
        gjjDetail.setTradeAmount(this.getDouble(detail.getBasenum()));// 发生额
        gjjDetail.setType(detail.getReason());// 业务种类
        gjjDetail.setUserAcc(detail.getAccnum1());// 个人账号
        gjjDetail.setUserAccId(gjjUser.getAccId());// 用户登陆账号
        gjjDetail.setUserName(detail.getAccname1());// 姓名
        return gjjDetail;
    }

    /**
     * 获取到的贷款审批纪录转化
     *
     * @return
     */
    private GjjLoanStatus resultDetailToGjjDetail(LoanStatus loanStatus, GjjUser gjjUser) {
        GjjLoanStatus status = new GjjLoanStatus();
        status.setUserAccId(gjjUser.getAccId());
        status.setName(gjjUser.getName());
        status.setIdCareType(loanStatus.getCertitype());//证件类型
        status.setIdCard(loanStatus.getCertinum());//证件号码
        status.setLoanAcc(loanStatus.getLoanaccnum());//贷款账号
        status.setLoanAmount(this.getDouble(loanStatus.getLoanamt()));// 贷款金额
        status.setLoanMonths(this.getInteger(loanStatus.getLoanterm()));//贷款年限(年)
        status.setLoanType(loanStatus.getCommloanflag());//贷款类型
        status.setHouseType(loanStatus.getHousetype());//房屋类型
        status.setBank(loanStatus.getAgentbankcode());//受理网点
        status.setLoanStatus(loanStatus.getComstate());//业务办理状态
        status.setParseTime(new Date());
        return status;
    }

    /**
     * 获取到的贷款详情转化
     *
     * @param detail
     * @return
     */
    private GjjRepayDetail resultToGjjRepayDetail(GjjCReplayDetail detail, GjjUser gjjUser) {
        GjjRepayDetail repayDetail = new GjjRepayDetail();
        repayDetail.setAccNo(gjjUser.getAccId());// 证件号码
        repayDetail.setLoanAcc(detail.getLoanaccnum());// 贷款账号
        repayDetail.setLoanAmount(this.getDouble(detail.getLoanamt()));// 贷款金额
        repayDetail.setLoanMonth(this.getInteger(detail.getLoanterm()));// 贷款期限(月)
        repayDetail.setLoanSrc(detail.getFundsource());// 还贷资金来源
        repayDetail.setLoanTeller(detail.getAgentop());// 柜员号
        repayDetail.setLoanTerm(this.getInteger(detail.getTermnum()));// 期数
        repayDetail.setLoanType(detail.getLoanfundtype());// 还款类型
        repayDetail.setName(gjjUser.getName());
        repayDetail.setParseTime(new Date());// 数据解析时间
        repayDetail.setTradeAmount(this.getDouble(detail.getTransamt()));// 发生额
        repayDetail.setTradeTime(DateUtils.strToDate(detail.getTransdate()));// 交易日期
        repayDetail.setTypeCode(detail.getSummarycode());// 业务摘要码
        repayDetail.setUserAccId(detail.getCertinum());// 用户登录号
        return repayDetail;
    }

    /**
     * 贷款试算使用的用户信息
     *
     * @param cookie
     * @param loginId
     * @param certitype
     * @param certinum
     */
    public String cal(String cookie, String loginId, Integer certitype, String certinum, Integer validflag,
                      Integer techpost, Integer ishas) {
        GjjUser gjjUser = this.getGjjUser(loginId);
        if (gjjUser == null) {
            return null;
        }
        Map<String, String> ext = new HashMap<String, String>();
        ext.put("age", "28");
        ext.put("birthday", "1987-06-06");
        ext.put("flag", "0");
        ext.put("indiacctype", "1");
        String initResponseStr = this.getDataPool(cookie, "60000008", gjjUser, ext);

        PoolSelect poolSelect = this.getPoolSelect(initResponseStr);

        List<BasicNameValuePair> params = getCommandSummerParams(poolSelect, ext);

        String url = "http://njgjj.com/command.summer?uuid=" + System.currentTimeMillis();

        String personInfo = HttpClientSupport.post(url, cookie, params);
        log.info("贷款试算信息：{}", personInfo);

        // 数据池
        String dataPool = this.getDataPool(initResponseStr);

        GjjResult personInfoResult = JSONUtils.jsonToObject(personInfo, GjjResult.class);
        log.info("person json: {}", personInfoResult.getData());

        List<BasicNameValuePair> basicParams = new ArrayList<BasicNameValuePair>();
        basicParams.add(new BasicNameValuePair("DATAlISTGHOST", "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAKeA =="));
        basicParams.add(new BasicNameValuePair("_APPLY", "0"));
        basicParams.add(new BasicNameValuePair("_CHANNEL", "1"));
        basicParams.add(new BasicNameValuePair("certitype", certitype.toString()));
        basicParams.add(new BasicNameValuePair("certinum", certinum));
        basicParams.add(new BasicNameValuePair("validflag", validflag.toString()));
        basicParams.add(new BasicNameValuePair("techpost", techpost.toString()));
        basicParams.add(new BasicNameValuePair("ishas", String.valueOf(ishas)));

        Iterator<Map.Entry<String, String>> it = personInfoResult.getData().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            basicParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        List<BasicNameValuePair> submitparams = new ArrayList<BasicNameValuePair>();
        submitparams.addAll(basicParams);
        submitparams.add(new BasicNameValuePair("_DATAPOOL_", dataPool));

        String submitUrl = "http://njgjj.com/submit.summer?uuid=" + System.currentTimeMillis();
        String submitResponseStr = HttpClientSupport.post(submitUrl, cookie, submitparams);
        log.info("submitResponseStr: {}", submitResponseStr);

        GjjResult submitResult = JSONUtils.jsonToObject(submitResponseStr, GjjResult.class);
        if (submitResult == null || submitResult.getData() == null) {
            return null;
        }
        String taskUrl = "http://njgjj.com" + submitResult.getData().get("url");
        String taskResponseStr = HttpClientSupport.post(taskUrl, cookie, null);
        String taskDataPool = this.getDataPool(taskResponseStr);

        String taskSubmitUrl = "http://njgjj.com/submit.summer?uuid=" + System.currentTimeMillis();

        String p = "certinum=32011319740704486X&accname=高原&birthday=1974-07-04&age=41&custid=082496917&accnum=3201000088775143&unitaccnum=20100967283&unitaccname=南京玉桥商业广场管理有限公司&opnaccdate=1993-09-10&begpayym=200306&lpaym=&basenum=1,800.00&monpaysum=540.00&unitprop=15&indiprop=15&totalprop=30&bal=9,442.07&lastdrawdate=&cardaccnum=320100008877514300&lmcardno=6217001370009103995&islocaltime=0&loantime=1&loanhouseflag=0&loanhousenum=&firsthousearea=&housecount=&perhouserarea=&amount1=800,000.00&buyhousearea=89&housetype=01&matecertinum=&matename=&birthday1=&age1=&custid1=&accnum1=&unitaccnum1=&unitaccname1=&opnaccdate1=&begpayym1=&lpaym1=&basenum1=&monpaysum1=&unitprop1=&indiprop1=&totalprop1=&bal1=&lastdrawdate1=&cardaccnum1=&lmcardno1=×=0&flag=0&_APPLY=0&_CHANNEL=1&_PROCID=60000008&DATAlISTGHOST=rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAKeA==&_DATAPOOL_=" + taskDataPool;

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(p);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpClient httpClient = new DefaultHttpClient();

        HttpPost post = new HttpPost(taskSubmitUrl);
        post.setEntity(stringEntity);
        post.addHeader("Cookie", cookie);

        String taskSubmitResponseStr = null;
        try {
            taskSubmitResponseStr = httpClient.execute(post, new BasicResponseHandler());
            log.info("taskSubmitResponseStr: {}", taskSubmitResponseStr);
        } catch (IOException e) {
        }

//        GjjResult taskSubmitResult = JSONUtils.jsonToObject(taskSubmitResponseStr, GjjResult.class);
//        if (taskSubmitResult == null || taskSubmitResult.getData() == null) {
//            return null;
//        }
//        String taskSubmitResultUrl = "http://njgjj.com" + taskSubmitResult.getData().get("url");
//        String html = HttpClientSupport.post(taskSubmitResultUrl, cookie, null);
//
//        log.info("html: {}" + html);

        return personInfo;
    }

    private String getDetailList(String cookie, String dataPool, GjjUser gjjUser, Map<String, String> extParms) {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis());

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("_DATAPOOL_", dataPool));
        params.add(new BasicNameValuePair("_APPLY", "0"));
        params.add(new BasicNameValuePair("_CHANNEL", "1"));
        params.add(new BasicNameValuePair("_PROCID", "70000002"));
        params.add(new BasicNameValuePair("accname", gjjUser.getName()));
        params.add(new BasicNameValuePair("accnum", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("begdate", "2000-01-01"));
        params.add(new BasicNameValuePair("dynamicTable_configSqlCheck", "0"));
        params.add(new BasicNameValuePair("dynamicTable_currentPage", "0"));
        params.add(new BasicNameValuePair("dynamicTable_id", "datalist2"));
        params.add(new BasicNameValuePair("dynamicTable_nextPage", "1"));
        params.add(new BasicNameValuePair("dynamicTable_page", "/ydpx/70000002/700002_01.ydpx"));
        params.add(new BasicNameValuePair("dynamicTable_pageSize", "1000"));
        params.add(new BasicNameValuePair("dynamicTable_paging", "true"));
        params.add(new BasicNameValuePair("enddate", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("errorFilter", "1=1"));

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }

        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        log.info("公积金详情json：{}", responseBody);

        return responseBody;
    }

    /**
     * 记录贷款详情
     *
     * @param cookie
     * @param dataPool
     * @param gjjUser
     * @return
     */
    private String getLoanDetailList(String cookie, String dataPool, GjjUser gjjUser) {
        String url = "http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis();

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("dynamicTable_id", "datalist"));
        params.add(new BasicNameValuePair("dynamicTable_currentPage", "0"));
        params.add(new BasicNameValuePair("dynamicTable_pageSize", "1000"));
        params.add(new BasicNameValuePair("dynamicTable_nextPage", "1"));
        params.add(new BasicNameValuePair("dynamicTable_page", "/ydpx/60000001/600001_01.ydpx"));
        params.add(new BasicNameValuePair("dynamicTable_paging", "true"));
        params.add(new BasicNameValuePair("dynamicTable_configSqlCheck", "0"));
        params.add(new BasicNameValuePair("errorFilter", "1=1"));
        params.add(new BasicNameValuePair("loanaccnum", ""));
        params.add(new BasicNameValuePair("accname", gjjUser.getName()));
        params.add(new BasicNameValuePair("certinum", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("begdate", "2000-01-01"));
        params.add(new BasicNameValuePair("enddate", new DateTime().toString("yyyy-MM-dd")));
        params.add(new BasicNameValuePair("_APPLY", "0"));
        params.add(new BasicNameValuePair("_CHANNEL", "1"));
        params.add(new BasicNameValuePair("_PROCID", "60000001"));
        params.add(new BasicNameValuePair("_DATAPOOL_", dataPool));

        String loanDetailListStr = HttpClientSupport.post(url, cookie, params);

        return loanDetailListStr;
    }

    private String getLoanStatusList(String cookie, String dataPool, GjjUser gjjUser, Map<String, String> extParms) {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis());

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("_DATAPOOL_", dataPool));
        params.add(new BasicNameValuePair("_APPLY", "0"));
        params.add(new BasicNameValuePair("_CHANNEL", "1"));
        params.add(new BasicNameValuePair("_PROCID", "60000004"));
        params.add(new BasicNameValuePair("accname", gjjUser.getName()));
        params.add(new BasicNameValuePair("accnum", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("begappdate", ""));
        params.add(new BasicNameValuePair("begappfinishdate", ""));
        params.add(new BasicNameValuePair("begguarcontrdate", ""));
        params.add(new BasicNameValuePair("begguarcreadate", ""));
        params.add(new BasicNameValuePair("begguarcreadate", ""));
        params.add(new BasicNameValuePair("comstate", ""));
        params.add(new BasicNameValuePair("endappdate", ""));
        params.add(new BasicNameValuePair("endappdate", ""));
        params.add(new BasicNameValuePair("endappdate", ""));
        params.add(new BasicNameValuePair("certinum", gjjUser.getAccId()));
        params.add(new BasicNameValuePair("certitype", "1"));
        params.add(new BasicNameValuePair("begdate", "2000-01-01"));
        params.add(new BasicNameValuePair("dynamicTable_configSqlCheck", "0"));
        params.add(new BasicNameValuePair("dynamicTable_currentPage", "0"));
        params.add(new BasicNameValuePair("dynamicTable_id", "datalist"));
        params.add(new BasicNameValuePair("dynamicTable_nextPage", "1"));
        params.add(new BasicNameValuePair("dynamicTable_page", "/ydpx/60000004/600004_01.ydpx"));
        params.add(new BasicNameValuePair("dynamicTable_pageSize", "1000"));
        params.add(new BasicNameValuePair("dynamicTable_paging", "true"));
        params.add(new BasicNameValuePair("errorFilter", "1=1"));

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }

        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("", e);
        } catch (IOException e) {
            log.error("", e);
        }
        log.info("贷款审批状态json：{}", responseBody);

        return responseBody;
    }

    /**
     * 详情页查询使用
     *
     * @param html
     * @return
     */
    private String getDataPool(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByAttributeValue("name", "_DATAPOOL_");
        return elements != null && elements.size() > 0 ? elements.get(0).val().replaceAll(" ", "") : null;
    }

    private GjjUser getGjjUser(GjjUserExample userExample) {
        List<GjjUser> gjjUsers = gjjUserMapper.selectByExample(userExample);
        if (gjjUsers.size() > 0) {
            return gjjUsers.get(0);
        }
        return null;
    }

    private Double getDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }

    private Double getDouble(Map<String, String> map, String key) {
        try {
            return Double.parseDouble(map.get(key));
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getInteger(Map<String, String> map, String key) {
        try {
            return Integer.parseInt(map.get(key));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public GjjUser getGjjUser(String certinum) {
        GjjUserExample example = new GjjUserExample();
        example.createCriteria().andAccIdEqualTo(certinum);
        List<GjjUser> gjjUsers = gjjUserMapper.selectByExample(example);
        return gjjUsers.size() > 0 ? gjjUsers.get(0) : null;
    }

    @Override
    public GjjAccDetail getGjjAccDetail(String certinum) {
        GjjAccDetailExample example = new GjjAccDetailExample();
        example.createCriteria().andUserAccIdEqualTo(certinum);
        List<GjjAccDetail> accDetails = gjjAccDetailMapper.selectByExample(example);
        return accDetails.size() > 0 ? accDetails.get(0) : null;
    }

    @Override
    public List<GjjDetail> getRecentGjjDetails(String certinum) {
        GjjDetailExample example = new GjjDetailExample();
        example.createCriteria().andTradeTimeGreaterThan(new DateTime().minusYears(1).toDate());
        return gjjDetailMapper.selectByExample(example);
    }

    public BlockingQueue<GjjAsynDomain> getGjjAsynDomainQuery() {
        return gjjAsynDomainQuery;
    }
}
