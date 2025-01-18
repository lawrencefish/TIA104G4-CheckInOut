package com.user.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.HotelRoomInventoryDTO;
import com.roomInventory.model.RoomInventoryService;
import com.roomType.model.RoomTypeService;
import com.roomTypeFacility.model.RoomTypeFacilityService;
import com.roomTypeImg.model.RoomTypeImgService;
import com.roomTypeImg.model.RoomTypeImgVO;

@RestController
@RequestMapping("/booking/api")
public class UserBookingController {

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
	@Autowired
	RoomTypeService RTService;

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

	// 存入購物車訂單明細
	@PostMapping("/addCart")
	public ResponseEntity<Map<String, String>> addCart(@RequestBody Map<String, String> orderDetail,
			HttpSession session) {
		// 從 session 取得購物車 cartList，如果 session 為空則初始化
		List<Map<String, Object>> cartList = (List<Map<String, Object>>) session.getAttribute("cartList");
		if (cartList == null) {
			cartList = new ArrayList<>();
		}

		// 解析傳入的訂單資訊
		Integer hotelId = Integer.valueOf(orderDetail.get("hotelId"));
		String hotelName = orderDetail.get("hotelName");
		Double review = Double.valueOf(orderDetail.get("review"));

		LocalDate checkInDate = LocalDate.parse(orderDetail.get("checkInDate"));
		LocalDate checkOutDate = LocalDate.parse(orderDetail.get("checkOutDate"));

		Integer roomTypeId = Integer.valueOf(orderDetail.get("roomTypeId"));
		Integer guestNum = Integer.valueOf(orderDetail.get("guestNum"));
		Integer roomNum = Integer.valueOf(orderDetail.get("roomNum"));

		// 建立 cartDetail（單筆房型資訊）
		Map<String, Object> cartDetail = new HashMap<>();
		cartDetail.put("checkInDate", checkInDate);
		cartDetail.put("checkOutDate", checkOutDate);
		cartDetail.put("roomTypeId", roomTypeId);
		cartDetail.put("guestNum", guestNum);
		cartDetail.put("roomNum", roomNum);

		boolean foundHotel = false; // 是否找到相同的 hotelId
		boolean notSameDateExists = false; // 是否已存在相同房型且日期完全一致
		boolean sameDateAndRoomType = false; // 是否已存在相同房型且日期完全一致

		for (Map<String, Object> existingCart : cartList) {
			if (existingCart.get("hotelId").equals(hotelId)) {
				foundHotel = true;
				List<Map<String, Object>> cartDetailList = (List<Map<String, Object>>) existingCart
						.get("cartDetailList");

				if (cartDetailList == null) {
					cartDetailList = new ArrayList<>();
					existingCart.put("cartDetailList", cartDetailList);
				}

				// **檢查是否已有相同房型且日期完全一致**
				for (Map<String, Object> existingDetail : cartDetailList) {
					Integer existingRoomTypeId = (Integer) existingDetail.get("roomTypeId");
					LocalDate existingCheckInDate = (LocalDate) existingDetail.get("checkInDate");
					LocalDate existingCheckOutDate = (LocalDate) existingDetail.get("checkOutDate");

					if (existingRoomTypeId.equals(roomTypeId) && existingCheckInDate.equals(checkInDate)
							&& existingCheckOutDate.equals(checkOutDate)) {
						sameDateAndRoomType = true;
						System.out.println(notSameDateExists);
						break;
					}

					if (existingRoomTypeId.equals(roomTypeId) && !existingCheckInDate.equals(checkInDate)
							&& !existingCheckOutDate.equals(checkOutDate)) {
						notSameDateExists = true;
						System.out.println(notSameDateExists);
						break;
					}
				}

				// **只有入住與退房日期一致的情況下才允許新增**
				if (!notSameDateExists && !sameDateAndRoomType) {
					cartDetailList.add(cartDetail);
				} else if(sameDateAndRoomType){
					Map<String, String> errorResponse = new HashMap<>();
					errorResponse.put("message", "<strong>已經有相同的房型加入，請勿重新加入</strong>");
					errorResponse.put("dateMismatch", "true");
					return ResponseEntity.badRequest().body(errorResponse);
					
				}else{
					Map<String, String> errorResponse = new HashMap<>();
					errorResponse.put("message", "<strong>入住日期跟退房日期不一致，無法加入購物車</strong><br>相同旅館只能將入住與退房日期一致的房型加入購物車。");
					errorResponse.put("dateMismatch", "true");
					return ResponseEntity.badRequest().body(errorResponse);
				}

				break;
			}
		}

		// 如果 `hotelId` 尚未存在於 `cartList`，則新增 `cart`
		if (!foundHotel) {
			Map<String, Object> cart = new HashMap<>();
			cart.put("hotelId", hotelId);
			cart.put("hotelName", hotelName);
			cart.put("review", review);

			List<Map<String, Object>> cartDetailList = new ArrayList<>();
			cartDetailList.add(cartDetail);
			cart.put("cartDetailList", cartDetailList);

			cartList.add(cart);
		}

		// 更新 session，確保購物車內容被保存
		session.setAttribute("cartList", cartList);

		// 回傳成功訊息
		Map<String, String> response = new HashMap<>();
		response.put("message", "ok");

		// Debug 輸出
		System.out.println("目前購物車：" + session.getAttribute("cartList"));

		return ResponseEntity.ok(response);
	}

