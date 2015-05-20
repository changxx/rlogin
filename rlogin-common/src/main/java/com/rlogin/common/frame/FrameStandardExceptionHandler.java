package com.rlogin.common.frame;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.rlogin.common.frame.exception.FrameBSHandledException;
import com.rlogin.common.frame.exception.FrameIllegalPrivilegeException;
import com.rlogin.common.frame.exception.FrameSignException;
import com.rlogin.common.frame.json.Response;
import com.rlogin.common.frame.json.Result;

/**
 * 全局统一异常处理 changxiangxiang
 */
public class FrameStandardExceptionHandler extends SimpleMappingExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(FrameStandardExceptionHandler.class);

    /**
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {

        if (!(handler == null || handler instanceof HandlerMethod)) {
            return null;
        }

        String name = "anonymous";

        String uri = request.getRequestURI();

        // 异步请求
        if (isResponseBody(handler) || com.rlogin.common.frame.json.Request.isAjax(request)) {
            return handleResponseBodyException(name, uri, ex, request, response);
        }
        // 其他
        else {
            return handleNormalException(name, uri, ex, request, response);
        }
    }

    private boolean isResponseBody(Object handler) {
        ResponseBody isResponseBody = null;
        try {
            HandlerMethod method = (HandlerMethod) handler;
            if (method != null) {
                isResponseBody = method.getMethodAnnotation(ResponseBody.class);
            }
        } catch (Exception e) {}
        return isResponseBody != null;
    }

    /**
     * 创建日志构造对象
     */
    private Parameter newParameter(String uuid, String uri, Exception ex) {
        Parameter parameter = new Parameter();
        parameter.put("uuid", uuid);
        parameter.put("uri", uri);
        parameter.put("exception", ex);
        return parameter;
    }

    /**
     * 处理异步请求或者返回json
     */
    private ModelAndView handleResponseBodyException(String uuid, String uri, Exception ex,
            HttpServletRequest request, HttpServletResponse response) {
        // 业务异常
        if (ex instanceof FrameSignException) {
            LOG.error("{}", new Object[] { newParameter(uuid, uri, ex) });
            Response.push(response, new Result(Result.ERROR, ex.getMessage()));
        }
        // 业务异常
        else if (ex instanceof FrameBSHandledException) {
            LOG.warn("{}", new Object[] { newParameter(uuid, uri, ex) });
            Response.push(response, new Result(Result.ERROR, ex.getMessage()));
        }
        // 权限异常
        else if (ex instanceof FrameIllegalPrivilegeException) {
            LOG.warn("{}", new Object[] { newParameter(uuid, uri, ex) });
            Response.push(response, new Result(Result.ERROR, "对不起，您没有该项的访问权限。"));
        }
        // 其他
        else {
            LOG.error("{}", new Object[] { newParameter(uuid, uri, ex), ex });
            Response.push(response, new Result(Result.ERROR, "遇到了意外情况，无法完成您的请求"));
        }
        // 不返回空
        return new ModelAndView();
    }

    /**
     * 处理普通请求
     */
    private ModelAndView handleNormalException(String uuid, String uri, Exception ex,
            HttpServletRequest request, HttpServletResponse response) {

        String viewName = super.determineViewName(ex, request);
        if (viewName != null) {

            if (ex != null
                    && (ex instanceof FrameBSHandledException || ex instanceof FrameIllegalPrivilegeException)) {
                LOG.warn("{}", new Object[] { newParameter(uuid, uri, ex) });
            } else {
                LOG.error("{}", new Object[] { newParameter(uuid, uri, ex), ex });
            }

            Integer statusCode = super.determineStatusCode(request, viewName);
            if (statusCode != null) {
                super.applyStatusCodeIfPossible(request, response, statusCode.intValue());
            }

            ModelAndView mv = super.getModelAndView(viewName, ex, request);
            if (ex != null && (ex instanceof FrameBSHandledException)) {
                mv.addObject("message", ex.getMessage());
            }
            return mv;

        } else {

            return null;
        }
    }

}
