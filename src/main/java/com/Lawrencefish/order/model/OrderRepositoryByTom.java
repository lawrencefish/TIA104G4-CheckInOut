package com.Lawrencefish.order.model;

import com.order.model.OrderVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryByTom extends JpaRepository<OrderVO, Integer> {


    @Query(value = """
    SELECT o.order_id, m.member_id, CONCAT(m.first_name, ' ', m.last_name) AS full_name, o.status
    FROM orders o
    JOIN member m ON o.member_id = m.member_id
    WHERE o.check_in_date = :checkInDate AND o.hotel_id = :hotelId
""", nativeQuery = true)
    List<Object[]> findTodayOrdersWithCustomer(@Param("checkInDate") Date checkInDate, @Param("hotelId") Integer hotelId);


    @Query(value = """
    SELECT o.order_id, m.member_id, CONCAT(m.first_name, ' ', m.last_name) AS full_name, o.status
    FROM orders o
    JOIN member m ON o.member_id = m.member_id
    WHERE o.check_out_date = :checkOutDate AND o.hotel_id = :hotelId
""", nativeQuery = true)
    List<Object[]> findTodayCheckoutOrders(@Param("checkOutDate") Date checkOutDate, @Param("hotelId") Integer hotelId);


    @Query(value = """
    SELECT o.*
    FROM orders o
    WHERE o.hotel_id = :hotelId
""", nativeQuery = true)
    List<OrderVO> findOrdersByHotelId(@Param("hotelId") Integer hotelId);


    @Query(value = "SELECT o FROM OrderVO o JOIN FETCH o.member WHERE o.hotel.hotelId = :hotelId ORDER BY o.orderId DESC")
    List<OrderVO> findOrdersWithMemberInfo(@Param("hotelId") Integer hotelId);


    // 日期與關鍵字的複合查詢
    @Query("SELECT o FROM OrderVO o " +
            "WHERE o.hotel.hotelId = :hotelId " +
            "AND (CAST(o.checkInDate AS string) LIKE %:date% " +
            "OR CAST(o.checkOutDate AS string) LIKE %:date%) " +
            "AND (CAST(o.orderId AS string) LIKE %:keyword% " +
            "OR o.member.firstName LIKE %:keyword% " +
            "OR o.member.lastName LIKE %:keyword% " +
            "OR CAST(o.member.memberId AS string) LIKE %:keyword%)")
    List<OrderVO> searchByDateAndKeyword(@Param("hotelId") Integer hotelId,
                                         @Param("date") String date,
                                         @Param("keyword") String keyword);

    // 僅日期的查詢
    @Query("SELECT o FROM OrderVO o " +
            "WHERE o.hotel.hotelId = :hotelId " +
            "AND (CAST(o.checkInDate AS string) LIKE %:date% " +
            "OR CAST(o.checkOutDate AS string) LIKE %:date%)")
    List<OrderVO> searchByDate(@Param("hotelId") Integer hotelId, @Param("date") String date);

    // 僅關鍵字的查詢
    @Query("SELECT o FROM OrderVO o " +
            "WHERE o.hotel.hotelId = :hotelId " +
            "AND (CAST(o.orderId AS string) LIKE %:keyword% " +
            "OR o.member.firstName LIKE %:keyword% " +
            "OR o.member.lastName LIKE %:keyword% " +
            "OR CAST(o.member.memberId AS string) LIKE %:keyword%)")
    List<OrderVO> searchByKeyword(@Param("hotelId") Integer hotelId, @Param("keyword") String keyword);
}
