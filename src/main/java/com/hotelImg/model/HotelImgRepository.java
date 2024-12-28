package com.hotelImg.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelImgRepository extends JpaRepository<HotelImgVO, Integer> {
    List<HotelImgVO> findByHotel_HotelId(Integer hotelId);
}
