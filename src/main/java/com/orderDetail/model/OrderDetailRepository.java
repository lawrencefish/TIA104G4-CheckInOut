package com.orderDetail.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OrderDetailRepository extends JpaRepository<OrderDetailVO, Integer> {
    @Query("SELECT new com.orderDetail.model.OrderDetailDTO( " +
            "od.orderDetailId, od.guestNum, od.roomNum, od.breakfast, " +
            "od.roomTypeId, rt.roomName, rt.maxPerson) " +
            "FROM OrderDetailVO od " +
            "LEFT JOIN RoomTypeVO rt ON od.roomTypeId = rt.roomTypeId " +  // 額外查詢 roomType
            "WHERE od.order.orderId = :orderId")
     List<OrderDetailDTO> findOrderDetailsByOrderId(@Param("orderId") Integer orderId);

}
