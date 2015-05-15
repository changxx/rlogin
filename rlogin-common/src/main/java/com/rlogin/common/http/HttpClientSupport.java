/**
 * 
 */
package com.rlogin.common.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
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

    public static void release() {
        if (cm != null) {
            cm.shutdown();
        }
    }

}
