package com.Lawrencefish.checkin.model;

import com.Lawrencefish.order.model.OrderRepositoryByTom;
import com.Lawrencefish.websocket.NotificationWebSocketHandler;
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
        Set<Integer> processedOrders = new HashSet<>(); // 用来记录已更新的订单 ID

        for (CheckInRequest request : requests) {
            // 如果该订单尚未处理，更新其状态
            if (!processedOrders.contains(request.getOrderId())) {
                updateOrderStatus(request.getOrderId(), (byte) 1); // 订单状态设为已报到
                processedOrders.add(request.getOrderId());

                // 推播 Check-In 订单状态更新
                String checkInMessage = String.format(
                        "{\"type\": \"checkIn\", \"orderId\": %d, \"status\": %d}",
                        request.getOrderId(),
                        1
                );
                NotificationWebSocketHandler.broadcast(checkInMessage); // 推播
            }

            // 处理单个分配的房间
            Integer assignedRoomId = request.getAssignedRoomId();
            String customerName = request.getCustomerName();
            String customerPhoneNumber = request.getCustomerPhoneNumber();

            if (assignedRoomId != null) {
                // 更新房间状态为 "已占用"
                updateRoomStatus(assignedRoomId, (byte) 1);
                // 更新房间的住客信息
                updateRoomCustomerInfo(
                        assignedRoomId,
                        customerName,
                        customerPhoneNumber,
                        request.getOrderDetailId()
                );

                // 推播房间状态更新
                String roomMessage = String.format(
                        "{\"type\": \"roomStatus\", \"roomId\": %d, \"status\": %d}",
                        assignedRoomId,
                        1
                );
                NotificationWebSocketHandler.broadcast(roomMessage); // 推播给其他用户
            } else {
                throw new IllegalArgumentException("Assigned room ID cannot be null.");
            }
        }
    }



    public List<RoomVO> getRoomsByOrderDetailId(Integer orderDetailId) {
        return roomRepository.findRoomsByOrderDetailId(orderDetailId);
    }
}
