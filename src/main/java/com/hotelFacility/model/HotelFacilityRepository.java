package com.hotelFacility.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelFacilityRepository extends JpaRepository<HotelFacilityVO, Integer> {

    /**
     * 查詢指定飯店的所有設施與服務 ID
     *
     * @param hotelId 飯店 ID
     * @return 設施與服務 ID 列表
     */
    @Query("SELECT hf.facility.facilityId FROM HotelFacilityVO hf WHERE hf.hotel.hotelId = :hotelId")
    List<Integer> findFacilityIdsByHotelId(Integer hotelId);
}