package com.shop.project.service;


import com.shop.project.base.service.BaseService;
import com.shop.project.domain.Airline;
import com.shop.project.domain.Flight;
import com.shop.project.domain.Passenger;
import com.shop.project.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.query.Param;

public interface TicketService extends BaseService<Ticket,Long>{

    List<Ticket> getByPassenger(Passenger passenger);

    List<Ticket> findByFlight(Flight flight);

    Ticket buyTicket(Passenger passenger, Flight flight);

    void refundTicket(Ticket ticket);

    List<Ticket> findByAirline(@Param("airline") Airline airline);

    List<Ticket> findByAirlineBetweenDates(@Param("airline") Airline airline, @Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);

}
