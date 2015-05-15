package com.rlogin.web.frame;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;

/**
 * @author changxiangxiang
 * @date 2014年9月4日 下午4:04:15
 * @description
 * @since sprint2
 */
public class DefaultVelocityLayoutViewResolver extends VelocityLayoutViewResolver {

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView view = super.buildView(viewName);
        ServletContext context = getServletContext();

        Map<String, Object> attribute = view.getAttributesMap();
        attribute.put("context", context.getContextPath());

        return view;
    }
}
