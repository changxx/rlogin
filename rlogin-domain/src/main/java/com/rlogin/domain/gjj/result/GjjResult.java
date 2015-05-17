package com.rlogin.domain.gjj.result;

import java.util.Map;

/**
 * 公积金中心返回格式
 * 
 * @author changxx
 *
 */
public class GjjResult {

	private String returnCode;

	private Map<String, String> data;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
