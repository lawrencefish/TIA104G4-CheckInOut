package com.Lawrencefish.roomStatus.controller;

import com.Lawrencefish.roomStatus.model.RoomStatusService;
import com.order.model.OrderService;
import com.order.model.OrderVO;
import com.room.model.RoomService;
import com.room.model.RoomVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roomStatus")
public class RoomStatusController {

    @Autowired
    private RoomStatusService roomStatusService;

    @GetMapping("/roomDetails")
    public ResponseEntity<?> getRoomDetails(@RequestParam("roomId") Integer roomId) {
        try {
            Map<String, Object> roomDetails = roomStatusService.getRoomDetails(roomId);
            return ResponseEntity.ok(roomDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/updateRoomStatus")
    public ResponseEntity<?> updateRoomStatus(@RequestBody Map<String, Object> request) {
        try {
            Integer roomId = (Integer) request.get("roomId");
            Integer status = (Integer) request.get("status");

            roomStatusService.updateRoomStatus(roomId, status);
            return ResponseEntity.ok(Map.of("message", "房間狀態更新成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
