package com.Lawrencefish.checkin.model;

import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.order.model.OrderVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeRepository;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        // 檢查當前狀態是否允許更新
        Byte currentStatus = order.getStatus();
//        if (currentStatus != 0) { // 0 表示未報到
//            throw new RuntimeException("Order ID " + orderId + " cannot be updated. Current status: " + currentStatus);
//        }
        if (currentStatus == 1) { // 0 表示未報到
            throw new RuntimeException("此訂單已被處理");
        } else if (currentStatus != 0){
            throw new RuntimeException("Order ID " + orderId + " cannot be updated. Current status: " + currentStatus);
        }
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

    @Transactional
    public void processCheckIn(List<CheckInRequest> requests) {
        Set<Integer> processedOrders = new HashSet<>(); // 用來記錄已更新的訂單 ID
        for (CheckInRequest request : requests) {
            // 如果該訂單已處理，跳過狀態更新
            if (!processedOrders.contains(request.getOrderId())) {
                // 更新訂單狀態為 "已報到"
                updateOrderStatus(request.getOrderId(), (byte) 1);
                // 將訂單 ID 記錄為已處理
                processedOrders.add(request.getOrderId());
            }
            // 更新房間狀態為 "已占用"
            updateRoomStatus(request.getAssignedRoomId(), (byte) 1);
            // 更新房間的住客信息
            updateRoomCustomerInfo(
                    request.getAssignedRoomId(),
                    request.getCustomerName(),
                    request.getCustomerPhoneNumber(),
                    request.getOrderDetailId()
            );
        }
    }
}
