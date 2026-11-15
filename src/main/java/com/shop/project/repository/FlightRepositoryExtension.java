package com.shop.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;

public interface FlightRepositoryExtension {

    List<Flight> groupAllByOrigin(String order);

    List<Flight> groupAllByDestination(String order);

    List<Flight> groupAllByAirline(String order);

    List<Flight> groupAllByPrice(String order);

    List<Flight> advancedSearch(City origin, City destination, Airline airline, LocalDateTime startDate, LocalDateTime endDate);
    
}
