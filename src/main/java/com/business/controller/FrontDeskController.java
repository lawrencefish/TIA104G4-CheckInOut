package com.business.controller;

import com.hotel.model.HotelVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/frontDesk")
public class FrontDeskController {
    @Autowired
    private RoomTypeService roomTypeService;

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
    public String showroomStatus(Model model, HttpSession session) {
        // 假設已登入並綁定到飯店
        HotelVO hotel = (HotelVO) session.getAttribute("hotel");
        if (hotel == null) {
            return "redirect:/login"; // 如果未綁定飯店，重定向到登入頁面
        }

        List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotel.getHotelId());

        // 每個房型包含房間數量
        Map<String, Long> roomCounts = roomTypes.stream()
                .collect(Collectors.toMap(RoomTypeVO::getRoomName,
                        roomType -> roomType.getRooms().stream().count()));

        model.addAttribute("roomCounts", roomCounts);
        model.addAttribute("roomTypes", roomTypes);
        return "business/roomStatus";
    }
}
