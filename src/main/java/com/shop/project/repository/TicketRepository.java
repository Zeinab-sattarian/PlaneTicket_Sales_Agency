package com.shop.project.repository;

import com.shop.project.domain.Airline;
import com.shop.project.domain.Flight;
import com.shop.project.domain.Passenger;
import com.shop.project.domain.Ticket;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByPassenger(Passenger passenger);

    List<Ticket> findByFlight(Flight flight);

    @Query("SELECT t FROM Ticket t WHERE t.flight.airline = :airline")
    List<Ticket> findByAirline(@Param("airline") Airline airline);

    @Query("SELECT t FROM Ticket t WHERE t.flight.airline = :airline AND t.flight.departureTime BETWEEN :startDate AND :endDate")
    List<Ticket> findByAirlineBetweenDates(@Param("airline") Airline airline, @Param("startDate") LocalDateTime startDate,@Param("endDate") LocalDateTime endDate);
}
