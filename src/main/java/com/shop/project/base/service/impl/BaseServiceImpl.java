package com.shop.project.base.service.impl;

import com.shop.project.base.domain.BaseEntity;
import com.shop.project.base.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public abstract class BaseServiceImpl<T extends BaseEntity<ID>, ID extends Serializable, R extends JpaRepository<T, ID>> implements BaseService<T, ID> {


    protected final R repository;

    public BaseServiceImpl(R repository) {

        this.repository = repository;

    }

    @Override
    @Transactional
    public T save(T t) {

        repository.save(t);

        return t;

    }

    @Override
    @Transactional
    public void delete(T t) {

        t.setIsDeleted(true);
        repository.save(t);

    }

    @Override
    @Transactional
    public void restore(T t){

        t.setIsDeleted(false);
        repository.save(t);


    }

    @Transactional
    @Override
    public void permDelete(T t) {

            repository.delete(t);

    }

    @Override
    @Transactional
    public T findById(ID id) {

        Optional<T> t;

        try {

             t = repository.findById(id);

        } catch (Exception e) {

            return null;

        }

        return t.get();

    }

    @Override
    @Transactional
    public List<T> findAll() {

        List<T> list;

        try {

            list = repository.findAll();

        } catch (Exception e) {

            return null;

        }

        return list;

    }

}
