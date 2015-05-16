package com.rlogin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.rlogin.dao.mapper.gjj.GjjUserMapper;
import com.rlogin.domain.gjj.GjjUser;
import com.rlogin.domain.gjj.GjjUserExample;

/**
 * @author changxiangxiang
 * @date 2014年12月9日 下午8:05:38
 * @description
 */
@Controller
@RequestMapping("")
public class HomeController {

	@Autowired
	private GjjUserMapper gjjUserMapper;

	@RequestMapping("")
	public ModelAndView home() {
		ModelAndView mv = new ModelAndView("home");

		GjjUser gjjUser = new GjjUser();
		gjjUser.setAccId("s");
		gjjUser.setAccPwd("ss");
		gjjUser.setName("ss");
		//int i = gjjUserMapper.insert(gjjUser);
		
		GjjUserExample gjjUserExample = new GjjUserExample();
		gjjUserExample.createCriteria().andNameEqualTo("ss");
		
		System.out.println(gjjUserMapper.selectByExample(gjjUserExample));
		return mv;
	}
}
