package com.user.controller;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.facility.model.FacilityVO;
import com.googleAPI.GeocodingService;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import com.hotelFacility.model.HotelFacilityService;
import com.hotelFacility.model.HotelFacilityVO;
import com.hotelImg.model.HotelImgService;
import com.hotelImg.model.HotelImgVO;
import com.order.model.OrderService;
import com.order.model.OrderVO;
import com.price.model.PriceRepository;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.HotelRoomInventoryDTO;
import com.roomInventory.model.RoomInventoryDTO;
import com.roomInventory.model.RoomInventoryRepository;
import com.roomInventory.model.RoomInventoryService;
import com.roomInventory.model.RoomInventoryVO;
import com.roomTypeFacility.model.RoomTypeFacilityService;
import com.roomTypeImg.model.RoomTypeImgService;
import com.roomTypeImg.model.RoomTypeImgVO;

@RestController
@RequestMapping("/search/api")
public class UserSearchController {

	@Autowired
	RoomInventoryService RIservice;
	@Autowired
	PriceService Pservice;
	@Autowired
	GeocodingService gService;
	@Autowired
	RoomTypeImgService roomTypeImgService;
	@Autowired
	HotelImgService hotelImgService;
	@Autowired
	HotelService hotelService;
	@Autowired
	OrderService orderService;
	@Autowired
	HotelFacilityService HFService;
	@Autowired
	RoomTypeFacilityService RTFService;

	@PostMapping("/all_inventory")
	public List<Map<String, Object>> getRoomCountsByDate() {
		return RIservice.getRoomCountsByDate();
	}

	@PostMapping("/search")
	public Map<String, String> search(@RequestBody Map<String, String> searchCondition, HttpServletRequest req,
			HttpSession session) {
		Integer guestNum = Integer.valueOf(searchCondition.get("guestNum"));
		Integer roomNum = Integer.valueOf(searchCondition.get("roomNum"));
		LocalDate checkInDate = LocalDate.parse(searchCondition.get("checkInDate"));
		LocalDate checkOutDate = LocalDate.parse(searchCondition.get("checkOutDate"));
		String place = searchCondition.get("place");
		Double lat = 0.0;
		Double lnt = 0.0;
		if (place == null || place.trim().isBlank()) {
			lat = Double.valueOf(searchCondition.get("lat"));
			lnt = Double.valueOf(searchCondition.get("lnt"));
		} else {
			Double[] latLnt = gService.getCoordinatesFromPlace(place);
			lat = latLnt[0];
			lnt = latLnt[1];
		}

		session.setAttribute("guestNum", guestNum);
		session.setAttribute("roomNum", roomNum);
		session.setAttribute("checkInDate", checkInDate);
		session.setAttribute("checkOutDate", checkOutDate);
		session.setAttribute("lat", lat);
		session.setAttribute("lnt", lnt);
		System.out.println("set OK");

		Map<String, String> response = new HashMap<String, String>();
		response.put("url", "/user/search");
		return response;
	}

	// 取得搜尋資訊
	@PostMapping("/map_search")
	public Map<String, String> mapSearch(@RequestBody Map<String, String> searchCondition, HttpSession session) {
		Double lat = Double.valueOf(searchCondition.get("lat"));
		Double lnt = Double.valueOf(searchCondition.get("lnt"));
		session.setAttribute("lat", lat);
		session.setAttribute("lnt", lnt);
		Map<String, String> response = new HashMap<String, String>();
		response.put("message", "成功");
		return response;

	}

