package com.order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {
	
	// 查詢會員已完成退房的不重複城市清單
    @Query(value = 
        "SELECT DISTINCT h.city " +
        "FROM `order` o " +
        "JOIN hotel h ON o.hotel_id = h.hotel_id " +
        "WHERE o.member_id = :memberId " +
        "AND o.order_status = 2 " +  // 已退房的訂單
        "GROUP BY h.city",
        nativeQuery = true)
    List<String> findDistinctCitiesByMember(@Param("memberId") Integer memberId);

    // 計算不重複城市數量
    @Query(value = 
        "SELECT COUNT(DISTINCT h.city) " +
        "FROM `order` o " +
        "JOIN hotel h ON o.hotel_id = h.hotel_id " +
        "WHERE o.member_id = :memberId " +
        "AND o.order_status = 2",
        nativeQuery = true)
    Integer countMemberTravelCities(@Param("memberId") Integer memberId);

}