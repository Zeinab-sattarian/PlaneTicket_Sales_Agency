package com.shop.project.service.impl;


import com.shop.project.base.service.impl.BaseServiceImpl;
import com.shop.project.domain.Airline;
import com.shop.project.repository.AirlineRepository;
import com.shop.project.service.AirlineService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AirlineServiceImpl extends BaseServiceImpl<Airline, Long, AirlineRepository> implements AirlineService {

    @Autowired
    public AirlineServiceImpl(AirlineRepository repository) {
        super(repository);
    }

    @Override
    public Airline save(Airline airline){
        if (findByName(airline.getName())==null) {
            return super.save(airline);
        }
        return null;
    }
    
    @Override
    @Transactional
    public Airline findByName(String name) {

        if (name != null)
            return repository.findByName(name);
        else
            return null;

    }

    @Override
    @Transactional
    public Airline createAirline(String newName, String newPassword, String newCallSign, String newHub, String newLogoFileName) {

        Airline newAirline = null;

        if (newLogoFileName == null || newLogoFileName.isEmpty()) {
            newLogoFileName = "default_airline.jpg";
        }

        if (newName != null && newPassword != null && !newName.equals("") && !newPassword.equals("") && findByName(newName) == null && newCallSign != null && !newCallSign.equals("") && newHub != null && !newHub.equals("")) {

            newAirline = new Airline();

            newAirline.setName(newName);

            newAirline.setPassword(newPassword);

            newAirline.setCallSign(newCallSign);

            newAirline.setHub(newHub);

            newAirline.setLogoFileName(newLogoFileName);

            newAirline = save(newAirline);
        }

        return newAirline;

    }

    @Override
    public List<String> getAllAirlineNames() {
        return findAll().stream().map(Airline::getName).toList();
    }

}
