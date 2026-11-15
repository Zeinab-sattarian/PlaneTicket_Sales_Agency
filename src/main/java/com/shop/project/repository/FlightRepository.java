package com.shop.project.repository;

import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FlightRepository extends JpaRepository<Flight, Long> , FlightRepositoryExtension {

        public List<Flight> findByOriginOrDestination(City Origin, City Destination);

        public List<Flight> findByAirline(Airline airline);
}
