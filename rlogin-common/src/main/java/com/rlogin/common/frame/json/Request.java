package com.rlogin.common.frame.json;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求的帮助类
 * @author changxiangxiang
 */
public class Request {

    /**
     * 判断是否为异步提交
     */
    public static boolean isAjax(HttpServletRequest request) {

        String requestType = request.getHeader("X-Requested-With");
        if (request.getRequestURI().endsWith(".json")
                || (requestType != null && requestType.equals("XMLHttpRequest"))) {
            return true;
        } else {
            return false;
        }

    }
}
