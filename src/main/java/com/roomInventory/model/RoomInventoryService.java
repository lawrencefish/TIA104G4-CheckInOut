package com.roomInventory.model;

import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomInventoryService {
    @Autowired
    RoomInventoryRepository roomInventoryRepository;
    @Autowired
    RoomTypeRepository roomTypeRepository;

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

    public RoomInventoryVO findById(Integer inventoryId) {
        return roomInventoryRepository.findById(inventoryId).orElse(null);
    }

    public void update(RoomInventoryVO roomInventory) {
        roomInventoryRepository.save(roomInventory);
    }

    @Transactional
    public RoomInventoryVO updateRoomInventory(RoomInventoryVO inventory) {
        RoomInventoryVO existingInventory = roomInventoryRepository.findById(inventory.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("房型庫存ID=" + inventory.getInventoryId() + " 不存在"));

        // 更新刪減數量
        existingInventory.setDeleteQuantity(inventory.getDeleteQuantity());

        // 更新可用數量
        int remainingQuantity = existingInventory.getRoomType().getRoomNum() - inventory.getDeleteQuantity();
        existingInventory.setAvailableQuantity(Math.max(0, remainingQuantity));

        // 保存更改
        return roomInventoryRepository.save(existingInventory);
    }
    //by YuCheng
	public List<RoomInventoryDTO> findAvailableRooms(LocalDate startDate, LocalDate endDate,
			double latitudeCenter, double longitudeCenter,double radius) {
		return roomInventoryRepository.findAvailableRooms(startDate, endDate, latitudeCenter,
				longitudeCenter,radius);
	}

}
