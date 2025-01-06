package com.roomInventory.model;

import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomInventoryService {
    @Autowired
    RoomInventoryRepository roomInventoryRepository;

    public List<RoomInventoryVO> findByRoomTypes(List<RoomTypeVO> roomTypes) {
        List<Integer> roomTypeIds = roomTypes.stream().map(RoomTypeVO::getRoomTypeId).collect(Collectors.toList());
        return roomInventoryRepository.findByRoomTypeIds(roomTypeIds);
    }

    public List<RoomInventoryVO> findByRoomTypesAndDateRange(List<RoomTypeVO> roomTypes, LocalDate startDate, LocalDate endDate) {
        return roomInventoryRepository.findByRoomTypesAndDateRange(roomTypes, startDate, endDate);
    }

    public List<RoomInventoryVO> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return roomInventoryRepository.findByDateRange(startDate, endDate);
    }

    public RoomInventoryVO save(RoomInventoryVO roomInventory) {
        return roomInventoryRepository.save(roomInventory);
    }
}
