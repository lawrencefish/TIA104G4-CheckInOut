package com.user.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.creditcard.model.CreditcardService;
import com.creditcard.model.CreditcardVO;
import com.googleAPI.GeocodingService;
import com.hotel.model.HotelService;
import com.hotelFacility.model.HotelFacilityService;
import com.hotelImg.model.HotelImgService;
import com.member.model.MemberVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponService;
import com.membercoupon.model.MemberCouponVO;
import com.order.model.OrderService;
import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailService;
import com.orderDetail.model.OrderDetailVO;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.HotelRoomInventoryDTO;
import com.roomInventory.model.RoomInventoryService;
import com.roomInventory.model.RoomInventoryVO;
import com.roomTypeFacility.model.RoomTypeFacilityService;
import com.roomTypeImg.model.RoomTypeImgService;
import com.roomTypeImg.model.RoomTypeImgVO;

@RestController
@RequestMapping("/order/api")
public class UserOrderController {

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
	MemberCouponService mCService;
	@Autowired
	CreditcardService cDService;
	@Autowired
	OrderDetailService oDService;

	// 取得購物車訂單明細
	@PostMapping("/cart/get")
	public ResponseEntity<List<Map<String, Object>>> getCart(HttpSession session) {
		List<Map<String, Object>> cartList = (List<Map<String, Object>>) session.getAttribute("cartList");

		// 如果購物車是空的，返回提示信息
		if (cartList == null || cartList.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "購物車是空的");
			response.put("empty", "true");
			return ResponseEntity.ok(Collections.singletonList(response));
		}

		// 調用 checkCart 方法來檢查並更新購物車數據
		List<Map<String, Object>> updatedCartList = checkCart(cartList);

