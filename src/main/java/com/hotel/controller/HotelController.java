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

    @GetMapping("/introduce/{hotelId}")
    public String showHotelIntroduce(@PathVariable Integer hotelId, Model model) {
        // 使用服務層獲取酒店及其圖片數據
        HotelVO hotelWithImages = hotelService.getHotelWithImages(hotelId);
        model.addAttribute("hotel", hotelWithImages);
        return "business/hotelIntroduce"; // Thymeleaf模板名稱
    }
}
