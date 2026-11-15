package com.shop.project.base.service;

import com.shop.project.base.domain.BaseEntity;
import java.io.Serializable;
import java.util.List;

public interface BaseService<T extends BaseEntity<ID>, ID extends Serializable> {

    T save(T t);

    void delete(T t);

    void restore(T t);

    void permDelete(T t);

    T findById(ID id);

    List<T> findAll();

}
