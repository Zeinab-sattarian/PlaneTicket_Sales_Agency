package com.shop.project.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.project.base.service.impl.BaseServiceImpl;
import com.shop.project.domain.City;
import com.shop.project.repository.CityRepository;
import com.shop.project.service.CityService;

@Service
public class CityServiceImpl extends BaseServiceImpl<City, Long, CityRepository> implements CityService{

   @Autowired
    public CityServiceImpl(CityRepository repository) {
        super(repository);
    }

    @Override
    public City findByName(String name){
        return repository.findByName(name);
    }

    @Override
    public List<String> getAllCityNames() {
        return findAll().stream().map(City::getName).toList();
    }
    
}
