package com.Lawrencefish.order.model;

import com.Lawrencefish.OrderDetail.model.OrderDetailRepositoryByTom;
import com.order.model.OrderRepository;
import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailVO;
import com.roomInventory.model.RoomInventoryService;
import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service("orderServiceByTom")
public class OrderServiceByTom {
	@Autowired
	private OrderRepositoryByTom orderRepository;
	@Autowired
	private RoomTypeRepository roomTypeRepository;
	@Autowired
	private OrderDetailRepositoryByTom orderDetailRepository;
	@Autowired
	private RoomInventoryService roomInventoryService;

	public List<Map<String, Object>> getTodayOrders(Integer hotelId) {
		// 獲取今天日期
		LocalDate today = LocalDate.now();
		Date todayAsDate = Date.valueOf(today);

		// 查詢今日訂單
		List<Object[]> result = orderRepository.findTodayOrdersWithCustomer(todayAsDate, hotelId);

		// 將查詢結果轉換為 Map 格式
		return result.stream().map(row -> {
			Byte status = (Byte) row[3]; // 明確處理 Byte 型別
			return Map.of(
					"orderId", row[0],
					"memberId", row[1],
					"memberName", row[2],
					"status", status // 直接返回數字，不轉換為文字
			);
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> getTodayCheckoutOrders(Integer hotelId) {
		// 獲取今天的日期
		LocalDate today = LocalDate.now();
		Date todayAsDate = Date.valueOf(today);

		// 查詢今日退房的訂單
		List<Object[]> result = orderRepository.findTodayCheckoutOrders(todayAsDate, hotelId);

		// 將查詢結果轉換為 Map 格式
		return result.stream().map(row -> {
			Byte status = (Byte) row[3]; // 處理 Byte 型別的 status
			return Map.of(
					"orderId", row[0],
					"memberId", row[1],
					"memberName", row[2],
					"status", status // 直接返回數字，不轉換為文字
			);
		}).collect(Collectors.toList());
	}

	public List<OrderVO> getOrdersByHotelId(Integer hotelId) {
		return orderRepository.findOrdersByHotelId(hotelId);
	}

	public List<Map<String, Object>> getOrdersWithMemberInfo(Integer hotelId) {
		List<OrderVO> orders = orderRepository.findOrdersWithMemberInfo(hotelId);

		if (orders == null || orders.isEmpty()) {
			return Collections.emptyList();
		}
		return orders.stream().map(order -> {
			Map<String, Object> map = new HashMap<>();
			map.put("orderId", order.getOrderId());
			map.put("checkInDate", order.getCheckInDate());
			map.put("checkOutDate", order.getCheckOutDate());
			map.put("memberId", order.getMember() != null ? order.getMember().getMemberId() : null);
			map.put("memberName", order.getMember() != null ? order.getMember().getLastName() + "" + order.getMember().getFirstName() : "未指定");
			map.put("totalAmount", order.getTotalAmount());
			map.put("status", order.getStatus());
			return map;
		}).collect(Collectors.toList());
	}

	public List<Map<String, Object>> searchOrders(Integer hotelId, String date, String keyword) {
		List<OrderVO> orders;

		if (date != null && !date.isEmpty() && keyword != null && !keyword.isEmpty()) {
			orders = orderRepository.searchByDateAndKeyword(hotelId, date, keyword);
		} else if (date != null && !date.isEmpty()) {
			orders = orderRepository.searchByDate(hotelId, date);
		} else if (keyword != null && !keyword.isEmpty()) {
			orders = orderRepository.searchByKeyword(hotelId, keyword);
		} else {
			throw new IllegalArgumentException("查詢參數錯誤");
		}

		return orders.stream().map(order -> {
			Map<String, Object> map = new HashMap<>();
			map.put("orderId", order.getOrderId());
			map.put("checkInDate", order.getCheckInDate().toString());
			map.put("checkOutDate", order.getCheckOutDate().toString());
			map.put("memberId", order.getMember() != null ? order.getMember().getMemberId() : null);
			map.put("memberName", order.getMember() != null ? order.getMember().getLastName() + order.getMember().getFirstName() : "未指定");
			map.put("totalAmount", order.getTotalAmount());
			map.put("status", order.getStatus());
			return map;
		}).collect(Collectors.toList());
	}

	public Map<String, Object> getOrderDetails(Integer orderId) {
		// 查詢 Order
		OrderVO order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));

		// 建構回傳資料
		Map<String, Object> response = new HashMap<>();
		response.put("orderId", order.getOrderId());
		response.put("checkInDate", order.getCheckInDate());
		response.put("checkOutDate", order.getCheckOutDate());
		response.put("memberId", order.getMember().getMemberId());
		response.put("memberFirstName", order.getMember().getFirstName());
		response.put("memberLastName", order.getMember().getLastName());
		response.put("totalAmount", order.getTotalAmount());
		response.put("status", order.getStatus());
		response.put("memo", order.getMemo());
		// 直接包含 HotelVO
		response.put("hotel", order.getHotel());

		// 查詢訂單明細
		List<Map<String, Object>> detailsList = order.getOrderDetail().stream().map(detail -> {
			Map<String, Object> detailMap = new HashMap<>();
			detailMap.put("orderDetailId", detail.getOrderDetailId());
			detailMap.put("guestNum", detail.getGuestNum());
			detailMap.put("roomNum", detail.getRoomNum());
			detailMap.put("breakfast", detail.getBreakfast());

			// 查詢房型名稱
			RoomTypeVO roomType = roomTypeRepository.findById(detail.getRoomTypeId())
					.orElseThrow(() -> new RuntimeException("Room type not found for ID: " + detail.getRoomTypeId()));
			detailMap.put("roomTypeName", roomType.getRoomName());

			return detailMap;
		}).collect(Collectors.toList());

		response.put("orderDetails", detailsList);
		return response;
	}

	public boolean cancelOrder(Integer orderId) {
		// 查詢訂單是否存在
		OrderVO order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));

		// 更新訂單狀態為 3 (取消)
		order.setStatus((byte) 3); // 将整数 3 转换为 byte 类型
		orderRepository.save(order);

		return true;
	}

	@Transactional
	public boolean cancelOrderAndRestoreInventory(Integer orderId) {
		// 查詢訂單是否存在
		Optional<OrderVO> optionalOrder = orderRepository.findById(orderId);
		if (!optionalOrder.isPresent()) {
			throw new RuntimeException("訂單不存在，無法取消");
		}

		OrderVO order = optionalOrder.get();

		// 更新訂單狀態為取消
		if (order.getStatus() == 3) {
			throw new RuntimeException("訂單已被取消，無法再次取消");
		}
		order.setStatus((byte)3); // 設置訂單狀態為取消
		orderRepository.save(order);

		// 查詢訂單詳細資訊並恢復庫存
		List<OrderDetailVO> orderDetails = orderDetailRepository.findByOrder(order);
		for (OrderDetailVO detail : orderDetails) {
			// 將 Date 轉換為 LocalDate
			LocalDate checkInDate = order.getCheckInDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			// 恢復庫存
			roomInventoryService.increaseInventory(detail.getRoomTypeId(), checkInDate, detail.getRoomNum());
		}

		return true;
	}
}