	// 尋找房型
	@PostMapping("/search_result")
	public Map<String, Object> searchResult(HttpSession session) {
		// 1. 解析前端傳進來的參數
		Integer guestNum = (Integer) session.getAttribute("guestNum");
		Integer roomNum = (Integer) session.getAttribute("roomNum");
		LocalDate checkInDate = (LocalDate) session.getAttribute("checkInDate");
		LocalDate checkOutDate = (LocalDate) session.getAttribute("checkOutDate");
		LocalDate checkOutDateMOne = checkOutDate.minusDays(1);
		Double lat = (Double) session.getAttribute("lat");
		Double lnt = (Double) session.getAttribute("lnt");

		// 2. 準備最終回傳結構
		Map<String, Object> resList = new LinkedHashMap<>();
		resList.put("myLat", lat);
		resList.put("myLnt", lnt);
		resList.put("guestNum", guestNum);
		resList.put("roomNum", roomNum);
		resList.put("checkInDate", checkInDate);
		resList.put("checkOutDate", checkOutDate);

		// 新增用於存放所有飯店資訊的 ArrayList
		List<Map<String, Object>> hotels = new ArrayList<>();

		try {
			// 3. 一次撈取符合搜尋條件的所有房型
			List<RoomInventoryDTO> allRoomData = RIservice.findAvailableRooms(checkInDate, checkOutDateMOne, lat, lnt,
					0.05);

			// 4. 將資料分組
			Map<Integer, Map<Integer, List<RoomInventoryDTO>>> groupedData = new LinkedHashMap<>();
			for (RoomInventoryDTO dto : allRoomData) {
				Integer hotelId = dto.getHotelId();
				Integer roomTypeId = dto.getRoomTypeId();

				groupedData.computeIfAbsent(hotelId, k -> new LinkedHashMap<>())
						.computeIfAbsent(roomTypeId, k -> new ArrayList<>()).add(dto);
			}

			// 5. 依各飯店處理資料
			for (Map.Entry<Integer, Map<Integer, List<RoomInventoryDTO>>> hotelEntry : groupedData.entrySet()) {
				Integer hotelId = Integer.valueOf(hotelEntry.getKey());
				Map<Integer, List<RoomInventoryDTO>> roomMap = hotelEntry.getValue();

				// 準備飯店的資料結構
				Map<String, Object> hotelData = new LinkedHashMap<>();
				RoomInventoryDTO anyDtoInHotel = roomMap.entrySet().iterator().next().getValue().get(0);
				Double ratings = orderService.getAvgRatingAndCommentCounts(hotelId).getAvgRating();
				long comments = orderService.getAvgRatingAndCommentCounts(hotelId).getCommentCount();
				
				hotelData.put("hotelID", hotelId);
				hotelData.put("hotel", anyDtoInHotel.getName());
				hotelData.put("city", anyDtoInHotel.getCity());
				hotelData.put("district", anyDtoInHotel.getDistrict());
				hotelData.put("address", anyDtoInHotel.getAddress());
				hotelData.put("lat", anyDtoInHotel.getLatitude());
				hotelData.put("lng", anyDtoInHotel.getLongitude());
				hotelData.put("ratings", ratings);
				hotelData.put("comments", comments);

				// 處理飯店底下的各房型
				List<Map<String, Object>> rooms = new ArrayList<>();
				for (Map.Entry<Integer, List<RoomInventoryDTO>> roomTypeEntry : roomMap.entrySet()) {
					Integer roomTypeId = roomTypeEntry.getKey();
					List<RoomInventoryDTO> dailyList = roomTypeEntry.getValue();

					// 建立每日房型的可用資訊
					Map<LocalDate, RoomInventoryDTO> dateMap = new HashMap<>();
					for (RoomInventoryDTO d : dailyList) {
						dateMap.put(d.getDate(), d);
					}

					RoomInventoryDTO sample = dailyList.get(0);
					byte breakfast = sample.getBreakfast();
					int maxPerson = sample.getMaxPerson();
					int needRooms = (guestNum + maxPerson - 1) / maxPerson;
					
					if (needRooms > roomNum) {
						continue;
					}

					boolean isRoomAvailableEveryDay = true;
					Map<LocalDate, Integer> dailyAvailableRooms = new LinkedHashMap<>();
					LocalDate currentDate = checkInDate;
					while (!currentDate.isAfter(checkOutDateMOne)) {
						RoomInventoryDTO dayDto = dateMap.get(currentDate);
						if (dayDto == null || dayDto.getAvailableQuantity() < roomNum) {
							isRoomAvailableEveryDay = false;
							break;
						}
						dailyAvailableRooms.put(currentDate, dayDto.getAvailableQuantity());
						currentDate = currentDate.plusDays(1);
					}

					if (!isRoomAvailableEveryDay) {
						continue;
					}

					// 整理房型資訊
					Map<String, Object> roomDetails = new LinkedHashMap<>();
					
					roomDetails.put("roomTypeID", roomTypeId);
					roomDetails.put("maxPerson", maxPerson);

					// 整理每日資訊
					LocalDate dateForPrice = checkInDate;
					Integer priceSum = 0;
					while (!dateForPrice.isAfter(checkOutDateMOne)) {
						PriceVO dailyPrice = Pservice.getPriceOfDay(roomTypeId, dateForPrice);
						if (breakfast != 0 ) {
							priceSum += (dailyPrice.getBreakfastPrice()*guestNum);
						}
						priceSum += dailyPrice.getPrice()*roomNum;
						dateForPrice = dateForPrice.plusDays(1);
					}
					roomDetails.put("total_price", priceSum);
					rooms.add(roomDetails);
				}
				if (!rooms.isEmpty()) {
				hotelData.put("rooms", rooms);
				hotels.add(hotelData);
				}
			}

			// 將飯店清單加入回傳結構
			resList.put("hotels", hotels);

		} catch (Exception e) {
			resList.put("error", e.getMessage());
			resList.put("message", "搜尋有誤，請確認地點、日期、房間等資料是否正確輸入");
		}

		return resList;
	}

	@GetMapping("/image/room/{roomId}/{num}")
	public ResponseEntity<byte[]> getRoomImage(@PathVariable Integer roomId, @PathVariable Integer num) {
		List<RoomTypeImgVO> roomTypeImg = roomTypeImgService.findImagesByRoomTypeId(roomId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) // 假設圖片為 JPEG 格式
				.body(roomTypeImg.get(num).getPicture());
	}

	@GetMapping("/image/hotel/{hoteId}/{num}")
	public ResponseEntity<byte[]> getHotelImage(@PathVariable Integer hoteId, @PathVariable Integer num) {
		List<HotelImgVO> hotelImg = hotelImgService.getImagesByHotelId(hoteId);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(hotelImg.get(num).getPicture());
	}
	
}
