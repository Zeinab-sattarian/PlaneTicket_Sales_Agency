package com.shop.project.service.impl;

import com.shop.project.base.service.impl.BaseServiceImpl;
import com.shop.project.domain.Airline;
import com.shop.project.domain.Flight;
import com.shop.project.domain.Passenger;
import com.shop.project.domain.Ticket;
import com.shop.project.repository.TicketRepository;
import com.shop.project.service.FlightService;
import com.shop.project.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketServiceImpl extends BaseServiceImpl<Ticket, Long, TicketRepository>  implements TicketService {

    @Autowired
    private FlightService flightService;

    @Autowired
    public TicketServiceImpl(TicketRepository repository, FlightService flightService) {
        super(repository);
        this.flightService = flightService;
    }

    @Override
    @Transactional
    public List<Ticket> getByPassenger(Passenger passenger) {

        if (passenger != null) {

            try {

                return repository.findByPassenger(passenger);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;

    }

    @Override
    public List<Ticket> findByFlight(Flight flight){

        if (flight != null) {

            try {

                return repository.findByFlight(flight);

            } catch (Exception e) {

                return null;

            }

        } else
            return null;
    }

    @Override
    @Transactional
    public Ticket buyTicket(Passenger passenger, Flight boughtFlight) {

        Ticket newTicket = null;

        if (boughtFlight == null || passenger == null || boughtFlight.getAvailableSeats()<1)
            return newTicket;

        newTicket = new Ticket();

        newTicket.setPassenger(passenger);

        newTicket.setFlight(boughtFlight);

        newTicket = save(newTicket);

        if (newTicket != null) {
            flightService.allocateSeat(boughtFlight);
        }

        return newTicket;

    }

    @Override
    public List<Ticket> findByAirline(Airline airline) {
        return repository.findByAirline(airline);
    }

    @Override
    public List<Ticket> findByAirlineBetweenDates(Airline airline, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByAirlineBetweenDates(airline, startDate, endDate);
    }

    @Override
    public void refundTicket(Ticket ticket) {
        flightService.deallocateSeat(ticket.getFlight());
        permDelete(ticket);
    }


}