	// 取得飯店資訊
	@PostMapping("/hotel_detail")
	public Map<String, Object> getHotelInfo(@RequestBody Map<String, String> parsedHotelId) {
		Integer hotelId = Integer.valueOf(parsedHotelId.get("id"));
		HotelVO hotel = hotelService.findById(hotelId).orElse(null);
		List<OrderVO> orders = orderService.findByHotelId(hotelId);
		Double ratings = orderService.getAvgRatingAndCommentCounts(hotelId).getAvgRating();
		List<HotelFacilityVO> hotelFList = HFService.findFacilityVOIdsByHotelId(hotelId);
		Map<String, Object> response = new HashMap<String, Object>();

		response.put("name", hotel.getName());
		response.put("info", hotel.getInfoText());
		response.put("city", hotel.getCity());
		response.put("district", hotel.getDistrict());
		response.put("address", hotel.getAddress());
		response.put("lat", String.valueOf(hotel.getLatitude()));
		response.put("lng", String.valueOf(hotel.getLongitude()));
		response.put("avgRatings", String.valueOf(ratings));
		response.put("imgNum", String.valueOf(hotelImgService.countByHotelId(hotelId)));

		List<FacilityVO> facility = new ArrayList<FacilityVO>();
		for (HotelFacilityVO hf : hotelFList) {
			facility.add(hf.getFacility());
		}
		response.put("facility", facility);
		List<Map<String, String>> comments = new ArrayList<Map<String, String>>();
		for (OrderVO order : orders) {
			Map<String, String> comment = new HashMap<String, String>();
			comment.put("orderId", String.valueOf(order.getOrderId()));
			comment.put("guest", order.getGuestLastName());
			comment.put("rating", String.valueOf(order.getRating()));
			comment.put("comment", order.getCommentContent());
			comment.put("time", String.valueOf(order.getCommentCreateTime()));
			comments.add(comment);
		}
		response.put("comments", comments);

		return response;
	}

	@PostMapping("/update")
	public Map<String, String> updateRoomInventory(@RequestBody Map<String, String> info, HttpSession session) {
		Map<String, String> response = new HashMap<>();

		try {
			Integer guestNum = Integer.valueOf(info.get("guestNum"));
			Integer roomNum = Integer.valueOf(info.get("roomNum"));
			LocalDate checkInDate = LocalDate.parse(info.get("startDate"));
			LocalDate checkOutDate = LocalDate.parse(info.get("endDate"));
			session.setAttribute("guestNum", guestNum);
			session.setAttribute("roomNum", roomNum);
			session.setAttribute("checkInDate", checkInDate);
			session.setAttribute("checkOutDate", checkOutDate);
			response.put("message", "OK");
		} catch (Exception e) {
			response.put("message", e.getMessage());
		}
		return response;
	}

