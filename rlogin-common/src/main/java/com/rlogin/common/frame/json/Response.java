package com.rlogin.common.frame.json;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 返回结果处理类
 */
public class Response {

    /**
     * 输出
     */
    public static final void push(HttpServletResponse response, Result result) {

        try {
            response.setContentType("application/json");
            PrintWriter wt = response.getWriter();
            wt.write(result.toString());
            wt.flush();
        } catch (Exception e) {}

    }

}
