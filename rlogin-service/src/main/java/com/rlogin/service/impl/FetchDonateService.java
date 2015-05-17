package com.rlogin.service.impl;

import java.io.UnsupportedEncodingException;

import com.rlogin.common.frame.Result;

public interface FetchDonateService {

	/**
	 * 获取用户数据
	 * 
	 * @param certinum
	 * @param pass
	 * @param cookie2
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public Result fetchService(String certinum, String pass, String cookie);
}
