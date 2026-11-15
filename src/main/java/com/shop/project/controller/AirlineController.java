package com.shop.project.controller;


import com.shop.project.domain.Airline;
import com.shop.project.domain.City;
import com.shop.project.domain.Flight;
import com.shop.project.domain.Ticket;
import com.shop.project.domain.Mapper.FlightMapper;
import com.shop.project.domain.Mapper.TicketMapper;
import com.shop.project.domain.dto.FlightAdvancedSearchDTO;
import com.shop.project.domain.dto.FlightDTO;
import com.shop.project.domain.dto.TicketDTO;
import com.shop.project.service.AirlineService;
import com.shop.project.service.CityService;
import com.shop.project.service.FlightService;
import com.shop.project.service.MediaService;
import com.shop.project.service.TicketService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/Airline")
public class AirlineController {

    @Autowired
    AirlineService airlineService;

    @Autowired
    FlightService flightService;

    @Autowired
    CityService cityService;

    @Autowired
    MediaService mediaService;

    @Autowired
    TicketService ticketService;

    @GetMapping("/signup")
    public String signupForm(Model model){

        Airline Airline = new Airline();

        model.addAttribute("Airline",Airline);

        return "Airline/AirlineSignup";

    }

    @PostMapping("/signup")
    public String signupAirline(Model model , @ModelAttribute("Airline") Airline airline, HttpServletResponse response, @RequestParam("logo") MultipartFile logo){

        if (!logo.isEmpty()) {
            airline.setLogoFileName(mediaService.saveAirlineLogo(logo));
        }

        Airline newAirline = airlineService.createAirline(airline.getName(), airline.getPassword(), airline.getCallSign(), airline.getHub(), airline.getLogoFileName());

        if (newAirline!=null){

            Cookie cookie = new Cookie("AirlineId" , newAirline.getId().toString());

            cookie.setMaxAge(60*15);

            response.addCookie(cookie);


            return "redirect:login";
        }
        else
            return "redirect:/message/failed-signup";

    }

    @GetMapping("/login")
    public String loginForm(Model model , HttpServletRequest httpRequest){

        Airline loggedInAirline = getLoggedInAirline(httpRequest);

        if ( loggedInAirline != null) {
            model.addAttribute("loggedInAirline" , loggedInAirline);
            if (loggedInAirline.getLogoFileName()!=null) {
                model.addAttribute("logoPath", mediaService.resolveLogoAddress(loggedInAirline.getLogoFileName()));
            }
            return "Airline/AirlineDashboard";
        }

        model.addAttribute("airline" , new Airline());
        return "Airline/AirlineLogin";

    }

    @PostMapping("/login")
    public String loginAirline(Model model , @ModelAttribute("Airline") Airline Airline, HttpServletResponse response){

        Airline checkAirline = airlineService.findByName(Airline.getName());

        if (checkAirline==null)
            return "redirect:/message/failed-login";

        if (checkAirline.getPassword().equals(Airline.getPassword())){
            
            Cookie cookie = new Cookie("AirlineId" , checkAirline.getId().toString());

            cookie.setMaxAge(60*15);

            response.addCookie(cookie);

            model.addAttribute("loggedInAirline" , checkAirline);
            model.addAttribute("logoPath", mediaService.resolveLogoAddress(checkAirline.getLogoFileName()));

            return "Airline/AirlineDashboard";

        }

        else
            return "redirect:/message/failed-login";

    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){

        Cookie cookie = new Cookie("AirlineId" ,null);

        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "redirect:/";

    }

    @GetMapping("/dashboard/create-flight")
    public String createFlightForm(Model model, HttpServletRequest request){

        if (getLoggedInAirline(request) == null) {
            return "redirect:/Airline/login";
        }

        model.addAttribute("cities",cityService.getAllCityNames());

        model.addAttribute("flight", new FlightDTO());

        return "Airline/CreateFlight";

    }

    @PostMapping("/dashboard/create-flight")
    public String createFlight(Model model , @ModelAttribute FlightDTO flightDTO, HttpServletRequest request){

        Airline loggedInAirline = getLoggedInAirline(request);

        Flight flight = FlightMapper.flightDtoToFlight(flightDTO);

        flight.setOrigin(cityService.findByName(flightDTO.getOrigin())); 

        flight.setDestination(cityService.findByName(flightDTO.getDestination())); 
        
        flight.setAirline(loggedInAirline);

        flight = flightService.save(flight);

        if (flight!=null){
            return "redirect:/Airline/login";
        }
        else
            return "redirect:/message/flight-not-created";

    }

    @GetMapping("/dashboard/search-flight")
    public String searchFlightForm(Model model){

        model.addAttribute("cities",cityService.getAllCityNames());

        model.addAttribute("airlines",airlineService.getAllAirlineNames());

        model.addAttribute("flight", new FlightAdvancedSearchDTO());

        return "Airline/SearchAllFlights";

    }

