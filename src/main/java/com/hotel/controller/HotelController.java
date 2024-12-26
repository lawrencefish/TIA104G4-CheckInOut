package com.hotel.controller;

import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/byCity")
    public String getHotelByCity(@RequestParam String city, Model model) {
        List<HotelVO> hotels = hotelService.findByCity(city);
        model.addAttribute("hotels", hotels);
        return "hotelList";  // Thymeleaf頁面，顯示結果
    }

    @PostMapping("/save")
    public String saveHotel(@ModelAttribute HotelVO hotel, Model model) {
        hotelService.saveHotel(hotel);
        // ...
        return "redirect:/hotel/all";
    }
}
