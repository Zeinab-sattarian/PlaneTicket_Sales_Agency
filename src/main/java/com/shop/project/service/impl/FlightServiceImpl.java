package com.shop.project.service.impl;

import com.shop.project.base.service.impl.BaseServiceImpl;
import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;
import com.shop.project.repository.FlightRepository;
import com.shop.project.service.CityService;
import com.shop.project.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl extends BaseServiceImpl<Flight, Long, FlightRepository> implements FlightService {


    @Autowired
    public FlightServiceImpl(FlightRepository repository) {
        super(repository);
    }

    @Autowired
    private CityService cityService;

    @Override
    @Transactional
    public List<Flight> findByOriginAndDestination(String origin, String destination) {
        try {
            City originCity = cityService.findByName(origin);
            City destinationCity = cityService.findByName(destination);
            return repository.findByOriginOrDestination(originCity, destinationCity);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public Flight save(Flight flight){
        if (flight.getOrigin().getId() != flight.getDestination().getId()) {
            return super.save(flight);
        }else{
            return null;
        }
    }

    @Override
    @Transactional
    public List<Flight> groupAllByOrigin(String order) {

        if (order != null && (order.equals("ASC") || order.equals("DESC"))) {

            try {

                return repository.groupAllByOrigin(order);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    @Transactional
    public List<Flight> groupAllByDestination(String order) {

        if (order != null && (order.equals("ASC") || order.equals("DESC"))) {

            try {

                return repository.groupAllByDestination(order);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    @Transactional
    public List<Flight> groupAllByAirline(String order) {

        if (order != null && (order.equals("ASC") || order.equals("DESC"))) {

            try {

                return repository.groupAllByAirline(order);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    @Transactional
    public List<Flight> groupAllByPrice(String order) {

        if (order != null && (order.equals("ASC") || order.equals("DESC"))) {

            try {

                return repository.groupAllByPrice(order);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    @Transactional
    public Flight createFlight(Airline Airline, String origin, String destination, Integer price, Integer availableSeats, LocalDateTime departureTime) {

        Flight newFlight = new Flight();

        newFlight.setAirline(Airline);

        City origCity = cityService.findByName(origin);

        City destinationCity = cityService.findByName(destination);
        
        newFlight.setOrigin(origCity);

        newFlight.setDestination(destinationCity);

        newFlight.setPrice(price);

        newFlight.setAvailableSeats(availableSeats);

        newFlight.setDepartureTime(departureTime);

        newFlight = save(newFlight);

        return newFlight;
    }

    @Override
    @Transactional
    public Flight createFlight(Airline Airline, City origin, City destination, Integer price, Integer availableSeats, LocalDateTime departureTime) {

        Flight newFlight = new Flight();

        newFlight.setAirline(Airline);
        
        newFlight.setOrigin(origin);

        newFlight.setDestination(destination);

        newFlight.setPrice(price);

        newFlight.setAvailableSeats(availableSeats);

        newFlight.setDepartureTime(departureTime);
        
        newFlight = save(newFlight);

        return newFlight;
    }

    @Override
    public List<Flight> advancedSearch(City origin, City destination, Airline airline, LocalDateTime startDate, LocalDateTime endDate) {
       return repository.advancedSearch(origin, destination, airline, startDate, endDate);
    }

    @Override
    public List<Flight> findByOriginAndDestinationAndTime(String origin, String destination, LocalDate date) {
        
        List<Flight> foundFlights = findByOriginAndDestination(origin, destination);

        return foundFlights.stream().filter(flights -> flights.getDepartureTime().toLocalDate().equals(date)).toList();
    }

    @Override
    public Flight allocateSeat(Flight flight) {
        flight.fillOneSeat();
        return save(flight);
    }

    @Override
    public Flight deallocateSeat(Flight flight) {
        flight.addOneSeat();
        return save(flight);
    }

}
