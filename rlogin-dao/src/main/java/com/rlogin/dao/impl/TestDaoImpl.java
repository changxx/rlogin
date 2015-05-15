package com.rlogin.dao.impl;

import org.springframework.stereotype.Repository;

import com.rlogin.dao.TestDao;
import com.rlogin.dao.base.impl.BaseDaoImpl;
import com.rlogin.domain.Test;

@Repository
public class TestDaoImpl extends BaseDaoImpl<Test> implements TestDao {

}
