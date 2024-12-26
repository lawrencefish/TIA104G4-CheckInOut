package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/business")
public class BusinessController {
    @GetMapping("")
    public String showHotel() {
        return "redirect:/business/hotelInfo";
    }

    @GetMapping("/hotelInfo")
    public String showHotelInfo() {
        return "business/hotelInfo";
    }

    @GetMapping("/hotelIntroduce")
    public String showHotelIntroduce() {
        return "business/hotelIntroduce";
    }

    @GetMapping("/roomTypeSet")
    public String showRoomTypeSet() {
        return "business/roomTypeSet";
    }

    @GetMapping("/roomManagement")
    public String showRoomManagement() {
        return "business/roomManagement";
    }

    @GetMapping("/priceSet")
    public String showPriceSet() {
        return "business/priceSet";
    }
}
