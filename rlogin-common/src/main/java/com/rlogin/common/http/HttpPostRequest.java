package com.rlogin.common.http;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;

public class HttpPostRequest {

    private HttpClient httpClient;

    private HttpPost   post;

    public HttpPostRequest(HttpClient httpClient, HttpPost post) {
        super();
        this.httpClient = httpClient;
        this.post = post;
    }

    public String request(List<BasicNameValuePair> params) {

        String result = null;

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
            post.setEntity(entity);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            result = httpClient.execute(post, responseHandler);

        } catch (Exception e) {
            post.abort();
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }

        return result;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpPost getPost() {
        return post;
    }

    public void setPost(HttpPost post) {
        this.post = post;
    }

}
