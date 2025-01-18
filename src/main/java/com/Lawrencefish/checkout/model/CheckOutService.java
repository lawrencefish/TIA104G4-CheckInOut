package com.Lawrencefish.checkout.model;

import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.Lawrencefish.websocket.NotificationWebSocketHandler;
import com.order.model.OrderVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckOutService {
    @Autowired
    private OrderRepositoryByTom orderRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    public void updateOrderStatus(Integer orderId, Byte status) {
        OrderVO order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
        System.out.println("Order status updated to " + status + " for Order ID: " + orderId);
    }

    public void updateRoomStatus(Integer roomId, Byte status) {
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for ID: " + roomId));
        room.setStatus(status);
        roomRepository.save(room);
        System.out.println("Room status updated to " + status + " for Room ID: " + roomId);
    }

    public void clearRoomCustomerInfo(Integer roomId) {
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found for ID: " + roomId));
        room.setCustomerName(null);
        room.setCustomerPhoneNumber(null);
        room.setOrderDetailId(null);
        roomRepository.save(room);
        System.out.println("Room customer info cleared for Room ID: " + roomId);
    }

    @Transactional
    public void processCheckOut(CheckOutRequest checkOutRequest) {
        // 1. 驗證訂單狀態是否為 "已報到" (1)
        OrderVO order = orderRepository.findById(checkOutRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + checkOutRequest.getOrderId()));
//        if (order.getStatus() != 1) {
//            throw new RuntimeException("Order ID " + checkOutRequest.getOrderId() + " is not in a valid state for check-out.");
//        }
        if (order.getStatus() == 2) {
            throw new RuntimeException("此訂單已被處理");
        } else if (order.getStatus() != 1) {
            throw new RuntimeException("Order ID " + checkOutRequest.getOrderId() + " is not in a valid state for check-out.");
        }

        // 2. 更新訂單狀態為 "已退房" (2)
        order.setStatus((byte) 2);
        orderRepository.save(order);
        // 推播訂單狀態更新
        String orderMessage = String.format(
                "{\"type\": \"checkout\", \"orderId\": %d, \"status\": %d}",
                order.getOrderId(),
                order.getStatus()
        );
        NotificationWebSocketHandler.broadcast(orderMessage);

        // 處理每個房間的狀態更新和清空住客信息
        List<Integer> roomIds = checkOutRequest.getRoomIds();
        if (roomIds == null || roomIds.isEmpty()) {
            throw new RuntimeException("Room IDs cannot be null or empty.");
        }

        for (Integer roomId : roomIds) {
            RoomVO room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found for ID: " + roomId));

            // 更新房間狀態為 "空房" (0)
            room.setStatus((byte) 0);

            // 清空住客信息
            room.setCustomerName(null);
            room.setCustomerPhoneNumber(null);
            room.setOrderDetailId(null);

            roomRepository.save(room);

            // 推播房間狀態更新
            String roomMessage = String.format(
                    "{\"type\": \"roomStatus\", \"roomId\": %d, \"status\": %d}",
                    room.getRoomId(),
                    room.getStatus()
            );
            NotificationWebSocketHandler.broadcast(roomMessage);
        }
    }
}
