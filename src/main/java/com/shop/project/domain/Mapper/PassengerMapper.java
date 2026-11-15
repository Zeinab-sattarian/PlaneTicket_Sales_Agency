package com.shop.project.domain.Mapper;

import com.shop.project.domain.Passenger;
import com.shop.project.domain.dto.PassengerDTO;

public class PassengerMapper {

    public static PassengerDTO passengerToPassengerDTO(Passenger passenger){
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setUsername(passenger.getUsername());
        return passengerDTO; 
    }
    
}