		return ResponseEntity.ok(updatedCartList);
	}

	public List<Map<String, Object>> checkCart(List<Map<String, Object>> cartList) {
		List<Map<String, Object>> updatedCartList = new ArrayList<>();

		for (Map<String, Object> cart : cartList) {
			List<Map<String, Object>> cartDetailList = (List<Map<String, Object>>) cart.get("cartDetailList");
			List<Map<String, Object>> updatedCartDetailList = new ArrayList<>();

			for (Map<String, Object> cartDetail : cartDetailList) {
				try {
					Integer roomTypeId = (Integer) cartDetail.get("roomTypeId");
					Integer roomNum = (Integer) cartDetail.get("roomNum");
					Integer guestNum = (Integer) cartDetail.get("guestNum");
					LocalDate checkInDate = (LocalDate) cartDetail.get("checkInDate");
					LocalDate checkOutDate = (LocalDate) cartDetail.get("checkOutDate");
					LocalDate checkOutDateMOne = checkOutDate.minusDays(1);

					// 查詢該房型在入住期間的可用房間資訊
					List<HotelRoomInventoryDTO> cartDetailInfoList = RIservice
							.findRoomsFromDateAndRoomTypeId(checkInDate, checkOutDateMOne, roomTypeId);

					int daysBetween = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);

					// 若查詢結果數量不匹配入住天數，則略過該筆資料
					if (cartDetailInfoList == null || cartDetailInfoList.size() != daysBetween) {
						continue;
					}

					int totalPrice = 0;
					int totalbreakPrice = 0;

					// 更新價格資訊
					for (HotelRoomInventoryDTO cartDetailInfo : cartDetailInfoList) {
						PriceVO todayPrice = Pservice.getPriceOfDay(roomTypeId, cartDetailInfo.getDate());
						cartDetail.put("roomName", cartDetailInfo.getRoomName());
						cartDetail.put("breakfast", cartDetailInfo.getBreakfast());
						totalPrice += (todayPrice.getPrice()*roomNum);

						if (cartDetailInfo.getBreakfast() != 0) {
							totalbreakPrice += (todayPrice.getBreakfastPrice()*guestNum);
							totalPrice += (todayPrice.getBreakfastPrice()*guestNum);
						}
					}

					// 更新購物車詳細資訊
					cartDetail.put("totalPrice", totalPrice);
					cartDetail.put("totalbreakPrice", totalbreakPrice);

					updatedCartDetailList.add(cartDetail);
				} catch (Exception e) {
					System.out.println("Error processing cartDetail: " + e.getMessage());
					e.printStackTrace();
				}
			}

			// 如果 cartDetailList 還有有效的房型，才保留
			if (!updatedCartDetailList.isEmpty()) {
				cart.put("cartDetailList", updatedCartDetailList);
				updatedCartList.add(cart);
			}
		}

		return updatedCartList;
	}

	// 刪除飯店資訊
	@PostMapping("/cart/delete")
	public ResponseEntity<Map<String, Object>> deleteCart(@RequestParam String roomTypeId, HttpSession session) {
		List<Map<String, Object>> cartList = (List<Map<String, Object>>) session.getAttribute("cartList");

		// 如果購物車是空的，直接回傳
		if (cartList == null || cartList.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "購物車是空的");
			response.put("empty", "true");
			return ResponseEntity.ok(response);
		}

		boolean itemRemoved = false;
		Iterator<Map<String, Object>> cartIterator = cartList.iterator();

		while (cartIterator.hasNext()) {
			Map<String, Object> cart = cartIterator.next();

			if (cart.containsKey("cartDetailList")) {
				List<Map<String, Object>> cartDetailList = (List<Map<String, Object>>) cart.get("cartDetailList");

				// 使用 Iterator 移除符合 roomTypeId 的項目
				cartDetailList.removeIf(item -> item.containsKey("roomTypeId")
						&& String.valueOf(item.get("roomTypeId")).equals(roomTypeId));

				// 如果 cartDetailList 清空了，才移除 cart
				if (cartDetailList.isEmpty()) {
					cartIterator.remove();
				} else {
					// 更新 cartDetailList
					cart.put("cartDetailList", cartDetailList);
				}

				itemRemoved = true;
			}
		}

		// 更新 session
		session.setAttribute("cartList", cartList);

		// 準備回應
		Map<String, Object> response = new HashMap<>();
		if (itemRemoved) {
			response.put("removed", "true");
			response.put("message", "成功移除");
		} else {
			response.put("message", "移除失敗，請稍後再試");
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/cart/addOrder")
	public ResponseEntity<Map<String, Object>> addOrder(@RequestParam String hotelId, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			session.setAttribute("hotelIdTobeCheckOut", hotelId);
			response.put("message", "ok");
			response.put("id", session.getAttribute("hotelIdTobeCheckOut"));
		} catch (Exception e) {
			response.put("message", e.getMessage());
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("/order/get")
	public ResponseEntity<Map<String, Object>> getOrder(HttpSession session) {
		List<Map<String, Object>> cartList = (List<Map<String, Object>>) session.getAttribute("cartList");
		Integer hotelId = Integer.valueOf((String) session.getAttribute("hotelIdTobeCheckOut"));

		// 如果購物車是空的，直接回傳
		if (cartList == null || cartList.isEmpty()) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "無法取得要結帳的資訊");
			response.put("empty", "true");
			return ResponseEntity.ok(response);
		}

		Map<String, Object> OrderTobeCheckOut = new HashMap<>();
		for (Map<String, Object> cart : cartList) {
			if (cart.containsKey("hotelId")) {
				if (Integer.valueOf(cart.get("hotelId").toString()).equals(hotelId)) {
					OrderTobeCheckOut = cart;
					session.setAttribute("OrderTobeCheckOut", OrderTobeCheckOut);
					break; // 找到後就跳出迴圈
				}
			}
		}
		return ResponseEntity.ok(OrderTobeCheckOut);
	}

	@PostMapping("/order/getMemberInfo")
	public ResponseEntity<Map<String, Object>> getMemberInfo(HttpSession session) {
		MemberVO member = (MemberVO) session.getAttribute("member");
		List<Map<String, Object>> couponList = getMemberCoupon(member);
		List<Map<String, Object>> creditCardList = getMemberCreditCard(member);
		Map<String, Object> response = new HashMap<>();
		response.put("lastName", member.getLastName());
		response.put("firstName", member.getFirstName());
		response.put("email", member.getAccount());
		response.put("id", member.getMemberId());
		response.put("counponList", couponList);
		response.put("creditCardList", creditCardList);
		return ResponseEntity.ok(response);
	}

	public List<Map<String, Object>> getMemberCoupon(MemberVO member) {
		List<MemberCouponVO> mCList = mCService.findByMemberIdAndCouponStatus(member.getMemberId(), (byte) 1);
		List<Map<String, Object>> couponList = new ArrayList<>();
		for (MemberCouponVO mC : mCList) {
			Map<String, Object> coupon = new HashMap<>();
			coupon.put("id", mC.getMemberCouponId());
			coupon.put("name", mC.getCoupon().getCouponName());
			coupon.put("detail", mC.getCoupon().getCouponDetail());
			coupon.put("discount", mC.getCoupon().getDiscountAmount());
			couponList.add(coupon);
		}
		return couponList;
	}

	public List<Map<String, Object>> getMemberCreditCard(MemberVO member) {
		List<CreditcardVO> cList = cDService.findCreditcardByMemberId(member.getMemberId());
		List<Map<String, Object>> creditCardList = new ArrayList<>();
		Map<String, Object> creditCard = new HashMap<>();
		for (CreditcardVO c : cList) {
			creditCard.put("id", c.getCreditcardId());
			creditCard.put("name", c.getCreditcardName());
			creditCard.put("num", c.getCreditcardNum());
			creditCardList.add(creditCard);
		}
		return creditCardList;
	}
	
	@Transactional
	@PostMapping("/order/checkout")
	public Map<String, Object> checkout(@RequestBody Map<String, Object> orderInfo, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		String email = (String) orderInfo.get("email");
		String lastName = (String) orderInfo.get("lastName");
		String firstName = (String) orderInfo.get("firstName");
		String memo = (String) orderInfo.get("memo");
		Integer couponId = (Integer) orderInfo.get("coupon");
		LocalDate checkInDate = LocalDate.parse((String) orderInfo.get("checkInDate"));
		LocalDate checkOutDate = LocalDate.parse((String) orderInfo.get("checkOutDate"));
		Integer finalPrice = Integer.valueOf((String) orderInfo.get("finalPrice"));
		Integer discount = 0;
		Object savedCardObj = orderInfo.get("savedCard"); // 取得 `savedCard` 值
		CreditcardVO creditcard = null; // 用來存信用卡物件

		if (savedCardObj instanceof String) {
			// **如果是已存信用卡（信用卡 ID）**
			Integer savedCardId = Integer.valueOf((String) savedCardObj);
			creditcard = cDService.queryCreditCard(savedCardId); // 查詢信用卡
		} else if (savedCardObj instanceof Map) {
			// **如果是新信用卡**
			Map<String, Object> savedCardMap = (Map<String, Object>) savedCardObj;
			System.out.println(savedCardMap);
			String name = lastName+"的信用卡"+checkInDate.toString();
			creditcard = new CreditcardVO();
			creditcard.setCreditcardInfo(name,
					(String) savedCardMap.get("cardNumber"), (String) savedCardMap.get("securityCode"),
					(String) savedCardMap.get("expiryDate"), (MemberVO) session.getAttribute("member"));
			// **存入資料庫**
			creditcard = cDService.addCreditCardAndGet(creditcard);
		}

		Map<String, Object> orderSaved = (Map<String, Object>) session.getAttribute("OrderTobeCheckOut");		
		List<Map<String, Object>> orderSavedDetailList = (List<Map<String, Object>>) orderSaved.get("cartDetailList");
		try {
			OrderVO order = new OrderVO();
			order.setCheckInDate(Date.valueOf(checkInDate));
			order.setCheckOutDate(Date.valueOf(checkOutDate));
			order.setHotel(hotelService.findById((Integer) orderSaved.get("hotelId")).orElse(null));
			order.setMember((MemberVO) session.getAttribute("member"));
			order.setTotalAmount(Integer.valueOf(finalPrice));
			order.setCreditcard(creditcard);
			order.setGuestLastName(lastName);
			order.setGuestFirstName(firstName);
			order.setMemo(memo);

			Integer reCalcTotalPrice = 0;
			List<OrderDetailVO> orderDetails = new ArrayList<>();
			for (Map<String, Object> orderSavedDetail : orderSavedDetailList) {
				Integer roomTypeId = (Integer) orderSavedDetail.get("roomTypeId");
				Byte breakfast = (Byte) orderSavedDetail.get("breakfast");
				Integer guestNum = (Integer) orderSavedDetail.get("guestNum");
				Integer roomNum = (Integer) orderSavedDetail.get("roomNum");
				for (LocalDate date = checkInDate; !date.isEqual(checkOutDate); date = date.plusDays(1)) {
					RoomInventoryVO ri = RIservice.findByRoomTypeIdAndDate(roomTypeId, date);
					Integer newQuantity = ri.getAvailableQuantity() - roomNum;
					System.out.println(ri.toString());
					System.out.println(ri.getAvailableQuantity());
					System.out.println(roomTypeId);
					System.out.println(date);
					System.out.println(roomNum);
					if (newQuantity >= 0) {
						ri.setAvailableQuantity(newQuantity);
						RIservice.roomTransaction(ri);
					} else {
					    response.put("message", "房間不足，請選擇其他日期或房型");
				        throw new RuntimeException("房間不足，請選擇其他日期或房型");					    
					}
					PriceVO todayPrice = Pservice.getPriceOfDay(roomTypeId, date);
					Integer todayTotalPrice = (breakfast != 0)
							? (todayPrice.getPrice() * roomNum) + (todayPrice.getBreakfastPrice() * guestNum)
							: todayPrice.getPrice() * roomNum;
					reCalcTotalPrice += todayTotalPrice;
					System.out.println("今日房價:"+todayPrice.getPrice()*roomNum);
					System.out.println("今日早餐價:"+todayPrice.getBreakfastPrice()*guestNum);
					System.out.println("今日總價:"+todayTotalPrice);

				}
				System.out.println(reCalcTotalPrice);
				OrderDetailVO orderDetail = new OrderDetailVO();
				orderDetail.setBreakfast(breakfast);
				orderDetail.setGuestNum(guestNum);
				orderDetail.setOrder(order);
				orderDetail.setRoomNum(roomNum);
				orderDetail.setRoomTypeId(roomTypeId);
				oDService.addOrderDetail(orderDetail);
				orderDetails.add(orderDetail);
			}
						
			if (couponId != 0 ) {
				MemberCouponVO coupon =  mCService.getById(couponId);
	            if (coupon != null && coupon.getCoupon().getDiscountAmount() != null) {
	                discount = coupon.getCoupon().getDiscountAmount();    
					reCalcTotalPrice -= discount;
	                coupon.setCouponStatus((byte) 0); // 標記已使用
	                mCService.save(coupon);
	            }
			}

			
			System.out.println("重算的:"+reCalcTotalPrice);
			System.out.println("訂單的:"+finalPrice);
			System.out.println(finalPrice==reCalcTotalPrice);
			
			if (reCalcTotalPrice.equals(finalPrice)) {
				
				order.setTotalAmount(reCalcTotalPrice);
				orderService.addOrder(order);
				response.put("message", "交易成功");
			}else {
		        throw new RuntimeException("金額錯誤，請重新再試一次");

			}
		} catch (Exception e) {
		    e.printStackTrace(); // 印出完整的錯誤訊息到控制台
		    response.put("message", "訂單失敗，請重新再試一次");
		    response.put("error", e.toString()); // 儲存完整錯誤資訊
		    throw e; 
		}
		return response;
	}

}
