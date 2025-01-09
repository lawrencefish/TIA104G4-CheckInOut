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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("orderServiceByTom")
public class OrderServiceByTom {
	@Autowired
	private OrderRepositoryByTom orderRepository;

	public List<Map<String, Object>> getTodayOrders(Integer hotelId) {
		// 獲取今天的 LocalDate
		LocalDate today = LocalDate.now();

		// 將 LocalDate 轉換為 java.sql.Date
		java.sql.Date todayAsDate = java.sql.Date.valueOf(today);

		List<Object[]> result = orderRepository.findCustomOrders(todayAsDate, hotelId);

		return result.stream().map(row -> Map.of(
				"orderId", row[0],
				"checkInDate", row[1],
				"checkOutDate", row[2],
				"totalAmount", row[3]
		)).collect(Collectors.toList());
	}
}
