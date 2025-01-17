package com.user.controller;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
		// 先嘗試從 session 取出 cartList，若為 null 則初始化
		List<Map<String, Object>> cartList = (List<Map<String, Object>>) session.getAttribute("cartList");
		if (cartList == null) {
			cartList = new ArrayList<>();
		}

		Map<String, Object> cart = new HashMap<>();
		List<Map<String, Object>> cartDetailList = new ArrayList<>();
		Map<String, Object> cartDetail = new HashMap<>();

		Integer hotelId = Integer.valueOf(orderDetail.get("hotelId"));
		String hotelName = orderDetail.get("hotelName");
		Double review = Double.valueOf(orderDetail.get("review"));
		cart.put("hotelId", hotelId);
		cart.put("hotelName", hotelName);
		cart.put("review", review);

		// 解析 LocalDate
		LocalDate checkInDate = LocalDate.parse(orderDetail.get("checkInDate"));
		LocalDate checkOutDate = LocalDate.parse(orderDetail.get("checkOutDate"));

		// 解析 Integer
		Integer roomTypeId = Integer.valueOf(orderDetail.get("roomTypeId"));
		String roomTypeName = orderDetail.get("roomTypeName");
		Integer guestNum = Integer.valueOf(orderDetail.get("guestNum"));
		Integer roomNum = Integer.valueOf(orderDetail.get("roomNum"));
		Integer price = Integer.valueOf(orderDetail.get("price"));
		Integer breakfast = Integer.valueOf(orderDetail.get("breakfast"));

		// 存入 cartDetail
		cartDetail.put("checkInDate", checkInDate);
		cartDetail.put("checkOutDate", checkOutDate);
		cartDetail.put("roomTypeName", roomTypeName);
		cartDetail.put("roomTypeId", roomTypeId);
		cartDetail.put("guestNum", guestNum);
		cartDetail.put("roomNum", roomNum);
		cartDetail.put("price", price);
		cartDetail.put("breakfast", breakfast);
		cartDetailList.add(cartDetail);

		cart.put("cartDetail", cartDetailList);

		// **檢查相同 hotelId 是否已存在於 cartList**
		boolean found = false;
		for (Map<String, Object> existingCart : cartList) {
			if (existingCart.get("hotelId").equals(hotelId)) {
				List<Map<String, Object>> existingCartDetailList = (List<Map<String, Object>>) existingCart
						.get("cartDetail");
				existingCartDetailList.add(cartDetail);
				found = true;
				break;
			}
		}

		// 如果 hotelId **尚未存在**，新增整個 cart
		if (!found) {
			cartList.add(cart);
		}

		// 更新 session
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
			LocalDate checkInDate = LocalDate.parse(info.get("startDate"));
			LocalDate checkOutDate = LocalDate.parse(info.get("endDate"));
			Integer guestNum = Integer.valueOf(info.get("guestNum"));
			Integer roomNum = Integer.valueOf(info.get("roomNum"));
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
		List<Map<String, String>> dailyInventory = new ArrayList<>();
		for (HotelRoomInventoryDTO room : hotels) {
			Map<String, String> daydto = new HashMap<>();
			Integer roomTypeId = room.getRoomTypeId();
			Integer totalPrice = 0;
			LocalDate date = room.getDate();
			if (guestNum != 0 && roomNum != 0) {
				int maxPerson = (int) room.getMaxPerson();
				int needRooms = (guestNum + maxPerson - 1) / maxPerson;

				if (needRooms > roomNum || room.getAvailableQuantity() < roomNum) {
					continue;

				}
				PriceVO todayPrice = Pservice.getPriceOfDay(roomTypeId, date);
				totalPrice = (room.getBreakfast() == 1) ? todayPrice.getPrice() + todayPrice.getBreakfastPrice()
						: todayPrice.getPrice();
				daydto.put("price", String.valueOf(totalPrice));
			}
			daydto.put("date", String.valueOf(date));
			dailyInventory.add(daydto);
		}
		hotelResponse.put("date", dailyInventory);
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
