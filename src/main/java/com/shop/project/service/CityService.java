package com.shop.project.service;

import java.util.List;

import com.shop.project.base.service.BaseService;
import com.shop.project.domain.City;

public interface CityService extends BaseService<City,Long>{
    
    public City findByName(String name);

    public List<String> getAllCityNames();

}
