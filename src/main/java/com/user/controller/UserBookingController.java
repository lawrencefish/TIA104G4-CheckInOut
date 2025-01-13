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

import com.price.model.PriceRepository;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.RoomInventoryDTO;
import com.roomInventory.model.RoomInventoryRepository;
import com.roomInventory.model.RoomInventoryService;

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

	@PostMapping("/search")
	public Map<String, Object> search(@RequestBody Map<String, String> searchCondition, HttpSession session) {
	    // 解析輸入參數
	    Integer guestNum = Integer.valueOf(searchCondition.get("guestNum"));
	    Integer roomNum = Integer.valueOf(searchCondition.get("roomNum"));
	    LocalDate checkInDate = LocalDate.parse(searchCondition.get("checkInDate"));
	    LocalDate checkOutDate = LocalDate.parse(searchCondition.get("checkOutDate")).minusDays(1);
	    Double lat = Double.valueOf(searchCondition.get("lat"));
	    Double lnt = Double.valueOf(searchCondition.get("lnt"));

	    if (roomNum == 0 || guestNum == 0) {
	        throw new IllegalArgumentException("Guest number and room number must be greater than zero");
	    }

	    Map<String, Object> resList = new LinkedHashMap<>(); // 最終返回的結果

	    // 取得可用的房間資料
	    List<RoomInventoryDTO> result = RIservice.findAvailableRooms(checkInDate, checkOutDate, lat, lnt, 0.3);

	    for (RoomInventoryDTO r : result) {
	        // 計算所需房間數量
	        Integer neededRooms = (guestNum + r.getMaxPerson() - 1) / r.getMaxPerson(); // 向上取整

	        if (neededRooms > r.getAvailableQuantity() || neededRooms != roomNum) {
	            continue;
	        }

	        // 初始化旅館資訊
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

	        // 初始化房型資訊
	        Map<String, Object> roomDetails = new LinkedHashMap<>();
	        roomDetails.put("roomTypeID", r.getRoomTypeId());
	        roomDetails.put("roomType", r.getRoomName());
	        roomDetails.put("maxPerson", r.getMaxPerson());
	        roomDetails.put("breakfast", r.getBreakfast());

	        // 添加每日價格和日期資料
	        List<Map<String, Object>> availableDates = new ArrayList<>();
	        for (LocalDate date = checkInDate; !date.isAfter(checkOutDate); date = date.plusDays(1)) {
	            PriceVO dailyPrice = Pservice.getPriceOfDay(r.getRoomTypeId(), date);

	            Map<String, Object> dailyDetails = new LinkedHashMap<>();
	            dailyDetails.put("date", date.toString());
	            dailyDetails.put("roomNum", neededRooms);
	            dailyDetails.put("priceType", dailyPrice.getPriceType());
	            dailyDetails.put("price", dailyPrice.getPrice());
	            dailyDetails.put("breakfastPrice", dailyPrice.getBreakfastPrice());

	            availableDates.add(dailyDetails);
	        }

	        roomDetails.put("dates", availableDates);
	        hotelList.put("ROOM" + r.getRoomTypeId(), roomDetails);
	    }
	    return resList;
	}

}
