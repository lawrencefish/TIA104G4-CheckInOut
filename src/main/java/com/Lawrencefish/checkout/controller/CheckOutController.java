package com.Lawrencefish.checkout.controller;

import com.Lawrencefish.checkin.model.CheckInService;
import com.Lawrencefish.checkout.model.CheckOutRequest;
import com.Lawrencefish.checkout.model.CheckOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkOut")
public class CheckOutController {

    @Autowired
    private CheckOutService checkOutService;

//    @PostMapping("/saveCheckOutDetails")
//    public ResponseEntity<?> saveCheckOutDetails(@RequestBody CheckOutRequest checkOutRequest) {
//        try {
//            // 驗證輸入數據
//            if (checkOutRequest.getRoomIds() == null || checkOutRequest.getRoomIds().isEmpty()) {
//                return ResponseEntity.badRequest().body(Map.of("error", "Room IDs cannot be null or empty."));
//            }
//
//            // 更新訂單狀態
//            checkOutService.updateOrderStatus(checkOutRequest.getOrderId(), (byte) 2);
//
//            // 處理每個房間
//            for (Integer roomId : checkOutRequest.getRoomIds()) {
//                if (roomId == null) {
//                    throw new IllegalArgumentException("Room ID cannot be null.");
//                }
//                checkOutService.updateRoomStatus(roomId, (byte) 0); // 更新房間狀態為空房
//                checkOutService.clearRoomCustomerInfo(roomId); // 清空房間住客信息
//            }
//
//            return ResponseEntity.ok(Map.of("message", "Check-out details saved successfully."));
//        } catch (IllegalArgumentException e) {
//            // 處理非法參數
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            // 處理其他異常
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Error saving check-out details", "message", e.getMessage()));
//        }
//    }

    @PostMapping("/saveCheckOutDetails")
    public ResponseEntity<?> saveCheckOutDetails(@RequestBody CheckOutRequest checkOutRequest) {
        try {
            // 執行 Check-out 處理
            checkOutService.processCheckOut(checkOutRequest);

            // 返回成功的 JSON 響應
            return ResponseEntity.ok(Map.of(
                    "message", "Check-out details saved successfully."
            ));
        } catch (RuntimeException e) {
            // 返回 BAD_REQUEST 錯誤的 JSON 響應
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            // 返回 INTERNAL_SERVER_ERROR 錯誤的 JSON 響應
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "Error saving check-out details",
                    "details", e.getMessage()
            ));
        }
    }
}
