package com.roomType.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public List<RoomTypeVO> findByHotelId(Integer hotelId) {
        return roomTypeRepository.findByHotel_HotelId(hotelId);
    }

    public void updateRoomType(Integer roomTypeId, RoomTypeVO roomTypeVO) {
        Optional<RoomTypeVO> existingRoomType = roomTypeRepository.findById(roomTypeId);
        if (existingRoomType.isPresent()) {
            RoomTypeVO roomType = existingRoomType.get();
            roomType.setRoomName(roomTypeVO.getRoomName());
            roomType.setMaxPerson(roomTypeVO.getMaxPerson());
            roomType.setRoomNum(roomTypeVO.getRoomNum());
            roomType.setBreakfast(roomTypeVO.getBreakfast());
            roomTypeRepository.save(roomType);
        } else {
            throw new IllegalArgumentException("找不到指定的房型 ID：" + roomTypeId);
        }
    }
}