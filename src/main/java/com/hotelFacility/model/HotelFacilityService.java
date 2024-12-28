package com.hotelFacility.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelFacilityService {

    @Autowired
    private HotelFacilityRepository hotelFacilityRepository;

    /**
     * 根據飯店 ID 查詢已關聯的設施 ID 列表
     *
     * @param hotelId 飯店 ID
     * @return 設施 ID 列表
     */
    public List<Integer> findFacilityIdsByHotelId(Integer hotelId) {
        return hotelFacilityRepository.findFacilityIdsByHotelId(hotelId);
    }
}