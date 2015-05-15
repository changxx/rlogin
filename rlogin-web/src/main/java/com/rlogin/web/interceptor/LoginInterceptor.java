package com.rlogin.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    private static final String USER_COOKIE_key = "login_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        return super.preHandle(request, response, handler);

        /*Cookie cookie = this.getCookie(USER_COOKIE_key, request);

        System.out.println(cookie);

        if (cookie == null) {
            cookie = new Cookie(USER_COOKIE_key, "changxx");
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);
        }

        return super.preHandle(request, response, handler);*/
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
