package com.rlogin.web.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rlogin.common.frame.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.NJReserveResponseHandler;

@Controller
@RequestMapping("/njgjj")
public class NJGJJController {

    private String script  = "<script type=\"text/javascript\" src=\"";

    private String css     = "<link rel=\"stylesheet\" type=\"text/css\" href=\"";

    private String img     = "<img src=\"";

    private String context = "http://www.njgjj.com";

    @RequestMapping("")
    public void login(HttpServletResponse response) throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/login-per.jsp");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler(response);

        String responseStr = httpClient.execute(post, responseHandler);

        responseStr = responseStr.replaceAll(script, script + context);
        responseStr = responseStr.replaceAll(css, css + context);
        responseStr = responseStr.replaceAll(img, img + context + "/");
        responseStr = responseStr.replaceAll("GB18030", "UTF-8");

        response.getOutputStream().write(responseStr.getBytes(Charset.forName("gbk")));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


}
