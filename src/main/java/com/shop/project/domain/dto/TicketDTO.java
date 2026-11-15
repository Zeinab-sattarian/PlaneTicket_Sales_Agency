package com.shop.project.domain.dto;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;

    private FlightDTO flightDTO;

    private PassengerDTO passengerDTO;
}
