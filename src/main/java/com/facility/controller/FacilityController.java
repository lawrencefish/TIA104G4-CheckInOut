package com.facility.controller;

import com.facility.model.FacilityService;
import com.facility.model.FacilityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    // 回傳 facilityType = 1 的設施
    @GetMapping("/hotel/facilities")
    public ResponseEntity<List<FacilityVO>> getHotelFacilities() {
        List<FacilityVO> facilities = facilityService.getHotelFacilities((Integer) 1, (Integer) 0);
        return ResponseEntity.ok(facilities);
    }

    // 回傳 facilityType = 1 的服務
    @GetMapping("/hotel/services")
    public ResponseEntity<List<FacilityVO>> getHotelServices() {
        List<FacilityVO> services = facilityService.getHotelFacilities((Integer) 1, (Integer) 1);
        return ResponseEntity.ok(services);
    }
}