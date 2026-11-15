package com.shop.project.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class FlightDTO {

    private Long Id;

    private String origin;

    private String destination;

    private String airline;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private int availableSeats;

    private int price;

}
