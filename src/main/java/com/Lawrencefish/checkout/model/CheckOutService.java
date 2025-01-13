package com.Lawrencefish.checkout.model;

import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.order.model.OrderVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
