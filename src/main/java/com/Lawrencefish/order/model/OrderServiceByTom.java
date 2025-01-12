package com.Lawrencefish.order.model;

import com.order.model.OrderRepository;
import com.order.model.OrderVO;
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
			map.put("memberName", order.getMember() != null ? order.getMember().getLastName() + " " + order.getMember().getFirstName() : "未指定");
			map.put("totalAmount", order.getTotalAmount());
			map.put("status", order.getStatus() == 1 ? "已完成" : "未完成");
			return map;
		}).collect(Collectors.toList());
	}
}