	// 取得每天旅館庫存
	@PostMapping("/calendar/inventory")
	public ResponseEntity<Map<String, Object>> getCalendarRoomInventory(@RequestBody Map<String, String> parsedHotel,
			HttpSession session) {
		Integer hotelId = Integer.valueOf(parsedHotel.get("id"));
		Integer guestNum = Integer.valueOf(parsedHotel.get("guestNum"));
		Integer roomNum = Integer.valueOf(parsedHotel.get("roomNum"));

		Map<String, Object> hotelResponse = new HashMap<>();
		List<HotelRoomInventoryDTO> hotels = RIservice.findAvailableRoomsFromHotel(hotelId);
		Map<LocalDate, Map<String, String>> dailyInventory = new HashMap<>();

		for (HotelRoomInventoryDTO room : hotels) {
			Integer roomTypeId = room.getRoomTypeId();
			LocalDate date = room.getDate();

			if (guestNum != 0 && roomNum != 0) {
				int maxPerson = room.getMaxPerson();
				int needRooms = (guestNum + maxPerson - 1) / maxPerson;

				if (needRooms > roomNum || room.getAvailableQuantity() < roomNum) {
					continue;
				}

				// 取得當天房價
				PriceVO todayPrice = Pservice.getPriceOfDay(roomTypeId, date);
				Integer totalPrice = (room.getBreakfast() == 1)
						? todayPrice.getPrice() * roomNum + todayPrice.getBreakfastPrice() * guestNum
						: todayPrice.getPrice() * roomNum;

				// 如果當天還沒有記錄最便宜的房價，或者新房價更低，則更新
				if (!dailyInventory.containsKey(date)
						|| totalPrice < Integer.valueOf(dailyInventory.get(date).get("price"))) {
					Map<String, String> daydto = new HashMap<>();
					daydto.put("date", String.valueOf(date));
					daydto.put("price", String.valueOf(totalPrice));
					daydto.put("roomTypeId", String.valueOf(roomTypeId));
					dailyInventory.put(date, daydto);
				}

				System.out.println(roomTypeId + ":" + date + ":" + totalPrice);
			}
		}

		hotelResponse.put("date", new ArrayList<>(dailyInventory.values()));
		return ResponseEntity.ok(hotelResponse);
	}

