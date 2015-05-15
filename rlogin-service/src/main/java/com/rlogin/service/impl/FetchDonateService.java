package com.rlogin.service.impl;

import java.io.UnsupportedEncodingException;

import com.rlogin.common.frame.Result;

public interface FetchDonateService {

    public Result fetchService(String certinum, String cookie) throws UnsupportedEncodingException;
}
