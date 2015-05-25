package com.rlogin.web.controller;

import com.rlogin.dao.mapper.gjj.GjjLoanStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlogin.dao.mapper.gjj.GjjUserMapper;

/**
 * @author changxiangxiang
 * @date 2014年12月9日 下午8:05:38
 * @description
 */
@Controller
@RequestMapping("")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private GjjUserMapper       gjjUserMapper;

    @Autowired
    private GjjLoanStatusMapper gjjLoanStatusMapper;

    @RequestMapping("")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }
}
