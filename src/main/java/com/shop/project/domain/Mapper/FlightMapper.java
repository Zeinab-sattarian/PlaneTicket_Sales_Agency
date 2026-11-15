package com.shop.project.domain.Mapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.shop.project.domain.Flight;
import com.shop.project.domain.dto.FlightDTO;

public class FlightMapper {
    
    public static Flight flightDtoToFlight(FlightDTO flightDTO){
        Flight flight = new Flight();
        flight.setDepartureTime(LocalDateTime.of(flightDTO.getDepartureDate(), flightDTO.getDepartureTime()==null ?LocalTime.of(0, 0, 0) :flightDTO.getDepartureTime()));
        flight.setAvailableSeats(flightDTO.getAvailableSeats());
        flight.setPrice(flightDTO.getPrice());
        return flight;
    }

    public static FlightDTO flightToFlightDTO(Flight flight){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setAvailableSeats(flight.getAvailableSeats());
        flightDTO.setDepartureDate(flight.getDepartureTime().toLocalDate());
        flightDTO.setDepartureTime(flight.getDepartureTime().toLocalTime());
        flightDTO.setId(flight.getId());
        flightDTO.setPrice(flight.getPrice());
        flightDTO.setOrigin(flight.getOrigin().getName());
        flightDTO.setDestination(flight.getDestination().getName());
        flightDTO.setAirline(flight.getAirline().getName());
        return flightDTO;
    }

    public static List<FlightDTO> flightListToFlightDTOList(List<Flight> flights){
        List<FlightDTO> flightDTOs = new ArrayList<>();
        for (Flight flight : flights) {
            flightDTOs.add(FlightMapper.flightToFlightDTO(flight));
        }
        return flightDTOs;
    }

}
