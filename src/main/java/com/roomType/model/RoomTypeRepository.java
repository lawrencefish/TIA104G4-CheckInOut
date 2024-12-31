package com.roomType.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeVO, Integer> {
    List<RoomTypeVO> findByHotel_HotelId(Integer hotelId);
}