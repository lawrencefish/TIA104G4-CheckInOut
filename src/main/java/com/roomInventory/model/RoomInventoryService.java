package com.roomInventory.model;

import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	public List<RoomInventoryVO> findByRoomTypesAndDateRange(List<RoomTypeVO> roomTypes, LocalDate startDate,
			LocalDate endDate) {
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

	// 尋找特定地點房間
	public List<RoomInventoryDTO> findAvailableRooms(LocalDate startDate, LocalDate endDate, double latitudeCenter,
			double longitudeCenter, double radius) {
		return roomInventoryRepository.findAvailableRooms(startDate, endDate, latitudeCenter, longitudeCenter, radius);
	}
	//從旅館找房
	public List<HotelRoomInventoryDTO> findAvailableRoomsFromHotel(Integer hotelId) {
		return roomInventoryRepository.findAvailableRoomsFromHotel(hotelId);
	}

	// 從ID搜尋庫存
	public RoomInventoryVO findByRoomTypeIdAndDate(Integer roomTypeId, LocalDate date) {
		return roomInventoryRepository.findByRoomTypeIdAndDate(roomTypeId, date);
	}
	public RoomInventoryVO findByRoomTypeId(Integer roomTypeId) {
		return roomInventoryRepository.findByRoomTypeRoomTypeId(roomTypeId);
	}

	// 取得每日庫存量
	public List<Map<String, Object>> getRoomCountsByDate() {
	    List<Object[]> results = roomInventoryRepository.countRoomsByDate();
	    List<Map<String, Object>> response = new ArrayList<>();

	    results.forEach(r -> {
	        Map<String, Object> result = new HashMap<>(); // 每次建立新的 Map
	        result.put("date", r[0].toString());
	        result.put("count", String.valueOf(r[1]));
	        response.add(result); 
	    });

	    return response;
	}

}
