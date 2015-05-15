package com.rlogin.common.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.batch.poller.Poller;

import com.rlogin.common.util.JSONUtils;
import com.rlogin.domain.njgjj.PoolSelect;

/**
 * @author changxiangxiang
 * @date 2014年8月6日 下午5:02:44
 * @description
 * @since sprint2
 */
public class HttpClientTest {

    @Test
    public void test1() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/per.login");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("certinum", "41052719870606543X"));
        params.add(new BasicNameValuePair("perpwd", "488000"));
        params.add(new BasicNameValuePair("vericode", "3143"));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie", "JSESSIONID=0000m8CBUJ6TYDbEfcThSCWKP3B:-1;");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void test2() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=1431333077571");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("$page", "/ydpx/80000003/dppage8001.ydpx"));
        params.add(new BasicNameValuePair("_TYPE", "init"));
        params.add(new BasicNameValuePair("_ACCNUM", "3201000273884880"));
        params.add(new BasicNameValuePair("_PAGEID", "step1"));
        params.add(new BasicNameValuePair("_BRANCHKIND", "0"));
        params.add(new BasicNameValuePair("CURRENT_SYSTEM_DATE", "2015-05-11"));
        params.add(new BasicNameValuePair("_LOGIP", "20150511160254520"));
        params.add(new BasicNameValuePair("_WITHKEY", "0"));
        params.add(new BasicNameValuePair("_SENDOPERID", "41052719870606543X"));
        params.add(new BasicNameValuePair("_ISCROP", "0"));
        params.add(new BasicNameValuePair("_PROCID", "80000003"));
        params.add(new BasicNameValuePair("_PORCNAME", "个人分户查询"));
        params.add(new BasicNameValuePair("_SENDDATE", "2015-05-11"));
        params.add(new BasicNameValuePair("_ACCNAME", "路学亮"));
        params.add(new BasicNameValuePair("_IS", "-1528623"));
        params.add(new BasicNameValuePair("isSamePer", "false"));
        params.add(new BasicNameValuePair("_UNITACCNAME", "华为技术有限公司南京研究所"));
        params.add(new BasicNameValuePair("_RW", "w"));
        params.add(new BasicNameValuePair("_DEPUTYIDCARDNUM", "41052719870606543X"));
        params.add(new BasicNameValuePair("_SENDTIME", "2015-05-11"));
        params.add(new BasicNameValuePair("accname", "路学亮"));
        params.add(new BasicNameValuePair("certinum", "41052719870606543X"));
        params.add(new BasicNameValuePair("prodcode", "1"));
        params.add(new BasicNameValuePair("accnum", "3201000273884880"));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie",
                "R0SzGKvPQ9=MDAwM2IyOWY5MTQwMDAwMDAwMjcwe2VkGCgxNDMxNTk5MDEx; JSESSIONID=0000kyvKeptnHptY6RzWs93Z9OV:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void test3() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=80000032");

        post.addHeader("Cookie", "JSESSIONID=0000ebrJbXxW-Z-YCUZfC9G9mr_:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

        String str = "var poolSelect = ";
        String poolSelectAfter = responseBody.substring(responseBody.indexOf(str) + str.length());
        String poolSelect = poolSelectAfter.substring(0, poolSelectAfter.indexOf("}") + 1);

        System.out.println(poolSelect);

        String accnum = this.getPoolSelectItem("_ACCNUM", poolSelect);
        String accname = this.getPoolSelectItem("_ACCNAME", poolSelect);
        String certinum = this.getPoolSelectItem("_DEPUTYIDCARDNUM", poolSelect);
        System.out.println(accnum);
        System.out.println(accname);
        System.out.println(certinum);
    }

    private String getPoolSelectItem(String key, String poolSelect) {
        String mark = "'" + key + "': '";
        String val = poolSelect.substring(poolSelect.indexOf(mark) + mark.length());
        val = val.substring(0, val.indexOf("'"));
        return val;
    }

    @Test
    public void logout() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/Logout");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie",
                "R0SzGKvPQ9=MDAwM2IyOWY5MTQwMDAwMDAwMjcwVC0CWGExNDMxNTEwODY1; JSESSIONID=0000X9O9C4zrX5vSh91xs4ms-J1:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void info() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("accname", "路学亮"));
        params.add(new BasicNameValuePair("accnum", "3201000273884880"));
        params.add(new BasicNameValuePair("certinum", "41052719870606543X"));
        params.add(new BasicNameValuePair("prodcode", "1"));
        params.add(new BasicNameValuePair("_PROCID", "80000003"));
        params.add(new BasicNameValuePair("_PAGEID", "step1"));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie", "JSESSIONID=0000g16T5EUINTQsEH5hLIhvmo-:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");
    }

    @Test
    public void login() throws ClientProtocolException, IOException {
        String cookie = "R0SzGKvPQ9=MDAwM2IyOWY5MTQwMDAwMDAwMjcwQS5SLkYxNDMxNDE5ODg5; JSESSIONID=0000Cbbo3bgQ4XahZcHYb29Lhfe:-1";
        String vericode = "2980";

        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/per.login");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("certinum", "41052719870606543X"));
        params.add(new BasicNameValuePair("perpwd", "488000"));
        params.add(new BasicNameValuePair("vericode", vericode));

        post.setEntity(new UrlEncodedFormEntity(params));
        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie", cookie);

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

        // String accnum = this.init(cookie);
    }

    @Test
    public void plogin() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/login-per.jsp");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void vericode() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/vericode.jsp");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");
        post.addHeader("Cookie",
                "R0SzGKvPQ9=MDAwM2IyOWY5MTQwMDAwMDAwMjcwcQdXBVYxNDMxNDIyMTE4; JSESSIONID=0000N1Ua8XfhHHyzJVo5KRTpLYS:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void localhost() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://localhost/verify/code");

        post.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36");

        post.addHeader("Cookie", "JSESSIONID=0000l_45bNHKUE1YNZuydmfYfpM:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

}
