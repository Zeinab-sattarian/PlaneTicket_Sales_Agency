package com.shop.project.service;


import com.shop.project.base.service.BaseService;
import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightService extends BaseService<Flight,Long>{

    List<Flight> findByOriginAndDestination(String origin, String destination);

    List<Flight> groupAllByOrigin(String order);

    List<Flight> groupAllByDestination(String order);

    List<Flight> groupAllByAirline(String order);

    List<Flight> groupAllByPrice(String order);

    Flight createFlight(Airline Airline, String origin, String destination, Integer price, Integer availableSeats, LocalDateTime departureTime);

    Flight createFlight(Airline Airline, City origin, City destination, Integer price, Integer availableSeats, LocalDateTime departureTime);

    List<Flight> advancedSearch(City origin, City destination, Airline airline, LocalDateTime startDate, LocalDateTime endDate);

    List<Flight> findByOriginAndDestinationAndTime(String origin, String destination, LocalDate date);

    Flight allocateSeat(Flight flight);

    Flight deallocateSeat(Flight flight);

}
