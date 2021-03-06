package com.rlogin.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rlogin.common.frame.Login;
import com.rlogin.common.frame.LoginType;
import com.rlogin.common.util.DesUtil;
import com.rlogin.domain.Constant;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler == null || handler instanceof HandlerMethod)) {
            return true;
        }

        Cookie cookie = this.getCookie(Constant.USER_COOKIE_KEY, request);

        if ("/favicon.ico".equals(request.getRequestURI())) {
            return false;
        }

        HandlerMethod method = (HandlerMethod) handler;

        Login login = method.getMethodAnnotation(Login.class);

        if (login != null && login.type() == LoginType.LOGIN && cookie == null) {
            // 未登录跳转登录界面
            response.sendRedirect("/gjj/login");
            return false;
        }

        if (cookie != null) {
            request.setAttribute(Constant.USER_COOKIE_KEY, DesUtil.decode(cookie.getValue()));
        }

        return super.preHandle(request, response, handler);
    }

    private Cookie getCookie(String key, HttpServletRequest request) {
        if (request.getCookies() != null && StringUtils.isNotBlank(key)) {
            for (Cookie cookie : request.getCookies()) {
                if (key.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

}