    @PostMapping("/dashboard/search-flight")
    public String viewFlights(Model model, @ModelAttribute FlightAdvancedSearchDTO flightAdvancedSearchDTO){

        List<Flight> foundFlights;

        City origin = !flightAdvancedSearchDTO.getOrigin().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getOrigin()) : null;
        City destination = !flightAdvancedSearchDTO.getDestination().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getDestination()) : null;
        Airline airline = !flightAdvancedSearchDTO.getAirline().isEmpty() ? airlineService.findByName(flightAdvancedSearchDTO.getAirline()) : null;
        LocalDateTime startTime = flightAdvancedSearchDTO.getStartDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getStartDate(),LocalTime.of(0,0,0)) : null;
        LocalDateTime endTime = flightAdvancedSearchDTO.getEndDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getEndDate(),LocalTime.of(23,59,59)) : null;
        foundFlights = flightService.advancedSearch(origin,destination,airline ,startTime , endTime);


        List<FlightDTO> flightDTOs = FlightMapper.flightListToFlightDTOList(foundFlights);

        model.addAttribute("foundFlights" , flightDTOs);

        return "Airline/viewFlights";

    }

    @GetMapping("/dashboard/search-ticket")
    public String searchTicketForm(Model model){

        model.addAttribute("cities",cityService.getAllCityNames());

        model.addAttribute("flight", new FlightAdvancedSearchDTO());

        return "Airline/SearchAirlineTickets";

    }

    @PostMapping("/dashboard/search-ticket")
    public String viewAirlineFlights(Model model, @ModelAttribute FlightAdvancedSearchDTO flightAdvancedSearchDTO, HttpServletRequest request){

        List<Flight> foundFlights;

        City origin = !flightAdvancedSearchDTO.getOrigin().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getOrigin()) : null;
        City destination = !flightAdvancedSearchDTO.getDestination().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getDestination()) : null;
        LocalDateTime startTime = flightAdvancedSearchDTO.getStartDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getStartDate(),LocalTime.of(0,0,0)) : null;
        LocalDateTime endTime = flightAdvancedSearchDTO.getEndDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getEndDate(),LocalTime.of(23,59,59)) : null;
        
        foundFlights = flightService.advancedSearch(origin,destination,getLoggedInAirline(request) ,startTime , endTime);

        List<FlightDTO> flightDTOs = FlightMapper.flightListToFlightDTOList(foundFlights);

        model.addAttribute("foundFlights" , flightDTOs);

        return "Airline/SelectFlight";

    }

    @GetMapping("/dashboard/show-tickets")
    public String showTickets(Model model,@ModelAttribute("selectedFlight") Long flightId){

        List<Ticket> tickets = ticketService.findByFlight(flightService.findById(flightId));

        if (!tickets.isEmpty()){

            List<TicketDTO> ticketDTOs = TicketMapper.TicketListToTicketDTOList(tickets);

            model.addAttribute("tickets" , ticketDTOs);

            return "/Airline/ShowTickets";

        }else
            return "redirect:/message/no-result";
    }

    @PostMapping("/dashboard/show-tickets")
    public String refundTickets(Model model,  @ModelAttribute("selectedTicket") Long ticketId){

        Ticket refundTicket = ticketService.findById(ticketId);
        ticketService.refundTicket(refundTicket);
        return "redirect:search-ticket";


    }

    @GetMapping("/dashboard/cancel-flights")
    public String searchMyFlights(Model model){

        model.addAttribute("cities",cityService.getAllCityNames());

        model.addAttribute("flight", new FlightAdvancedSearchDTO());

        return "Airline/SearchMyFlights";
    }

    @PostMapping("/dashboard/cancel-flights")
    public String searchMyFlightsPost(Model model ,@ModelAttribute FlightAdvancedSearchDTO flightAdvancedSearchDTO, HttpServletRequest request){

        List<Flight> foundFlights;

        City origin = !flightAdvancedSearchDTO.getOrigin().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getOrigin()) : null;
        City destination = !flightAdvancedSearchDTO.getDestination().isEmpty() ? cityService.findByName(flightAdvancedSearchDTO.getDestination()) : null;
        Airline airline = getLoggedInAirline(request);
        LocalDateTime startTime = flightAdvancedSearchDTO.getStartDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getStartDate(),LocalTime.of(0,0,0)) : null;
        LocalDateTime endTime = flightAdvancedSearchDTO.getEndDate() != null ? LocalDateTime.of(flightAdvancedSearchDTO.getEndDate(),LocalTime.of(23,59,59)) : null;
        foundFlights = flightService.advancedSearch(origin,destination,airline ,startTime , endTime);

        List<FlightDTO> flightDTOs = FlightMapper.flightListToFlightDTOList(foundFlights);

        model.addAttribute("foundFlights" , flightDTOs);

        return "Airline/CancelFlights";

    }

    @GetMapping("/dashboard/cancel-flight")
    public String cancelFlight(Model model,@ModelAttribute("selectedFlight") Long flightId){

        Flight flight = flightService.findById(flightId);

        List<Ticket> tickets = ticketService.findByFlight(flight);

        if (!tickets.isEmpty()){
            for(Ticket refundTicket : tickets)
                ticketService.refundTicket(refundTicket);
        }

        flightService.permDelete(flight);

        return "redirect:cancel-flights";
    }


    private Airline getLoggedInAirline(HttpServletRequest httpRequest){
        
        Cookie[] cookies = httpRequest.getCookies();

        Airline loggedInAirline = null;

        if(cookies != null){
            for (Cookie c : cookies){
                if (c.getName().equals("AirlineId"))
                    loggedInAirline = airlineService.findById(Long.parseLong(c.getValue()));
            }
        }
        
        return loggedInAirline;
    }

}
