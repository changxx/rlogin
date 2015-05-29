package com.rlogin.common.http;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

public class ImageResponseHandler extends BasicResponseHandler {

    private HttpServletResponse servletResponse;

    public ImageResponseHandler() {
        super();
    }

    public ImageResponseHandler(HttpServletResponse servletResponse) {
        super();
        this.servletResponse = servletResponse;
    }

    @Override
    public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
        Header[] headers = response.getHeaders("Set-Cookie");

        if (servletResponse != null) {
            for (Header header : headers) {
                System.out.println("服务器返回：" + header.getName() + ": " + header.getValue());
                servletResponse.addHeader(header.getName(), header.getValue());
            }
        }

        OutputStream os = servletResponse.getOutputStream();
        response.getEntity().writeTo(os);

        return null;
    }
}
