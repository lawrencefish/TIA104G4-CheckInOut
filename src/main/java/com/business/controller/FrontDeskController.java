package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/frontDesk")
public class FrontDeskController {

    @GetMapping("")
    public String showFrontDesk() {
        return "redirect:/frontDesk/checkIn";
    }

    @GetMapping("checkIn")
    public String showCheckIn() {
        return "business/checkIn";
    }

    @GetMapping("checkOut")
    public String showCheckOut() {
        return "business/checkOut";
    }

    @GetMapping("roomStatus")
    public String showroomStatus() {
        return "business/roomStatus";
    }
}
