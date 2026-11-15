package com.shop.project.domain.Mapper;

import java.util.ArrayList;
import java.util.List;

import com.shop.project.domain.Ticket;
import com.shop.project.domain.dto.TicketDTO;

public class TicketMapper {

    public static TicketDTO TicketToTicketDTO(Ticket ticket){
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setFlightDTO(FlightMapper.flightToFlightDTO(ticket.getFlight()));
        ticketDTO.setPassengerDTO(PassengerMapper.passengerToPassengerDTO(ticket.getPassenger()));
        return ticketDTO;
    }

    public static List<TicketDTO> TicketListToTicketDTOList(List<Ticket> tickets){
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(TicketMapper.TicketToTicketDTO(ticket));            
        }
        return ticketDTOs;
    }


    
}
