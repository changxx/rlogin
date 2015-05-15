package com.rlogin.dao.base;

import java.util.List;

import com.rlogin.domain.base.Domain;
import com.rlogin.domain.base.Query;

public interface BaseDao<T extends Domain> {

    Integer insert(T t);

    Integer delete(Object id);

    Integer update(T t);

    T find(Object id);

    List<T> list(Query query);

    Integer count(Query query);

}
