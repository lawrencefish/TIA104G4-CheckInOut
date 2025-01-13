package com.Lawrencefish.roomStatus.model;

import com.Lawrencefish.OrderDetail.model.OrderDetailRepositoryByTom;
import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.order.model.OrderRepository;
import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailRepository;
import com.orderDetail.model.OrderDetailVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomStatusService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Map<String, Object> getRoomDetails(Integer roomId) {
        // 查詢 Room
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for ID: " + roomId));

        // 確認是否有分配的 orderDetailId
        if (room.getOrderDetailId() == null) {
            throw new RuntimeException("No active order detail found for this room.");
        }

        // 查詢 OrderDetail
        OrderDetailVO orderDetail = orderDetailRepository.findById(room.getOrderDetailId())
                .orElseThrow(() -> new RuntimeException("Order detail not found for ID: " + room.getOrderDetailId()));

        // 獲取 Order
        OrderVO order = orderDetail.getOrder();

        // 構建回傳資料
        Map<String, Object> response = new HashMap<>();
        response.put("roomId", room.getRoomId());
        response.put("roomNumber", room.getNumber());
        response.put("customerName", room.getCustomerName()); // 添加住客姓名
        response.put("customerPhoneNumber", room.getCustomerPhoneNumber()); // 添加住客電話
        response.put("orderId", order.getOrderId());
        response.put("memberId", order.getMember().getMemberId());
        response.put("memberName", order.getMember().getLastName() + order.getMember().getFirstName());
        response.put("memo", order.getMemo());

        // 添加 OrderDetail 資料
        response.put("orderDetails", List.of(Map.of(
                "orderDetailId", orderDetail.getOrderDetailId(),
                "roomTypeId", orderDetail.getRoomTypeId(),
                "guestNum", orderDetail.getGuestNum(),
                "breakfast", orderDetail.getBreakfast()
        )));

        return response;
    }

    public void updateRoomStatus(Integer roomId, Integer status) {
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for ID: " + roomId));

        room.setStatus(status.byteValue());
        roomRepository.save(room);
    }
}
