package com.shop.project.controller;


import com.shop.project.domain.Flight;
import com.shop.project.domain.Ticket;
import com.shop.project.domain.Mapper.FlightMapper;
import com.shop.project.domain.Mapper.TicketMapper;
import com.shop.project.domain.dto.FlightDTO;
import com.shop.project.domain.dto.TicketDTO;
import com.shop.project.domain.Passenger;
import com.shop.project.service.CityService;
import com.shop.project.service.FlightService;
import com.shop.project.service.MediaService;
import com.shop.project.service.PassengerService;
import com.shop.project.service.TicketService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @Autowired
    FlightService flightService;

    @Autowired
    TicketService ticketService;

    @Autowired
    CityService cityService;

    @Autowired
    MediaService mediaService;

    @GetMapping("/signup")
    public String signupForm(Model model){

        model.addAttribute("passenger", new Passenger());

        return "passenger/PassengerSignup";

    }

    @PostMapping("/signup")
    public String signupPassenger(Model model , @ModelAttribute("passenger") Passenger passenger, HttpServletResponse response, @RequestParam("profilePic") MultipartFile profilePic){

        if(!profilePic.isEmpty()){
            passenger.setProfilePicFileName(mediaService.savePassengerProfilePicture(profilePic));
        }

        Passenger newPassenger = passengerService.createPassenger(passenger.getUsername(), passenger.getPassword(), passenger.getFirstName(), passenger.getLastName(), passenger.getEmail(), passenger.getPhoneNumber(), passenger.getSsn(), passenger.getProfilePicFileName());

        if (newPassenger!=null){
        
            Cookie cookie = new Cookie("PassengerId" , newPassenger.getId().toString());

            cookie.setMaxAge(60*15);

            response.addCookie(cookie);

            return "redirect:login";
        }
        else
            return "redirect:/message/failed-signup";

    }

    @GetMapping("/login")
    public String loginForm(Model model, HttpServletRequest request){

        Passenger loggedInPassenger = getLoggedInPassenger(request);

        if (loggedInPassenger!=null){
            
            model.addAttribute("loggedInPassenger" , loggedInPassenger);

            if (loggedInPassenger.getProfilePicFileName()!=null) {
                model.addAttribute("profilePicPath", mediaService.resolveProfilePicAddress(loggedInPassenger.getProfilePicFileName()));
            }

            return "passenger/PassengerDashboard";

        }

        Passenger passenger = new Passenger();

        model.addAttribute("passenger",passenger);

        return "passenger/PassengerLogin";

    }

    @PostMapping("/login")
    public String loginAirline(Model model , @ModelAttribute("passenger") Passenger passenger, HttpServletResponse response){

        Passenger checkPassenger = passengerService.findByUsername(passenger.getUsername());

        if (checkPassenger==null)
            return "redirect:/message/failed-login";

        if (checkPassenger.getPassword().equals(passenger.getPassword())){
            
            Cookie cookie = new Cookie("PassengerId" , checkPassenger.getId().toString());

            cookie.setMaxAge(60*15);

            response.addCookie(cookie);

            model.addAttribute("loggedInPassenger" , checkPassenger);

            if (checkPassenger.getProfilePicFileName()!=null) {
                model.addAttribute("profilePicPath", mediaService.resolveProfilePicAddress(checkPassenger.getProfilePicFileName()));
            }

            return "passenger/PassengerDashboard";

        }

        else
            return "redirect:/message/failed-login";

    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response){

        Cookie cookie = new Cookie("PassengerId" , null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";

    }

    @GetMapping("/dashboard/buy-ticket")
    public String buyTicketForm(Model model){

        FlightDTO flightDTO = new FlightDTO();

        List<String> allCities = cityService.getAllCityNames();

        model.addAttribute("cities" , allCities);

        model.addAttribute("flightDTO", flightDTO);

        return "/passenger/BuyTicketForm";

    }

    @PostMapping("/dashboard/buy-ticket")
    public String selectTicket(Model model , @ModelAttribute("flightDTO") FlightDTO flightDTO){

        List<Flight> foundFlights = null;

        foundFlights = flightService.findByOriginAndDestinationAndTime(flightDTO.getOrigin(),flightDTO.getDestination(),flightDTO.getDepartureDate());

        List<FlightDTO> flightDTOs = FlightMapper.flightListToFlightDTOList(foundFlights);
        
        if (foundFlights!=null){

            model.addAttribute("todayDate",LocalDate.now());

            model.addAttribute("foundFlights" , flightDTOs);

            return "/passenger/SelectTicket";

        }else
            return "redirect:/message/no-result";


    }


    @PostMapping("/dashboard/submit-ticket")
    public String submitTicket(Model model , @ModelAttribute("selectedFlight") Long flightId, HttpServletRequest request ){

        Flight selectedFlight = null;

        if (flightId != null){

            selectedFlight = flightService.findById(flightId);

        }

        if (selectedFlight!=null) {

            if (ticketService.buyTicket(getLoggedInPassenger(request),selectedFlight)!=null)

                return "redirect:/passenger/login";

            else

                return "redirect:/message/unsuccessful-booking";


        }else
            return "redirect:/message/no-result";

    }

    @GetMapping("/dashboard/view-tickets")
    public String viewTickets(Model model, HttpServletRequest request){

        List<Ticket> tickets = ticketService.getByPassenger(getLoggedInPassenger(request));

        if (!tickets.isEmpty()){

            List<TicketDTO> ticketDTOs = TicketMapper.TicketListToTicketDTOList(tickets);

            model.addAttribute("todayDate",LocalDate.now());


            model.addAttribute("tickets" , ticketDTOs);

            return "/passenger/ViewTickets";

        }else
            return "redirect:/message/no-result";
    }

    @PostMapping("/dashboard/view-tickets")
    public String refundTickets(Model model,  @ModelAttribute("selectedTicket") Long ticketId){

        Ticket refundTicket = ticketService.findById(ticketId);
        ticketService.refundTicket(refundTicket);
        return "redirect:view-tickets";


    }

    private Passenger getLoggedInPassenger(HttpServletRequest httpRequest){
        
        Cookie[] cookies = httpRequest.getCookies();

        Passenger loggedInPassenger = null;

        if(cookies != null){
            for (Cookie c : cookies){
                if (c.getName().equals("PassengerId"))
                    loggedInPassenger = passengerService.findById(Long.parseLong(c.getValue()));
            }
        }
        
        return loggedInPassenger;
    }


}
