package com.rlogin.common.http;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.impl.client.BasicResponseHandler;

public class NJReserveResponseHandler extends BasicResponseHandler {

	private HttpServletResponse servletResponse;

	public NJReserveResponseHandler() {
		super();
	}

	public NJReserveResponseHandler(HttpServletResponse servletResponse) {
		super();
		this.servletResponse = servletResponse;
	}

	public static void main(String[] args) throws IOException {
		File file = new File("D://ss.txt");
		file.createNewFile();
	}

	@Override
	public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {

		if (servletResponse != null) {
			Header[] headers = response.getHeaders("Set-Cookie");
			for (Header header : headers) {
				servletResponse.addHeader(header.getName(), header.getValue());
			}
		}

		return super.handleResponse(response);
	}
}
