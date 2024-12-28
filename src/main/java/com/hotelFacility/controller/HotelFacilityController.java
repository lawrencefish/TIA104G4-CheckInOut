package com.hotelFacility.controller;

import com.hotelFacility.model.HotelFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import com.hotelFacility.model.HotelFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hotelFacilities")
public class HotelFacilityController {

    @Autowired
    private HotelFacilityService hotelFacilityService;

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
}