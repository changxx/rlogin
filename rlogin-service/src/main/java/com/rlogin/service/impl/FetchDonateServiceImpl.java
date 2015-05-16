package com.rlogin.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

import com.rlogin.common.frame.Result;
import com.rlogin.common.http.HttpClientSupport;
import com.rlogin.common.http.NJReserveResponseHandler;
import com.rlogin.domain.gjj.PoolSelect;

@Service
public class FetchDonateServiceImpl implements FetchDonateService {

    @Override
    public Result fetchService(String certinum, String cookie) throws UnsupportedEncodingException {
        Result result = new Result();
        System.out.println("command:" + cookie);
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

        post.setEntity(new UrlEncodedFormEntity(params));
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("info: " + responseBody);
        return result;
    }

    /**
     * 获取页面中的数据总线
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.getPoolSelect(responseBody);
    }

    /**
     * 获取页面中的数据总线
     * @return
     */
    private PoolSelect getPoolSelect(String responseBody) {
        String str = "var poolSelect = ";
        String poolSelectAfter = responseBody.substring(responseBody.indexOf(str) + str.length());
        String poolSelectStr = poolSelectAfter.substring(0, poolSelectAfter.indexOf("}") + 1);

        String accnum = this.getPoolSelectItem("_ACCNUM", poolSelectStr);
        String accname = this.getPoolSelectItem("_ACCNAME", poolSelectStr);
        String certinum = this.getPoolSelectItem("_DEPUTYIDCARDNUM", poolSelectStr);
        String unitaccname = this.getPoolSelectItem("_UNITACCNAME", poolSelectStr);
        PoolSelect poolSelect = new PoolSelect(accnum, unitaccname, accname, certinum);
        return poolSelect;
    }

    private String getPoolSelectItem(String key, String poolSelect) {
        String mark = "'" + key + "': '";
        String val = poolSelect.substring(poolSelect.indexOf(mark) + mark.length());
        val = val.substring(0, val.indexOf("'"));
        return val;
    }

}
