package com.Lawrencefish.checkin.model;

import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.order.model.OrderVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service()
public class CheckInService {
    @Autowired
    private OrderRepositoryByTom orderRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    /**
     * 根據訂單 ID 查詢訂單基本信息。
     *
     * @param orderId 訂單 ID
     * @return 查詢到的訂單或 null（如果不存在）
     */
    public OrderVO getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * 查詢指定房型的可用房間列表。
     *
     * @param roomTypeId 房型 ID
     * @return 該房型的可用房間列表
     */
    public List<RoomVO> getAvailableRoomsByRoomTypeId(Integer roomTypeId) {
        return roomRepository.findAvailableRoomsByRoomTypeId(roomTypeId);
    }

    // 查詢房型名稱
    public String getRoomTypeNameById(Integer roomTypeId) {
        return roomTypeRepository.findById(roomTypeId)
                .map(RoomTypeVO::getRoomName)
                .orElse("未知房型");
    }

    public void updateOrderStatus(Integer orderId, Byte status) {
        OrderVO order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status); // 將訂單狀態設為 1（已報到）
        orderRepository.save(order);
    }

    public void updateRoomStatus(Integer roomId, Byte status) {
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setStatus(status); // 將房間狀態設為 1（已占用）
        roomRepository.save(room);
    }

    public void updateRoomCustomerInfo(Integer roomId, String customerName, String customerPhoneNumber, Integer orderDetailId) {
        // 查詢對應的 RoomVO
        RoomVO room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 更新住客姓名和電話
        room.setCustomerName(customerName);
        room.setCustomerPhoneNumber(customerPhoneNumber);
        // 更新房間的 OrderDetailId
        room.setOrderDetailId(orderDetailId);

        // 保存更新
        roomRepository.save(room);
    }

    public RoomVO getRoomByOrderDetailId(Integer orderDetailId) {
        return roomRepository.findByOrderDetailId(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Room not found for the given order detail ID"));
    }
}
