package com.rlogin.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rlogin.dao.mapper.gjj.GjjLoanMapper;
import com.rlogin.dao.mapper.gjj.GjjLoanStatusMapper;
import com.rlogin.domain.Vericode;
import com.rlogin.domain.gjj.GjjLoan;
import com.rlogin.domain.gjj.GjjLoanExample;
import com.rlogin.domain.gjj.GjjLoanStatusExample;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rlogin.common.frame.Login;
import com.rlogin.common.frame.json.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.ImageResponseHandler;
import com.rlogin.common.http.NJReserveResponseHandler;
import com.rlogin.common.util.DesUtil;
import com.rlogin.domain.Constant;
import com.rlogin.domain.GjjAccStatus;
import com.rlogin.service.GjjService;

/**
 * 公积金
 *
 * @author changxx
 */
@Controller
@RequestMapping("/gjj")
public class GjjController {

    @Autowired
    private GjjService gjjService;

    @Autowired
    private GjjLoanStatusMapper gjjLoanStatusMapper;

    @Autowired
    private GjjLoanMapper gjjLoanMapper;

    /**
     * 登录界面
     */
    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("gjj/login");
        return mv;
    }

    /**
     * 验证码
     */
    @RequestMapping("/vericode")
    public void vericode(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "1") int code)
            throws ClientProtocolException, IOException {
        DefaultHttpClient httpClient = HttpClientSupport.getHttpClient();

        Vericode vericode = Vericode.getByCode(code);

        HttpPost post = new HttpPost(vericode.getUrl() + "?" + System.currentTimeMillis());

        post.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        post.addHeader("Accept-Encoding", "gzip, deflate, sdch");
        post.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        post.addHeader("Cache-Control", "max-age=0");
        post.addHeader("Connection", "keep-alive");
        post.addHeader("Host", "www.njgjj.com");
        post.addHeader("Referer", "http://www.njgjj.com/Logout");
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        // post.addHeader("Cookie", request.getHeader("Cookie"));

        httpClient.execute(post, new ImageResponseHandler(response));
    }

    /**
     * 登录操作
     */
    @RequestMapping("/loginin")
    @ResponseBody
    public Result loginin(@RequestParam String account, @RequestParam String pass,
                          @RequestParam String vericode, HttpServletRequest request, HttpServletResponse response)
            throws ClientProtocolException, IOException {
        Result result = new Result();
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/per.login");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("certinum", account));
        params.add(new BasicNameValuePair("perpwd", pass));
        params.add(new BasicNameValuePair("vericode", vericode));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        String cookie = request.getHeader("Cookie");
        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler(response);

        String responseHtml = httpClient.execute(post, responseHandler);

        Document document = Jsoup.parse(responseHtml);
        // 登陆成功，直接跳转
        Elements elements = document.select("#title_bar .person");
        // 登陆账户选择
        Elements elements2 = document.select(".WTLoginSelect");
        if ((elements != null && elements.size() > 0) || (elements2 != null && elements2.size() > 0)) {
            // 登录成功
            Element pElement = elements.size() > 0 ? elements.get(0) : elements2.get(0);
            result.setTip(pElement.text());

            // 种cookie
            Cookie loginCookie;
            try {
                loginCookie = new Cookie(Constant.USER_COOKIE_KEY, DesUtil.encode(account));
                response.addCookie(loginCookie);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
        Elements wTLoginError = document.select(".WTLoginError").select(".text");
        if (wTLoginError != null && wTLoginError.size() > 0) {
            result.setCode(Result.ERROR);
            Element errorElement = wTLoginError.get(0);
            result.setTip(errorElement.text());
            return result;
        }
        result.setCode(Result.ERROR);
        result.setTip("未知错误");

        return result;
    }

    /**
     * 登录成功获取数据
     */
    @RequestMapping("/fetch")
    @ResponseBody
    public Result fetch(@RequestParam String account, @RequestParam String pass, HttpServletRequest request) {
        Result result = new Result();
        String cookie = request.getHeader("Cookie");
        gjjService.fetchService(account, pass, cookie);
        return result;
    }

    /**
     * 注销公积金中心
     */
    @RequestMapping("/loginout")
    @ResponseBody
    public void loginout(HttpServletRequest request, HttpServletResponse response)
            throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/Logout");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie", request.getHeader("Cookie"));

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler(response);

        String responseHtml = httpClient.execute(post, responseHandler);
    }

    /**
     * 个人公积金主页
     */
    @Login
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String loginId = request.getAttribute(Constant.USER_COOKIE_KEY).toString();
        mv.addObject("gjjUser", gjjService.getGjjUser(loginId));
        mv.addObject("gjjDetail", gjjService.getGjjAccDetail(loginId));
        mv.addObject("gjjAccStatus", GjjAccStatus.values());

        prepareMV(mv, loginId);
        return mv;
    }

    /**
     * 个人公积金主页
     */
    @Login
    @RequestMapping("/recent")
    public ModelAndView recent(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        String loginId = request.getAttribute(Constant.USER_COOKIE_KEY).toString();

        mv.addObject("gjjDetails", gjjService.getRecentGjjDetails(loginId));
        prepareMV(mv, loginId);
        return mv;
    }

    /**
     * 贷款个数
     *
     * @param request
     * @return
     */
    @Login
    @RequestMapping("/loan")
    public ModelAndView loan(HttpServletRequest request, @RequestParam(required = false) String loanAcc) {
        ModelAndView mv = new ModelAndView();
        String loginId = request.getAttribute(Constant.USER_COOKIE_KEY).toString();
        mv.addObject("loginId", loginId);
        mv.addObject("loanAcc", loanAcc);

        if (loanAcc != null && !loanAcc.equals("")) {
            GjjLoanExample example = new GjjLoanExample();
            example.createCriteria().andLoanAccEqualTo(loanAcc).andUserAccIdEqualTo(loginId);
            List<GjjLoan> gjjLoans = gjjLoanMapper.selectByExample(example);
            mv.addObject("loan", gjjLoans.size() > 0 ? gjjLoans.get(0) : null);
        }

        prepareMV(mv, loginId);
        return mv;
    }

    /**
     * 贷款试算
     *
     * @param request
     * @param certinum
     * @return
     */
    @RequestMapping("/cal")
    public ModelAndView cal(HttpServletRequest request, @RequestParam(required = false) Integer certitype,
                            @RequestParam(required = false) String certinum, @RequestParam(required = false) Integer validflag,
                            @RequestParam(required = false) Integer techpost, @RequestParam(required = false) Integer ishas) {
        ModelAndView mv = new ModelAndView();
        String loginId = request.getAttribute(Constant.USER_COOKIE_KEY).toString();
        mv.addObject("loginId", loginId);

        String cookie = request.getHeader("Cookie");
        if (certitype != null && certinum != null) {
            String personInfo = gjjService.cal(cookie, loginId, certitype, certinum, validflag, techpost, ishas);
        }

        mv.addObject("certitype", certitype);
        mv.addObject("certinum", certinum);
        mv.addObject("validflag", validflag);
        mv.addObject("techpost", techpost);
        mv.addObject("ishas", ishas);
        prepareMV(mv, loginId);
        return mv;
    }

    private void prepareMV(ModelAndView mv, String loginId) {
        GjjLoanExample example = new GjjLoanExample();
        example.createCriteria().andUserAccIdEqualTo(loginId);
        mv.addObject("loanNum", gjjLoanMapper.countByExample(example));
    }

}
