package com.user.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
 
	@PostMapping("/search")
	public Map<String, Object> search(@RequestBody Map<String, String> searchConditon, HttpSession session){
		Integer guestNum = Integer.valueOf(searchConditon.get("guestNum"));
		Integer roomNum = Integer.valueOf(searchConditon.get("guestNum"));
		LocalDate checkInDate = LocalDate.parse(searchConditon.get("checkInDate"));
		LocalDate checkOutDate = LocalDate.parse(searchConditon.get("checkInDate"));
		Double lat = Double.valueOf(searchConditon.get("lat"));
		Double lnt = Double.valueOf(searchConditon.get("lnt"));
		Map<String, Object> resList = new HashMap<>();
		
		List<RoomInventoryDTO> result = RIservice.findRoomInventoryByTypeHotelAndPrice(checkInDate,checkOutDate,lat,lnt);
		
		result.forEach(r ->{
			String sameHotel = "";
			Integer avgGusetPerRoom = guestNum/roomNum;
			Integer needRoom = roomNum;
			Map<String, String> roomList = new HashMap<>();
			
			//放入回傳列
			if (!r.getName().equals(sameHotel) && !sameHotel.equals("")) {
				roomList.put("hotelID", String.valueOf(r.getHotelId()));
				resList.put("hotel", sameHotel);
				resList.put("city",r.getCity());
				resList.put("district",r.getDistrict());
				resList.put("address",r.getAddress());
				resList.put("lat",r.getLatitude());
				resList.put("address",r.getLongitude());
				resList.put("result",roomList);
			}
				sameHotel = r.getName();
			
			if(avgGusetPerRoom >=r.getMaxPerson()) {
				needRoom +=1;
			}
			
			if(needRoom < r.getAvailableQuantity()) {
				roomList.put("roomTypeID", String.valueOf(r.getRoomTypeId()));
				roomList.put("roomType",r.getRoomName());
				roomList.put("maxPerson",String.valueOf(r.getMaxPerson()));
				roomList.put("breakfast",String.valueOf(r.getBreakfast()));
				roomList.put("date",String.valueOf(r.getDate()));
				roomList.put("address",r.getAddress());
				roomList.put("address",r.getAddress());
				roomList.put("roomNum",String.valueOf(needRoom));
				
			}
			
		});
		
		return resList;
		
	}

}
