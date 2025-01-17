package com.admin.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.HotelVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import com.hotel.model.HotelService;
import com.admin.model.*;

@RestController
@RequestMapping("/adminHotel")
public class AdminHotelController {

	@Autowired
    private AdminHotelService adminHotelService;
	
	@Autowired
    private RoomTypeService roomTypeService;

    /**
     * 獲取所有飯店資料 (JSON 格式)
     */
    @GetMapping("/findAllHotels")
    public ResponseEntity<List<HotelVO>> findAllHotels() {
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
    
    
    /* 獲取業者審核資料 */
    @GetMapping("/industry/review/{id}")
    public ResponseEntity<?> getIndustryReview(@PathVariable Integer id) {
        Optional<HotelVO> hotelOpt = adminHotelService.findById(id);
        
        if (hotelOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HotelVO hotel = hotelOpt.get();
        Map<String, Object> response = new HashMap<>();
        
        // 基本資訊
        response.put("hotelId", hotel.getHotelId());
        response.put("companyName", hotel.getName());
        response.put("taxId", hotel.getTaxId());
        response.put("city", hotel.getCity());
        response.put("district", hotel.getDistrict());
        response.put("address", hotel.getAddress());
        response.put("phoneNumber", hotel.getPhoneNumber());
        response.put("email", hotel.getEmail());
        response.put("owner", hotel.getOwner());
        response.put("status", hotel.getStatus());
        
        // 地理位置
        if (hotel.getLatitude() != null && hotel.getLongitude() != null) {
            response.put("latitude", hotel.getLatitude());
            response.put("longitude", hotel.getLongitude());
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 獲取業者的證件圖片
     */
    @GetMapping("/industry/documents/{id}/{type}")
    public ResponseEntity<byte[]> getDocument(
            @PathVariable Integer id,
            @PathVariable String type) {
        
        Optional<HotelVO> hotelOpt = adminHotelService.findById(id);
        if (hotelOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        HotelVO hotel = hotelOpt.get();
        byte[] document = null;

        switch (type) {
            case "idFront":
                document = hotel.getIdFront();
                break;
            case "idBack":
                document = hotel.getIdBack();
                break;
            case "license":
                document = hotel.getLicense();
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(document);
    }

    /**
     * 更新業者審核狀態
     */
    @PostMapping("/industry/review/{id}")
    public ResponseEntity<?> updateIndustryStatus(
            @PathVariable Integer id,
            @RequestParam Integer status,
            @RequestParam(required = false) String reason) {
        
        try {
            adminHotelService.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
	

