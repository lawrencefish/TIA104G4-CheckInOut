package com.roomInventory.model;

import com.hotel.model.HotelVO;
import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomInventoryService {
	@Autowired
	RoomInventoryRepository roomInventoryRepository;
	@Autowired
	private RoomTypeService roomTypeService;

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

	// 從旅館找房
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

	// 從日期房型Id找房（確認庫存）
	public List<HotelRoomInventoryDTO> findRoomsFromDateAndRoomTypeId(LocalDate startDate, LocalDate endDate,
			Integer roomTypeId) {
		return roomInventoryRepository.findRoomsFromDateAndRoomTypeId(startDate, endDate, roomTypeId);
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

	@Transactional
	public void updateRoomInventoryForHotel(LocalDate endDate, HttpSession session) {
		// 1. 從 Session 獲取當前酒店資訊
		HotelVO currentHotel = (HotelVO) session.getAttribute("hotel");
		if (currentHotel == null) {
			throw new RuntimeException("無法從 Session 中獲取酒店資訊，請重新登入！");
		}
		Integer hotelId = currentHotel.getHotelId();
//		System.out.println("當前操作酒店ID: " + hotelId);

		// 2. 查詢該酒店的所有房型
		List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotelId);
		if (roomTypes.isEmpty()) {
//			System.out.println("該酒店沒有可用的房型，任務結束！");
			return;
		}
//		System.out.println("獲取房型成功，房型數量: " + roomTypes.size());

		// 3. 過濾房型，只處理 status=1 的房型
		List<RoomTypeVO> activeRoomTypes = roomTypes.stream()
				.filter(roomType -> roomType.getStatus() == 1)
				.collect(Collectors.toList());
		if (activeRoomTypes.isEmpty()) {
//			System.out.println("該酒店的房型尚未審核通過，任務結束！");
			return;
		}
//		System.out.println("有效房型數量: " + activeRoomTypes.size());

		// 4. 確定日期範圍（從今天到指定日期）
		LocalDate today = LocalDate.now();
		if (endDate.isBefore(today)) {
			throw new RuntimeException("指定日期不能早於今天！");
		}
//		System.out.println("日期範圍: 從 " + today + " 到 " + endDate);

		// 5. 查詢日期範圍內的現有庫存
		List<RoomInventoryVO> existingInventories = roomInventoryRepository.findByDateRangeAndHotel(today, endDate, hotelId);
		Set<String> existingKeys = existingInventories.stream()
				.map(inventory -> inventory.getDate() + "-" + inventory.getRoomType().getRoomTypeId())
				.collect(Collectors.toSet());
//		System.out.println("現有庫存數量: " + existingKeys.size());

		// 6. 新增缺少的庫存
		for (RoomTypeVO roomType : activeRoomTypes) {
			for (LocalDate date = today; !date.isAfter(endDate); date = date.plusDays(1)) {
				String key = date + "-" + roomType.getRoomTypeId();
				if (!existingKeys.contains(key)) {
					// 如果庫存不存在，新增資料
					RoomInventoryVO newInventory = new RoomInventoryVO();
					newInventory.setRoomType(roomType);
					newInventory.setDate(date);
					newInventory.setAvailableQuantity(roomType.getRoomNum()); // 默認庫存數量為房型的房間數
					roomInventoryRepository.save(newInventory);

//					System.out.println("新增庫存: 日期=" + date + ", 房型ID=" + roomType.getRoomTypeId() + ", 房型=" + roomType.getRoomName() + ", 庫存數量=" + roomType.getRoomNum());
				}
			}
		}

//		System.out.println("酒店 " + hotelId + " 的庫存新增完成！");
	}

	// 查詢指定日期範圍內的庫存
	public List<RoomInventoryVO> findByDateRangeAndHotel(LocalDate startDate, LocalDate endDate, Integer hotelId) {
		return roomInventoryRepository.findByDateRangeAndHotel(startDate, endDate, hotelId);
	}
}
