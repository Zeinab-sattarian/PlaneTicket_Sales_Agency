package com.shop.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/message")
public class MessageController {

    @GetMapping("/failed-login")
    public String failedLogin(Model model){

        String message = "FAILED TO LOGIN";

        model.addAttribute("message",message);

        return "Message";

    }

    @GetMapping("/failed-signup")
    public String failedSignup(Model model){

        String message = "FAILED TO SIGNUP";

        model.addAttribute("message",message);

        return "Message";

    }

    @GetMapping("/no-result")
    public String noResult(Model model){

        String message = "NO RESULT FOUND";

        model.addAttribute("message",message);

        return "Message";

    }

    @GetMapping("/flight-not-created")
    public String failedToCreateFlight(Model model){

        String message = "FAILED TO CREATE FLIGHT";

        model.addAttribute("message",message);

        return "Message";

    }

    @GetMapping("/successful-booking")
    public String successfulBooking(Model model){

        String message = "TICKET WAS RESERVED SUCCESSFULLY";

        model.addAttribute("message",message);

        return "Message";

    }

    @GetMapping("/unsuccessful-booking")
    public String unsuccessfulBooking(Model model){

        String message = "UNABLE TO RESERVE TICKET SUCCESSFULLY";

        model.addAttribute("message",message);

        return "Message";

    }

}
