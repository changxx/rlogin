/**
 *
 */
package com.rlogin.common.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author cdtanlong@jd.com 2013-6-19 上午11:33:50 Function :
 * @copyright www.jd.com
 */
@SuppressWarnings("deprecation")
public class HttpClientSupport {

    private static final Logger log = LoggerFactory.getLogger(HttpClientSupport.class);

    private static PoolingClientConnectionManager cm;

    private static HttpParams httpParams;

    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 60000;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 500;

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 200;

    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 60000;

    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 60000;

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

        if (params != null) {
            try {
                post.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                log.error("httpClient error:{}", e);
            }
        }

        if (cookie != null) {
            post.addHeader("Cookie", cookie);
        }

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = null;
        try {
            responseBody = httpClient.execute(post, responseHandler);
        } catch (ClientProtocolException e) {
            log.error("httpClient error:{}", e);
        } catch (IOException e) {
            log.error("httpClient error:{}", e);
        }

        return responseBody;
    }

    public static String httpsPost(String url, String cookie, List<BasicNameValuePair> params) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);

            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            post = new HttpPost(url);

            if (params != null) {
                try {
                    post.setEntity(new UrlEncodedFormEntity(params));
                } catch (UnsupportedEncodingException e) {
                    log.error("httpClient error:{}", e);
                }
            }

            if (cookie != null) {
                post.addHeader("Cookie", cookie);
            }

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String responseBody = httpClient.execute(post, responseHandler);

            return responseBody;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("httpClient error:{}", e);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("httpClient error:{}", ex);
        }

        if (post != null) {
            post.abort();
        }

        return null;
    }

    public static String httpsPost(String url, String cookie, List<BasicNameValuePair> params, ResponseHandler<String> responseHandler) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = null;
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");

            X509TrustManager tm = new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);

            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", 443, ssf));

            post = new HttpPost(url);

            if (params != null) {
                try {
                    post.setEntity(new UrlEncodedFormEntity(params));
                } catch (UnsupportedEncodingException e) {
                    log.error("httpClient error:{}", e);
                }
            }

            if (cookie != null) {
                post.addHeader("Cookie", cookie);
            }

            if (responseHandler == null) {
                responseHandler = new BasicResponseHandler();
            }

            String responseBody = httpClient.execute(post, responseHandler);

            return responseBody;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("httpClient error:{}", e);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("httpClient error:{}", ex);
        }

        if (post != null) {
            post.abort();
        }

        return null;
    }

    public static void main(String[] args) {
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("org.apache.struts.taglib.html.TOKEN", "7ef8edfc0f5f1d5263b0f61a656fed2b"));
        params.add(new BasicNameValuePair("method", "login"));
        params.add(new BasicNameValuePair("date", "1432824735346"));
        params.add(new BasicNameValuePair("loginname", "changxx"));
        params.add(new BasicNameValuePair("password", "981188321chang"));
        params.add(new BasicNameValuePair("_@IMGRC@_", "5jet9y"));

        String resp = HttpClientSupport.httpsPost("https://ipcrs.pbccrc.org.cn/page/login/login.do",
                "JSESSIONID=KzGdVnjQQsxvChyySy8HTvG2wKJfQLdspZnmy7SJ2fYhdpJyHGSW!2086359231; BIGipServerpool_ipcrs_app=D/X39tUqMMn9qkwF9TwayuIh5sFUdrULrkHkm2iFoPTZOzSLp5KGue3Oumftf9ffavV7A0jk7AXB0AWoIZI034S8hc4AeZv/mj/XnVbaPmarj8ztAJlkJXh9ZlQkv9rZBHb9rzUxc5GUqJ1XhSjVxHpguEuU8g==; ipcrs=51o57ISn8ljkSGMF9TwayuIh5sFUdhTA/BYZhMK1leqJcbE8dYItyyHRyGNhMweCne0W9grCWUw6; TSf75e5b=5a6446ace1e5b129ff5c05d42417d2a4f205f691afc9e34055672cc1", params);

        System.out.println("resp: " + resp);
    }

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }

}
