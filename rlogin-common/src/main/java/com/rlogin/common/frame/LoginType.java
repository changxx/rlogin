package com.rlogin.common.frame;

public enum LoginType {

    /**
     * 无需认证直接通过
     */
    NONE,

    /**
     * 公共资源,需登录
     */
    PUBLIC,

    /**
     * 需要登录
     */
    LOGIN,

    /**
     * 功能权限验证
     */
    FUNCTION,

    /**
     * 请求需中包含指定Token值
     */
    TOKEN
}
