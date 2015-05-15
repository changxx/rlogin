package com.rlogin.dao.base.impl;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.rlogin.dao.base.BaseDao;
import com.rlogin.domain.base.Domain;
import com.rlogin.domain.base.Query;

public abstract class BaseDaoImpl<T extends Domain> extends SqlSessionDaoSupport implements BaseDao<T> {

    public String getNameSpace() {
        return this.getClass().getName();
    }

    public Integer insert(T t) {
        return this.getSqlSession().insert(getNameSpace() + ".insert", t);
    }

    public Integer update(T t) {
        return this.getSqlSession().update(this.getNameSpace() + ".update", t);
    }

    public Integer delete(Object id) {
        return this.getSqlSession().update(this.getNameSpace() + ".delete", id);
    }

    @SuppressWarnings("unchecked")
    public T find(Object id) {
        return (T) this.getSqlSession().selectOne(this.getNameSpace() + ".find", id);
    }

    public List<T> list(Query query) {
        return this.getSqlSession().selectList(this.getNameSpace() + ".list", query);
    }

    public Integer count(Query query) {
        return (Integer) this.getSqlSession().selectOne(this.getNameSpace() + ".count", query);
    }

}
