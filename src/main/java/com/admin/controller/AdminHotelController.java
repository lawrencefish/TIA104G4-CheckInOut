package com.admin.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.HotelVO;
import com.hotel.model.HotelService;
import com.admin.model.*;

@RestController
@RequestMapping("/hotel")
public class AdminHotelController {

	@Autowired
    private AdminHotelService adminHotelService;

    /**
     * 獲取所有飯店資料 (JSON 格式)
     */
    @GetMapping("/getHotelsData")
    public ResponseEntity<List<HotelVO>> getHotelsData() {
        List<HotelVO> hotels = (List<HotelVO>) adminHotelService.findAll(); // 獲取所有飯店資料

        // 如果需要額外字段處理
//        for (HotelVO hotel : hotels) {
//            hotel.setPendingRoomCount(hotelService.getPendingRoomCount(hotel.getHotelId())); // 假設有此方法
//        }

        return ResponseEntity.ok(hotels); // 返回 JSON 格式的數據
    }
    
    @GetMapping("/getHotelData/{id}")
    public ResponseEntity<Optional<HotelVO>> getHotelData(@PathVariable Integer id) {
        Optional<HotelVO> hotel = adminHotelService.findById(id);
        return ResponseEntity.ok(hotel);
    }
    
 // 獲取待審核的飯店列表
    @GetMapping("/getPendingReviews")
    public ResponseEntity<List<HotelVO>> getPendingReviews() {
        List<HotelVO> pendingHotels = adminHotelService.findByStatus((Integer)0);
        return ResponseEntity.ok(pendingHotels);
    }

    // 獲取特定狀態的飯店
    @GetMapping("/getHotelsByStatus/{status}")
    public ResponseEntity<List<HotelVO>> getHotelsByStatus(@PathVariable Integer status) {
        List<HotelVO> hotels = adminHotelService.findByStatus(status);
        return ResponseEntity.ok(hotels);
    }

    // 更新飯店審核狀態
    @PostMapping("/updateReviewStatus/{hotelId}")
    public ResponseEntity<?> updateReviewStatus(
            @PathVariable Integer hotelId,
            @RequestParam Integer status) {
        adminHotelService.updateStatus(hotelId, status);
        return ResponseEntity.ok().build();
    }
}
	

