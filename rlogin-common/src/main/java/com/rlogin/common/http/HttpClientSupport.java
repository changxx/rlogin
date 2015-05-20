/**
 * 
 */
package com.rlogin.common.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

/**
 * @copyright www.jd.com
 * @author cdtanlong@jd.com 2013-6-19 上午11:33:50 Function :
 */
@SuppressWarnings("deprecation")
public class HttpClientSupport {

    private static PoolingClientConnectionManager cm;

    private static HttpParams                     httpParams;

    /**
     * 获取连接的最大等待时间
     */
    public final static int                       WAIT_TIMEOUT          = 60000;

    /**
     * 最大连接数
     */
    public final static int                       MAX_TOTAL_CONNECTIONS = 500;

    /**
     * 每个路由最大连接数
     */
    public final static int                       MAX_ROUTE_CONNECTIONS = 200;

    /**
     * 连接超时时间
     */
    public final static int                       CONNECT_TIMEOUT       = 60000;

    /**
     * 读取超时时间
     */
    public final static int                       READ_TIMEOUT          = 60000;

    static {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        cm = new PoolingClientConnectionManager(registry);
        // 设置最大连接数
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        // 设置每个路由最大连接数
        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
        httpParams = new BasicHttpParams();
        // 设置连接超时时间
        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
        // 设置读取超时时间
        httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT);
        // 设置HTTP协议版本
        httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        // 设置获取连接的最大等待时间
        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);

    }

    public static DefaultHttpClient getHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);
        return httpClient;
    }

    public static String post(String url, String cookie, List<BasicNameValuePair> params) {
        DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);

        HttpPost post = new HttpPost(url);

        try {
            post.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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

        return responseBody;
    }

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }

}
