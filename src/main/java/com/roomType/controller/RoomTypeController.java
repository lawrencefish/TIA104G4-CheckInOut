package com.roomType.controller;

import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/roomType")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @PutMapping("/update/{roomTypeId}") // 修正路徑和方法
    public ResponseEntity<Map<String, String>> updateRoomType(
            @PathVariable Integer roomTypeId,
            @RequestBody RoomTypeVO roomTypeVO) {
        try {
            roomTypeService.updateRoomType(roomTypeId, roomTypeVO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "房型資料更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}