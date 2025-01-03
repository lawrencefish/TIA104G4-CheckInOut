package com.roomType.controller;

import com.hotel.model.HotelVO;
import com.room.model.RoomService;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roomType")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private RoomService roomService;

    @GetMapping("/roomTypes")
    public ResponseEntity<List<RoomTypeVO>> getRoomTypesBySession(HttpSession session) {
        // 從 Session 中取得當前酒店資訊
        HotelVO hotel = (HotelVO) session.getAttribute("hotel");
        if (hotel == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 查詢該酒店的房型
        List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotel.getHotelId());
        System.out.println("Fetched room types: " + roomTypes); // 日誌輸出
        return ResponseEntity.ok(roomTypes);
    }


    // 新增房型
    @PostMapping("/create")
    public ResponseEntity<?> createRoomType(HttpSession session, @RequestBody RoomTypeVO roomTypeVO) {
        try {
            // 從 Session 中獲取當前飯店
            HotelVO hotel = (HotelVO) session.getAttribute("hotel");
            if (hotel == null) {
                return ResponseEntity.badRequest().body("未登入或飯店資訊缺失");
            }

            // 綁定飯店到房型
            roomTypeVO.setHotel(hotel);

            // 保存房型
            RoomTypeVO savedRoomType = roomTypeService.createRoomType(hotel, roomTypeVO);

            // 根據房間數量生成 RoomVO
            List<RoomVO> rooms = new ArrayList<>();
            for (int i = 1; i <= roomTypeVO.getRoomNum(); i++) {
                RoomVO room = new RoomVO();
                room.setRoomType(savedRoomType); // 綁定到新建的房型
                room.setNumber(i); // 設定房間編號
                room.setStatus((byte) 0); // 預設為可用
                rooms.add(room);
            }

            // 保存房間
            roomService.saveAll(rooms);

            return ResponseEntity.ok(savedRoomType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{roomTypeId}")
    public ResponseEntity<Map<String, String>> updateRoomType(
            @PathVariable Integer roomTypeId,
            @RequestBody RoomTypeVO roomTypeVO) {
        try {
            // 更新房型資料，包括 status
            roomTypeService.updateRoomType(roomTypeId, roomTypeVO);

            // 成功回應
            Map<String, String> response = new HashMap<>();
            response.put("message", "房型資料更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 錯誤回應
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "更新失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/{roomTypeId}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer roomTypeId) {
        roomTypeService.deleteRoomType(roomTypeId);
        return ResponseEntity.noContent().build();
    }
}