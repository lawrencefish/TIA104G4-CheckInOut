package com.hotelFacility.controller;

import com.facility.model.FacilityRepository;
import com.facility.model.FacilityVO;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.hotelFacility.model.HotelFacilityRepository;
import com.hotelFacility.model.HotelFacilityService;
import com.hotelFacility.model.HotelFacilityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


import com.hotelFacility.model.HotelFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/hotelFacilities")
public class HotelFacilityController {

    @Autowired
    private HotelFacilityService hotelFacilityService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelFacilityRepository hotelFacilityRepository;
    @Autowired
    private FacilityRepository facilityRepository;

    /**
     * 查詢飯店已關聯的設施與服務
     *
     * @param hotelId 飯店 ID
     * @return 設施與服務 ID 列表
     */
    @GetMapping("/current")
    public List<Integer> getFacilitiesByHotelId(@RequestParam Integer hotelId) {
        return hotelFacilityService.findFacilityIdsByHotelId(hotelId);
    }

    @PostMapping("/update-facilities")
    public ResponseEntity<Map<String, String>> updateFacilities(
            @RequestBody Map<String, List<Integer>> payload, HttpSession session) {
        try {
            // 從會話中獲取 HotelVO
            HotelVO hotel = (HotelVO) session.getAttribute("hotel");

            // 提取設施和服務 ID 列表
            List<Integer> facilityIds = payload.getOrDefault("facilities", Collections.emptyList());
            List<Integer> serviceIds = payload.getOrDefault("services", Collections.emptyList());

            // 調用服務層更新設施
            hotelService.updateFacilities(hotel, facilityIds, serviceIds);

            // 返回成功響應
            Map<String, String> response = new HashMap<>();
            response.put("message", "設施與服務更新成功！");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 返回失敗響應
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "更新失敗：" + e.getMessage()));
        }
    }
}