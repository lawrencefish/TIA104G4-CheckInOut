package com.roomType.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public List<RoomTypeVO> findByHotelId(Integer hotelId) {
        return roomTypeRepository.findByHotel_HotelId(hotelId);
    }
}