	// 取得旅館庫存
	@PostMapping("/hotel_detail/inventory")
	public ResponseEntity<Map<String, Object>> getHotelRoomInventory(@RequestBody Map<String, String> parsedHotelId,
			HttpSession session) {
		Integer hotelId = Integer.valueOf(parsedHotelId.get("id"));
		Map<String, Object> hotelResponse = new HashMap<>();
		// 從 Session 取得屬性
		Integer guestNum = (Integer) session.getAttribute("guestNum");
		Integer roomNum = (Integer) session.getAttribute("roomNum");
		LocalDate checkInDate = (LocalDate) session.getAttribute("checkInDate");
		LocalDate checkOutDate = (LocalDate) session.getAttribute("checkOutDate");

		// 判斷是否缺少必要的 Session 資訊
		boolean sessionMissing = (guestNum == null || roomNum == null || checkInDate == null || checkOutDate == null);

		List<HotelRoomInventoryDTO> dtoList = RIservice.findAvailableRoomsFromHotel(hotelId);
		if (dtoList == null || dtoList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		hotelResponse.put("hotelId", hotelId);

		// 初始化分組用的地圖結構
		Map<Integer, Map<LocalDate, HotelRoomInventoryDTO>> roomTypeDateMap = new LinkedHashMap<>();
		Map<Integer, Map<String, Object>> roomTypeInfoMap = new LinkedHashMap<>();

		try {
			// 分組房型資訊，若 Session 缺失則不加入庫存相關資料
			for (HotelRoomInventoryDTO dto : dtoList) {
				Integer roomTypeId = dto.getRoomTypeId();
				LocalDate date = dto.getDate();
				roomTypeInfoMap.computeIfAbsent(roomTypeId, k -> {
					Map<String, Object> info = new HashMap<>();
					List<FacilityVO> roomFacility = RTFService.getFacilitiesOrServicesByRoomType(roomTypeId, 0);
					List<FacilityVO> roomService = RTFService.getFacilitiesOrServicesByRoomType(roomTypeId, 1);

					info.put("roomNum", roomNum);
					info.put("guestNum", guestNum);
					info.put("roomTypeId", dto.getRoomTypeId());
					info.put("roomName", dto.getRoomName());
					info.put("maxPerson", dto.getMaxPerson());
					info.put("breakfast", dto.getBreakfast());
					info.put("roomFacility", roomFacility);
					info.put("roomService", roomService);
					info.put("imgNum", roomTypeImgService.countByRoomTypeId(roomTypeId));

					// 只有在 Session 完整時才初始化 inventories 列表
					if (!sessionMissing) {
						info.put("inventories", new ArrayList<Map<String, Object>>());
					}
					return info;
				});

				// 只有在 Session 完整時才將日期與 DTO 分組
				if (!sessionMissing) {
					roomTypeDateMap.computeIfAbsent(roomTypeId, k -> new LinkedHashMap<>()).put(date, dto);
				}
			}

			// 如果 Session 資料完整，則進行庫存與價格計算
			if (!sessionMissing) {
				LocalDate checkOutDateMOne = checkOutDate.minusDays(1);
				for (Map.Entry<Integer, Map<LocalDate, HotelRoomInventoryDTO>> entry : roomTypeDateMap.entrySet()) {
					Integer totalPrice = 0;
					Integer roomTypeId = entry.getKey();
					Map<LocalDate, HotelRoomInventoryDTO> dateToDto = entry.getValue();

					Map<String, Object> roomType = roomTypeInfoMap.get(roomTypeId);
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> inventories = (List<Map<String, Object>>) roomType.get("inventories");

					int maxPerson = (int) roomType.get("maxPerson");
					int needRooms = (guestNum + maxPerson - 1) / maxPerson;

					if (needRooms > roomNum) {
						roomTypeInfoMap.remove(roomTypeId); // 移除不符合需求的房型
						continue;
					}

					boolean isRoomAvailableEveryDay = true;
					LocalDate currentDate = checkInDate;
					while (!currentDate.isAfter(checkOutDateMOne)) {
						HotelRoomInventoryDTO dayDto = dateToDto.get(currentDate);
						if (dayDto == null || dayDto.getAvailableQuantity() < needRooms) {
							isRoomAvailableEveryDay = false;
							break;
						}

						Map<String, Object> inventory = new HashMap<>();
						PriceVO todayPrice = Pservice.getPriceOfDay(roomTypeId, dayDto.getDate());
						inventory.put("inventoryId", dayDto.getInventoryId());
						inventory.put("date", dayDto.getDate());
						inventory.put("price", todayPrice.getPrice());
						inventory.put("breakfastPrice", todayPrice.getBreakfastPrice());
						inventory.put("availableQuantity", dayDto.getAvailableQuantity());
						inventories.add(inventory);
						totalPrice += todayPrice.getPrice();
						if (dayDto.getBreakfast() == 1) {
							totalPrice += todayPrice.getBreakfastPrice();
						}
						currentDate = currentDate.plusDays(1);
					}

					if (!isRoomAvailableEveryDay) {
						roomTypeInfoMap.remove(roomTypeId);
					}

					roomType.put("totalPrice", String.valueOf(totalPrice));
				}
			}

			List<Map<String, Object>> rooms = new ArrayList<>(roomTypeInfoMap.values());
			hotelResponse.put("rooms", rooms);

			// 當 Session 缺失時，添加提示訊息
			if (sessionMissing) {
				hotelResponse.put("message", "缺少部分查詢條件，因此未返回庫存與價格資訊。");
				hotelResponse.put("nosession", "true");
			}

		} catch (Exception e) {
			hotelResponse.put("error", "發生未知錯誤：" + e.getMessage());
			return ResponseEntity.status(500).body(hotelResponse);
		}

		return ResponseEntity.ok(hotelResponse);
	}

}
