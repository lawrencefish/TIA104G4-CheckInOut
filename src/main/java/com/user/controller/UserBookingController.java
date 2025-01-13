package com.user.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.googleAPI.GeocodingService;
import com.price.model.PriceRepository;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.RoomInventoryDTO;
import com.roomInventory.model.RoomInventoryRepository;
import com.roomInventory.model.RoomInventoryService;
import com.roomInventory.model.RoomInventoryVO;

@RestController
@RequestMapping("/booking/api")
public class UserBookingController {

	@Autowired
	RoomInventoryRepository RIrepository;
	@Autowired
	RoomInventoryService RIservice;
	@Autowired
	PriceRepository Prepository;
	@Autowired
	PriceService Pservice;
	@Autowired
	GeocodingService gService;

	@PostMapping("/all_inventory")
    public List<Map<String, Object>> getRoomCountsByDate() {
        return RIservice.getRoomCountsByDate();
    }
	
	@PostMapping("/search")
	public Map<String, Object> search(@RequestBody Map<String, String> searchCondition, HttpSession session) {
		Integer guestNum = Integer.valueOf(searchCondition.get("guestNum"));
		Integer roomNum = Integer.valueOf(searchCondition.get("roomNum"));
		LocalDate checkInDate = LocalDate.parse(searchCondition.get("checkInDate"));
		LocalDate checkOutDateMOne = LocalDate.parse(searchCondition.get("checkOutDate")).minusDays(1);
		String place = searchCondition.get("place");
		Double lat = 0.0;
		Double lnt = 0.0;
		if (place.trim().isEmpty() || place == null) {
			lat = Double.valueOf(searchCondition.get("lat"));
			lnt = Double.valueOf(searchCondition.get("lnt"));
		} else {
			Double[] latLnt = gService.getCoordinatesFromPlace(place);
			lat = latLnt[0];
			lnt = latLnt[1];
		}

		Map<String, Object> resList = new LinkedHashMap<>(); // 最終返回的結果
		resList.put("guestNum", guestNum);
		resList.put("roomNum", roomNum);
		resList.put("checkInDate", checkInDate);
		resList.put("checkOutDate", searchCondition.get("checkOutDate"));

		List<RoomInventoryDTO> result = RIservice.findAvailableRooms(checkInDate, checkOutDateMOne, lat, lnt, 0.3);
		
		try {
			for (RoomInventoryDTO r : result) {
				boolean isRoomAvailableEveryDay = true;

				Map<LocalDate, Integer> dailyAvailableRooms = new HashMap<>();
				for (LocalDate date = checkInDate; !date.isAfter(checkOutDateMOne); date = date.plusDays(1)) {
					RoomInventoryVO ri = RIservice.findByRoomTypeIdAndDate(r.getRoomTypeId(), date);
					Integer needRooms = (guestNum + r.getMaxPerson() - 1) / r.getMaxPerson();
					System.out.println(date + "is checking ,RoomID:" + r.getRoomTypeId());
					System.out.println(
							"Max Cap:" + r.getMaxPerson() + "Need Room:" + needRooms + ", want room:" + roomNum);
					if (ri == null || ri.getAvailableQuantity() == null || ri.getAvailableQuantity() < needRooms
							|| needRooms > roomNum) {
						isRoomAvailableEveryDay = false;
						break;
					}

					dailyAvailableRooms.put(date, ri.getAvailableQuantity());
				}

				if (!isRoomAvailableEveryDay) {
					System.out.println(
							"RoomTypeId " + r.getRoomTypeId() + " is not available for all dates. Skipping...");
					continue; // 若某天房間不足，直接跳過這個房型
				}

				// 整合符合條件的房型資料
				String hotelKey = "hotel" + r.getHotelId();
				Map<String, Object> hotelList;
				if (!resList.containsKey(hotelKey)) {
					hotelList = new LinkedHashMap<>();
					hotelList.put("hotelID", r.getHotelId());
					hotelList.put("hotel", r.getName());
					hotelList.put("city", r.getCity());
					hotelList.put("district", r.getDistrict());
					hotelList.put("address", r.getAddress());
					hotelList.put("lat", r.getLatitude());
					hotelList.put("longitude", r.getLongitude());
					resList.put(hotelKey, hotelList);
				} else {
					hotelList = (Map<String, Object>) resList.get(hotelKey);
				}

				Map<String, Object> roomDetails = new LinkedHashMap<>();
				roomDetails.put("roomTypeID", r.getRoomTypeId());
				roomDetails.put("roomType", r.getRoomName());
				roomDetails.put("maxPerson", r.getMaxPerson());
				roomDetails.put("breakfast", r.getBreakfast());

				// 添加每日價格和日期資料
				List<Map<String, Object>> availableDates = new ArrayList<>();
				for (LocalDate date = checkInDate; !date.isAfter(checkOutDateMOne); date = date.plusDays(1)) {
					PriceVO dailyPrice = Pservice.getPriceOfDay(r.getRoomTypeId(), date);

					Map<String, Object> dailyDetails = new LinkedHashMap<>();
					dailyDetails.put("date", date.toString());
					dailyDetails.put("roomNumAvalible", dailyAvailableRooms.get(date));
					dailyDetails.put("priceType", dailyPrice.getPriceType());
					dailyDetails.put("price", dailyPrice.getPrice());
					dailyDetails.put("breakfastPrice", dailyPrice.getBreakfastPrice());

					availableDates.add(dailyDetails);
				}

				roomDetails.put("dates", availableDates);
				hotelList.put("ROOM" + r.getRoomTypeId(), roomDetails);
			}
		} catch (Exception e) {
			resList.put("message", e);			
		}
		return resList;
	}
}
