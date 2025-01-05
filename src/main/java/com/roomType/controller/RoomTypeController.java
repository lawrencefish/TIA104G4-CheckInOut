package com.roomType.controller;

import com.hotel.model.HotelVO;
import com.room.model.RoomService;
import com.room.model.RoomVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
//        System.out.println("Fetched room types: " + roomTypes); // 日誌輸出
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

    @GetMapping("/{roomTypeId}")
    public ResponseEntity<RoomTypeVO> getRoomTypeById(@PathVariable Integer roomTypeId) {
        RoomTypeVO roomType = roomTypeService.getRoomTypeById(roomTypeId);
        if (roomType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(roomType);
    }
    
    // 房型審核用 -By Barry
    //查詢所有房型
    @GetMapping("/findAllRooms")
    public ResponseEntity<List<RoomTypeVO>> getAllRooms() {
        List<RoomTypeVO> rooms = roomTypeService.findAllRooms();
        
        if(rooms.isEmpty()) {
        	return ResponseEntity.noContent().build();
        	}
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/findRoomsByHotel/{hotelId}")
    public ResponseEntity<List<RoomTypeVO>> getRoomsByHotel(@PathVariable Integer hotelId) {
        List<RoomTypeVO> rooms = roomTypeService.findByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
    
    // 按狀態查詢房型
    @GetMapping("/findRoomsByStatus/{status}")
    public ResponseEntity<List<RoomTypeVO>> findRoomsByStatus(@PathVariable Byte status) {
        List<RoomTypeVO> rooms = roomTypeService.findByStatus(status);
        
        if (rooms.isEmpty()) {
        	return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/findRoom/{roomTypeId}")
    public ResponseEntity<RoomTypeVO> getRoomById(@PathVariable Integer roomTypeId) {
        Optional<RoomTypeVO> room = roomTypeService.findById(roomTypeId);
        return room.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/saveRoom")
    public ResponseEntity<RoomTypeVO> saveRoom(@RequestBody RoomTypeVO roomReview) {
    	RoomTypeVO savedRoom = roomTypeService.saveRoom(roomReview);
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping("/updateRoomStatus/{roomTypeId}")
    public ResponseEntity<RoomTypeVO> updateRoomStatus(
            @PathVariable Integer roomTypeId,
            @RequestParam Byte status) {
    	RoomTypeVO updatedRoom = roomTypeService.updateStatus(roomTypeId, status);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        }
        return ResponseEntity.notFound().build();
    }
    
    // 導向房型審核頁面
    @GetMapping("/roomtypeReview")
    public String showRoomTypeReviewPage(@PathVariable Integer roomTypeId, Model model) {
        RoomTypeVO roomType = roomTypeService.getRoomTypeById(roomTypeId);
        if (roomType != null) {
            model.addAttribute("roomType", roomType);
        }
        return "admin/roomtype-review";  // 返回 HTML 頁面
    }

    // 獲取特定房型的詳細資訊
    @GetMapping("/detail/{roomTypeId}")
    @ResponseBody
    public ResponseEntity<?> getRoomTypeDetails(@PathVariable Integer roomTypeId) {
        try {
            RoomTypeVO roomType = roomTypeService.getRoomTypeById(roomTypeId);
            if (roomType == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(roomType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("獲取房型資料失敗：" + e.getMessage());
        }
    }

    // 更新房型審核狀態
    @PutMapping("/{roomTypeId}/review")
    @ResponseBody
    public ResponseEntity<?> updateRoomTypeStatus(
            @PathVariable Integer roomTypeId,
            @RequestParam Byte status,
            @RequestParam(required = false) String reviewComment) {
        try {
            RoomTypeVO roomType = roomTypeService.getRoomTypeById(roomTypeId);
            if (roomType == null) {
                return ResponseEntity.notFound().build();
            }

            // 更新狀態和審核評論
//            roomType.setStatus(status);
//            if (reviewComment != null && !reviewComment.trim().isEmpty()) {
//                roomType.setReviewComment(reviewComment);
//            }
            
            // 儲存更新
            RoomTypeVO updatedRoomType = roomTypeService.saveRoom(roomType);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新審核狀態失敗：" + e.getMessage());
        }
    }
    //---------------------------------------------
}