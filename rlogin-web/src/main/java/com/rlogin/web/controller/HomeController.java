package com.rlogin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlogin.dao.TestDao;

/**
 * @author changxiangxiang
 * @date 2014年12月9日 下午8:05:38
 * @description
 */
@Controller
@RequestMapping("")
public class HomeController {

    @Autowired
    private TestDao testDao;

    @RequestMapping("")
    public ModelAndView home() {
        ModelAndView mv = new ModelAndView("home");
        return mv;
    }
}
