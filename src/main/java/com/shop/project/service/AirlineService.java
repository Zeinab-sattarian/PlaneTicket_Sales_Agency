package com.shop.project.service;




import java.util.List;

import com.shop.project.base.service.BaseService;
import com.shop.project.domain.Airline;

public interface AirlineService extends BaseService<Airline,Long> {

    Airline findByName(String name);

    Airline createAirline(String newName, String newPassword, String newCallSign, String newHub, String newLogoFileName);

    List<String> getAllAirlineNames();

}
