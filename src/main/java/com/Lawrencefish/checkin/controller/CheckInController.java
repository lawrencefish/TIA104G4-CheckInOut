package com.Lawrencefish.checkin.controller;

import com.Lawrencefish.checkin.model.CheckInRequest;
import com.Lawrencefish.checkin.model.CheckInService;
import com.order.model.OrderRepository;
import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailVO;
import com.room.model.RoomRepository;
import com.room.model.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/checkIn")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @GetMapping("/orderDetails")
    public ResponseEntity<?> getOrderDetails(@RequestParam("orderId") Integer orderId) {
        // 1. 查詢訂單基本信息
        OrderVO order = checkInService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }

        // 2. 構建返回數據
        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getOrderId());
        response.put("memberName", order.getGuestLastName() + order.getGuestFirstName());
        response.put("checkInDate", order.getCheckInDate());
        response.put("checkOutDate", order.getCheckOutDate());
        response.put("memo", order.getMemo());

        // 3. 查詢訂單明細並附加可用房間
        List<Map<String, Object>> detailsList = new ArrayList<>();
        for (OrderDetailVO detail : order.getOrderDetail()) {
            Map<String, Object> detailMap = new HashMap<>();
            detailMap.put("orderDetailId", detail.getOrderDetailId());
            detailMap.put("roomTypeId", detail.getRoomTypeId());
            detailMap.put("roomNum", detail.getRoomNum());
            detailMap.put("breakfast", detail.getBreakfast());

            // 在查詢訂單明細時添加房型名稱
            String roomTypeName = checkInService.getRoomTypeNameById(detail.getRoomTypeId());
            detailMap.put("roomTypeName", roomTypeName);

            // 查詢可用房間並處理空值
            List<RoomVO> availableRooms = checkInService.getAvailableRoomsByRoomTypeId(detail.getRoomTypeId());
            if (availableRooms == null || availableRooms.isEmpty()) {
                detailMap.put("availableRooms", new ArrayList<>());
            } else {
                List<Map<String, Object>> roomList = availableRooms.stream()
                        .map(room -> {
                            Map<String, Object> roomMap = new HashMap<>();
                            roomMap.put("roomId", room.getRoomId()); // 新增 roomId
                            roomMap.put("roomNumber", room.getNumber() != null ? room.getNumber() : "未提供");
                            return roomMap;
                        })
                        .collect(Collectors.toList());
                detailMap.put("availableRooms", roomList);
            }

            detailsList.add(detailMap);
        }

        response.put("orderDetails", detailsList);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/saveCheckInDetails")
    public ResponseEntity<?> saveCheckInDetails(@RequestBody List<CheckInRequest> requests) {
        try {
            System.out.println("Received requests: " + requests);

            for (CheckInRequest request : requests) {
                System.out.println("Processing request for Order ID: " + request.getOrderId() +
                        ", Room ID: " + request.getAssignedRoomId() +
                        ", Customer Name: " + request.getCustomerName() +
                        ", Customer Phone: " + request.getCustomerPhoneNumber());

                // 更新訂單狀態為 "已報到"
                System.out.println("Updating order status for Order ID: " + request.getOrderId());
                checkInService.updateOrderStatus(request.getOrderId(), (byte) 1);

                // 更新房間狀態為 "已占用"
                System.out.println("Updating room status for Room ID: " + request.getAssignedRoomId());
                checkInService.updateRoomStatus(request.getAssignedRoomId(), (byte) 1);

                // 更新住客信息到 room 表
                System.out.println("Updating room customer info for Room ID: " + request.getAssignedRoomId());
                // 更新房間的住客信息並寫入 OrderDetailId
                checkInService.updateRoomCustomerInfo(
                        request.getAssignedRoomId(),
                        request.getCustomerName(),
                        request.getCustomerPhoneNumber(),
                        request.getOrderDetailId() // 新增此參數
                );
            }

            System.out.println("All requests processed successfully.");
            return ResponseEntity.ok("Check-in details saved successfully.");
        } catch (Exception e) {
            System.out.println("Error occurred while saving check-in details: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving check-in details: " + e.getMessage());
        }
    }

    @GetMapping("/getArrivedDetails")
    public ResponseEntity<?> getArrivedDetails(@RequestParam("orderId") Integer orderId) {
        try {
            // 查詢訂單基本信息
            OrderVO order = checkInService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order not found"));
            }

            // 構建返回數據
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("memberName", order.getGuestLastName() + order.getGuestFirstName());
            response.put("checkInDate", order.getCheckInDate());
            response.put("checkOutDate", order.getCheckOutDate());
            response.put("memo", order.getMemo());

            // 查詢已報到訂單明細
            List<Map<String, Object>> detailsList = new ArrayList<>();
            for (OrderDetailVO detail : order.getOrderDetail()) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("orderDetailId", detail.getOrderDetailId());
                detailMap.put("roomTypeId", detail.getRoomTypeId());
                detailMap.put("roomNum", detail.getRoomNum());
                detailMap.put("guestNum", detail.getGuestNum()); // 新增人數欄位
                detailMap.put("breakfast", detail.getBreakfast());

                // 查詢房型名稱
                String roomTypeName = checkInService.getRoomTypeNameById(detail.getRoomTypeId());
                detailMap.put("roomTypeName", roomTypeName);

                // 查詢分配的房間信息
                RoomVO room = checkInService.getRoomByOrderDetailId(detail.getOrderDetailId());
                detailMap.put("roomId", room.getRoomId()); // 添加房間 ID
                detailMap.put("assignedRoomNumber", room.getNumber());
                detailMap.put("customerName", room.getCustomerName());
                detailMap.put("customerPhoneNumber", room.getCustomerPhoneNumber());

                detailsList.add(detailMap);
            }

            response.put("orderDetails", detailsList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 返回 JSON 格式的錯誤消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error retrieving arrived details", "message", e.getMessage()));
        }
    }

    @GetMapping("/getCheckedOutDetails")
    public ResponseEntity<?> getCheckedOutDetails(@RequestParam("orderId") Integer orderId) {
        try {
            // 查詢訂單基本信息
            OrderVO order = checkInService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order not found"));
            }

            // 構建返回數據
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.getOrderId());
            response.put("memberName", order.getGuestLastName() + order.getGuestFirstName());
            response.put("checkInDate", order.getCheckInDate());
            response.put("checkOutDate", order.getCheckOutDate());
            response.put("memo", order.getMemo());

            // 查詢退房訂單明細
            List<Map<String, Object>> detailsList = new ArrayList<>();
            for (OrderDetailVO detail : order.getOrderDetail()) {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put("orderDetailId", detail.getOrderDetailId());
                detailMap.put("roomTypeId", detail.getRoomTypeId());
                detailMap.put("roomNum", detail.getRoomNum());
                detailMap.put("guestNum", detail.getGuestNum()); // 添加人數欄位
                detailMap.put("breakfast", detail.getBreakfast());

                // 查詢房型名稱
                String roomTypeName = checkInService.getRoomTypeNameById(detail.getRoomTypeId());
                detailMap.put("roomTypeName", roomTypeName);

                detailsList.add(detailMap);
            }

            response.put("orderDetails", detailsList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 返回 JSON 格式的錯誤消息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error retrieving checked-out details", "message", e.getMessage()));
        }
    }

}