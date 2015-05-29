package com.rlogin.web.controller;

import com.rlogin.common.frame.json.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.ImageResponseHandler;
import com.rlogin.common.http.NJReserveResponseHandler;
import com.rlogin.common.util.DesUtil;
import com.rlogin.domain.Constant;
import com.rlogin.domain.Vericode;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 征信
 * Created by changxx on 15/5/29.
 */
@Controller
@RequestMapping("/ipcrs")
public class IpcrsController {

    /**
     * 登录界面
     */
    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    /**
     * 验证码
     */
    @RequestMapping("/vericode")
    public void vericode(HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "2") int code)
            throws ClientProtocolException, IOException {
        Vericode vericode = Vericode.getByCode(code);
        HttpClientSupport.httpsPost(vericode.getUrl(), null, null, new ImageResponseHandler(response));
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

        String url = "https://ipcrs.pbccrc.org.cn/login.do";

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("method", "login"));
        params.add(new BasicNameValuePair("date", String.valueOf(System.currentTimeMillis())));
        params.add(new BasicNameValuePair("loginname", account));
        params.add(new BasicNameValuePair("password", pass));
        params.add(new BasicNameValuePair("_@IMGRC@_", vericode));
        params.add(new BasicNameValuePair("method", "login"));

        String cookie = request.getHeader("Cookie");

        String responseHtml = HttpClientSupport.httpsPost(url, cookie, params, new BasicResponseHandler());

        Document doc = Jsoup.parse(responseHtml);
        Element element = doc.getElementById("_error_field_");
        if (element != null) {
            result.setTip(element.text());
            result.setCode(Result.ERROR);
        }

        return result;
    }

}
