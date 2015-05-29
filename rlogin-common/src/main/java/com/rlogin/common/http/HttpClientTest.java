package com.rlogin.common.http;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

import com.rlogin.domain.gjj.PoolSelect;

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

    public String getDataPool() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpGet post = new HttpGet("http://www.njgjj.com/init.summer?_PROCID=70000002");

        post.addHeader("Cookie", "JSESSIONID=0000qNu4ymNtvAr4Q0GEd8P9Dv_:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println("----------------------------------------");

        String dataPoolStart = "<textarea name=\"_DATAPOOL_\" style=\"display:none\">";
        String dataPool = responseBody
                .substring(responseBody.indexOf(dataPoolStart) + dataPoolStart.length());

        dataPool = dataPool.substring(0, dataPool.indexOf("</textarea>"));

        System.out.println(dataPool);

        PoolSelect poolSelect = this.getPoolSelect(responseBody);

        this.commandSummer(poolSelect);

        return dataPool;
    }

    /**
     * 获取页面中的数据总线
     * @return
     */
    private PoolSelect getPoolSelect(String responseBody) {
        String str = "var poolSelect = ";
        String poolSelectAfter = responseBody.substring(responseBody.indexOf(str) + str.length());
        String poolSelectStr = poolSelectAfter.substring(0, poolSelectAfter.indexOf("}") + 1);

        PoolSelect poolSelect = new PoolSelect();
        for (Field field : poolSelect.getClass().getDeclaredFields()) {
            String val = this.getPoolSelectItem(field.getName(), poolSelectStr);
            System.out.println(val);
            try {
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), poolSelect.getClass());
                Method md = pd.getWriteMethod();
                try {
                    md.invoke(poolSelect, val);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        }

        return poolSelect;
    }

    public void commandSummer(PoolSelect poolSelect) throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());

        post.addHeader("Cookie", "JSESSIONID=0000qNu4ymNtvAr4Q0GEd8P9Dv_:-1");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        for (Field field : poolSelect.getClass().getDeclaredFields()) {
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), poolSelect.getClass());
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            Method md = pd.getReadMethod();
            String val = null;
            try {
                val = (String) md.invoke(poolSelect);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            params.add(new BasicNameValuePair(field.getName(), val));
        }
        params.add(new BasicNameValuePair("begdate", "2006-05-04"));
        params.add(new BasicNameValuePair("enddate", "2015-05-01"));
        params.add(new BasicNameValuePair("accnum", "3201000273884880"));
        params.add(new BasicNameValuePair("accname", "路学亮"));

        post.setEntity(new UrlEncodedFormEntity(params));
        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        System.out.println("commandSummer----------------------------------------start");
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println(responseBody);
        System.out.println("commandSummer----------------------------------------end");
    }
    
    @Test
    public void commandSummer2() throws ClientProtocolException, IOException {
    	HttpClient httpClient = HttpClientSupport.getHttpClient();
    	
    	HttpPost post = new HttpPost("http://www.njgjj.com/command.summer?uuid=" + System.currentTimeMillis());
    	
    	post.addHeader("Cookie", "JSESSIONID=000096MiEwaVa0qEnPpgQSXcpxJ:-1");
    	
    	List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
    	params.add(new BasicNameValuePair("$page", "/ydpx/60000005/600005_01.ydpx"));
    	params.add(new BasicNameValuePair("CURRENT_SYSTEM_DATE", "2015-05-22"));
    	params.add(new BasicNameValuePair("_ACCNAME", "徐伟"));
    	params.add(new BasicNameValuePair("_ACCNUM", "3201000213202785"));
    	params.add(new BasicNameValuePair("BRANCHKIND", "0"));
    	params.add(new BasicNameValuePair("_DEPUTYIDCARDNUM", "321088198507175913"));
    	params.add(new BasicNameValuePair("_IS", "-1709955"));
    	params.add(new BasicNameValuePair("_ISCROP", "0"));
    	params.add(new BasicNameValuePair("_LOGIP", "20150521193613278"));
    	params.add(new BasicNameValuePair("_PAGEID", "step1"));
    	params.add(new BasicNameValuePair("_PORCNAME", "个贷分户查询"));
    	params.add(new BasicNameValuePair("_PROCID", "60000005"));
    	params.add(new BasicNameValuePair("_RW", "w"));
    	params.add(new BasicNameValuePair("_SENDDATE", "2015-05-22"));
    	params.add(new BasicNameValuePair("_SENDOPERID", "2015-05-21"));
    	params.add(new BasicNameValuePair("_TYPE", "init"));
    	params.add(new BasicNameValuePair("_UNITACCNAME", ""));
    	params.add(new BasicNameValuePair("_WITHKEY", "0"));
    	params.add(new BasicNameValuePair("certinum5", "321088198507175913"));
    	params.add(new BasicNameValuePair("isSamePer", "false"));	
    	params.add(new BasicNameValuePair("loanaccnum", "02231210001441470"));
    	params.add(new BasicNameValuePair("termnum", ""));
    	params.add(new BasicNameValuePair("transdate", ""));
    	params.add(new BasicNameValuePair("unitaccname", ""));
    	params.add(new BasicNameValuePair("usebal", ""));
    	params.add(new BasicNameValuePair("yearrpykind", ""));
    	params.add(new BasicNameValuePair("cardno", ""));
    	params.add(new BasicNameValuePair("lmcardno", ""));
    	
    	post.setEntity(new UrlEncodedFormEntity(params));
    	// 创建响应处理器处理服务器响应内容
    	ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
    	// 执行请求并获取结果
    	System.out.println("commandSummer----------------------------------------start");
    	String responseBody = httpClient.execute(post, responseHandler);
    	System.out.println(responseBody);
    	System.out.println("commandSummer----------------------------------------end");
    }

    @Test
    public void test3() throws ClientProtocolException, IOException {
        String datapool = this.getDataPool();

        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis());

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("_DATAPOOL_", datapool));
        params.add(new BasicNameValuePair("_APPLY", "0"));
        params.add(new BasicNameValuePair("_CHANNEL", "1"));
        params.add(new BasicNameValuePair("_PROCID", "70000002"));
        params.add(new BasicNameValuePair("accname", "路学亮"));
        params.add(new BasicNameValuePair("accnum", "3201000273884880"));
        params.add(new BasicNameValuePair("begdate", "2006-05-04"));
        params.add(new BasicNameValuePair("dynamicTable_configSqlCheck", "0"));
        params.add(new BasicNameValuePair("dynamicTable_currentPage", "0"));
        params.add(new BasicNameValuePair("dynamicTable_id", "datalist2"));
        params.add(new BasicNameValuePair("dynamicTable_nextPage", "1"));
        params.add(new BasicNameValuePair("dynamicTable_page", "/ydpx/70000002/700002_01.ydpx"));
        params.add(new BasicNameValuePair("dynamicTable_pageSize", "1000"));
        params.add(new BasicNameValuePair("dynamicTable_paging", "true"));
        params.add(new BasicNameValuePair("enddate", "2015-05-17"));
        params.add(new BasicNameValuePair("errorFilter", "1=1"));

        post.setEntity(new UrlEncodedFormEntity(params));

        post.addHeader("Cookie", "JSESSIONID=0000qNu4ymNtvAr4Q0GEd8P9Dv_:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");
    }

    @Test
    public void test4() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpPost post = new HttpPost("http://njgjj.com/submit.summer?uuid=" + System.currentTimeMillis() + "");

        post.addHeader("Cookie", "JSESSIONID=0000lC_Ai3_9LF_BwQR10p5Kb11:-1");

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("DATAlISTGHOST", "rO0ABXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAAAdwQAAAAKeA=="));
        params.add(new BasicNameValuePair("_APPLY", "0"));
        params.add(new BasicNameValuePair("CHANNEL", "1"));
        params.add(new BasicNameValuePair("_PROCID", "60000008"));
        params.add(new BasicNameValuePair("accname", "徐伟"));
        params.add(new BasicNameValuePair("accnum", "3201000213202785"));
        params.add(new BasicNameValuePair("accnum1", ""));
        params.add(new BasicNameValuePair("accstate", "0"));
        params.add(new BasicNameValuePair("accstate1", ""));
        params.add(new BasicNameValuePair("acctype", "1"));
        params.add(new BasicNameValuePair("acctype1", ""));
        params.add(new BasicNameValuePair("age", "30"));
        params.add(new BasicNameValuePair("age1", ""));
        params.add(new BasicNameValuePair("amount1", "800000.00"));
        params.add(new BasicNameValuePair("bal", "5770.57"));
        params.add(new BasicNameValuePair("bal1", ""));
        params.add(new BasicNameValuePair("basenum", "5000.00"));
        params.add(new BasicNameValuePair("basenum1", ""));
        params.add(new BasicNameValuePair("begpayym", "201502"));
        params.add(new BasicNameValuePair("begpayym1", ""));
        params.add(new BasicNameValuePair("birthday", "1985-07-17"));
        params.add(new BasicNameValuePair("birthday1", "201502"));
        params.add(new BasicNameValuePair("buyhousearea", "80"));
        params.add(new BasicNameValuePair("cardaccnum", "320100021320278500"));
        params.add(new BasicNameValuePair("cardaccnum1", ""));
        params.add(new BasicNameValuePair("certinum", "321088198507175913"));
        params.add(new BasicNameValuePair("certitype", "1"));
        params.add(new BasicNameValuePair("custid", "205949339"));
        params.add(new BasicNameValuePair("custid1", ""));
        params.add(new BasicNameValuePair("depositmflag", "1"));
        params.add(new BasicNameValuePair("depositmflag1", ""));
        params.add(new BasicNameValuePair("deposityflag2", ""));
        params.add(new BasicNameValuePair("firsthousearea", ""));
        params.add(new BasicNameValuePair("flag", "0"));
        params.add(new BasicNameValuePair("frzflag", "0"));
        params.add(new BasicNameValuePair("frzflag1", ""));
        params.add(new BasicNameValuePair("housecount", ""));
        params.add(new BasicNameValuePair("housetype", "01"));
        params.add(new BasicNameValuePair("indiprop", "8"));
        params.add(new BasicNameValuePair("indiprop1", ""));
        params.add(new BasicNameValuePair("ishas", "0"));
        params.add(new BasicNameValuePair("islocaltime", "0"));
        params.add(new BasicNameValuePair("lastdrawdate", ""));
        params.add(new BasicNameValuePair("lastdrawdate1", ""));
        params.add(new BasicNameValuePair("lmcardno", ""));
        params.add(new BasicNameValuePair("lmcardno1", ""));
        params.add(new BasicNameValuePair("loanhouseflag", "0"));
        params.add(new BasicNameValuePair("loanhousenum", ""));
        params.add(new BasicNameValuePair("loantime", "1"));
        params.add(new BasicNameValuePair("lpaym", ""));
        params.add(new BasicNameValuePair("lpaym1", ""));
        params.add(new BasicNameValuePair("matecertinum", ""));
        params.add(new BasicNameValuePair("matecertitype", ""));
        params.add(new BasicNameValuePair("matename", ""));
        params.add(new BasicNameValuePair("monpaysum", "800.00"));
        params.add(new BasicNameValuePair("monpaysum1", ""));
        params.add(new BasicNameValuePair("opnaccdate", "2007-08-21"));
        params.add(new BasicNameValuePair("othplace", "1"));
        params.add(new BasicNameValuePair("othplace1", ""));
        params.add(new BasicNameValuePair("perhouserarea", ""));
        params.add(new BasicNameValuePair("sex", "1"));
        params.add(new BasicNameValuePair("sex1", ""));
        params.add(new BasicNameValuePair("techpost", "5"));
        params.add(new BasicNameValuePair("techpost1", ""));
        params.add(new BasicNameValuePair("times", ""));
        params.add(new BasicNameValuePair("totalprop", "16"));
        params.add(new BasicNameValuePair("totalprop1", ""));
        params.add(new BasicNameValuePair("unitaccname", "南京消防器材股份有限公司"));
        params.add(new BasicNameValuePair("unitaccname1", ""));
        params.add(new BasicNameValuePair("unitaccnum", "20101133193"));
        params.add(new BasicNameValuePair("unitaccnum1", ""));
        params.add(new BasicNameValuePair("unitkind", "13"));
        params.add(new BasicNameValuePair("unitkind1", ""));
        params.add(new BasicNameValuePair("unitprop", "8"));
        params.add(new BasicNameValuePair("unitprop1", ""));
        params.add(new BasicNameValuePair("validflag", "0"));
        params.add(new BasicNameValuePair("validflag1", ""));

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

    }

    @Test
    public void test5() throws ClientProtocolException, IOException {
        HttpClient httpClient = HttpClientSupport.getHttpClient();

        HttpGet post = new HttpGet("http://njgjj.com/dynamictable?uuid=" + System.currentTimeMillis() + "");

        post.addHeader("Cookie", "JSESSIONID=00000I30w3f_Zw3QzUzzBM1sCHh:-1");

        // 创建响应处理器处理服务器响应内容
        ResponseHandler<String> responseHandler = new NJReserveResponseHandler();
        // 执行请求并获取结果
        String responseBody = httpClient.execute(post, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
        System.out.println("----------------------------------------");

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
