package com.Lawrencefish.order.model;

import com.order.model.OrderVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryByTom extends JpaRepository<OrderVO, Integer> {


    @Query(value = "SELECT o.order_id, o.check_in_date, o.check_out_date, o.total_amount FROM orders o WHERE o.check_in_date = :checkInDate AND o.hotel_id = :hotelId", nativeQuery = true)
    List<Object[]> findCustomOrders(@Param("checkInDate") java.sql.Date checkInDate, @Param("hotelId") Integer hotelId);



}
