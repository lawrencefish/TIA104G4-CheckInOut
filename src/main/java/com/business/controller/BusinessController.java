package com.business.controller;

import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.hotelImg.model.HotelImgService;
import com.hotelImg.model.HotelImgVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    HotelService hotelService;
    @Autowired
    HotelImgService hotelImgService;
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("")
    public String showHotel() {
        return "redirect:/business/hotelInfo";
    }

    @GetMapping("/hotelInfo")
    public String showHotelInfo() {
        return "business/hotelInfo";
    }

    @PostMapping("/hotelInfo")
    public String updateHotelInfo(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("email") String email,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("address") String address,
            HttpServletRequest request,
            Model model
    ) {
        // 從 session 獲取現有的 hotel 物件
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");
        if (hotel == null) {
            // 若無資料，重導至登入或錯誤頁
            return "redirect:/login";
        }

        // 更新 hotel 資料
        hotel.setName(name);
        hotel.setPhoneNumber(phone);
        hotel.setEmail(email);
        hotel.setCity(city);
        hotel.setDistrict(district);
        hotel.setAddress(address);

        // 更新至資料庫（假設有 service 方法）
        hotelService.updateHotel(hotel);

        // 更新 session 中的 hotel 資料
        request.getSession().setAttribute("hotel", hotel);

        // 返回成功訊息並重導
        model.addAttribute("successMessage", "資料更新成功！");
        return "business/hotelInfo"; // 返回表單頁面
    }

    @GetMapping("/hotelIntroduce")
    public String showHotelIntroduce(HttpServletRequest request, Model model) {
        // 從 session 中取得 hotel
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");

        if (hotel != null) {
            // 基於 hotelId 查詢圖片列表
            Integer hotelId = hotel.getHotelId();
            model.addAttribute("hotelId", hotelId);
            List<Integer> imageIds = hotelImgService.getImagesByHotelId(hotelId)
                    .stream()
                    .map(HotelImgVO::getHotelImgId)
                    .collect(Collectors.toList());
            model.addAttribute("images", imageIds); // 傳遞圖片列表
        } else {
            // 如果 session 中沒有 hotel，設置空列表
            model.addAttribute("images", Collections.emptyList());
        }
        return "business/hotelIntroduce";
    }

    @GetMapping("/roomTypeSet")
    public String getRoomTypes(HttpServletRequest request, Model model) {
        // 從 session 中獲取 hotel
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");
        Integer hotelId = hotel.getHotelId();

        // 從服務層獲取指定飯店的房型列表
        List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotelId);
        

        // 檢查集合是否為空
        if (roomTypes == null || roomTypes.isEmpty()) {
            model.addAttribute("message", "目前尚未設定任何房型！");
        } else {
            model.addAttribute("roomTypes", roomTypes);
        }

        // 返回對應的 Thymeleaf 頁面
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
