package com.shop.project.domain.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FlightAdvancedSearchDTO {
    private String origin;
    private String destination;
    private String airline;
    private LocalDate startDate;
    private LocalDate endDate;
}
