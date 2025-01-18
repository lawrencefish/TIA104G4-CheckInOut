package com.admin.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import com.roomTypeImg.model.RoomTypeImgService;
import com.roomTypeImg.model.RoomTypeImgVO;

@Controller
@RequestMapping("adminRoomType")
public class AdminRoomTypeController {

	@Autowired
	private RoomTypeService roomTypeService;
	
	@Autowired
	private RoomTypeImgService roomTypeImgService;
	
    //查詢所有房型
    @GetMapping("/findAllRooms")
    @ResponseBody
    public ResponseEntity<List<RoomTypeVO>> getAllRooms() {
        List<RoomTypeVO> rooms = roomTypeService.findAllRooms();
        
        if(rooms.isEmpty()) {
        	return ResponseEntity.noContent().build();
        	}
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/findRoomsByHotel/{hotelId}")
    @ResponseBody
    public ResponseEntity<List<RoomTypeVO>> getRoomsByHotel(@PathVariable Integer hotelId) {
        List<RoomTypeVO> rooms = roomTypeService.findByHotelId(hotelId);
        return ResponseEntity.ok(rooms);
    }
    
    // 按狀態查詢房型
    @GetMapping("/findRoomsByStatus/{status}")
    @ResponseBody
    public ResponseEntity<List<RoomTypeVO>> findRoomsByStatus(@PathVariable Byte status) {
        List<RoomTypeVO> rooms = roomTypeService.findByStatus(status);
        
        if (rooms.isEmpty()) {
        	return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/findRoom/{roomTypeId}")
    @ResponseBody
    public ResponseEntity<RoomTypeVO> getRoomById(@PathVariable Integer roomTypeId) {
        Optional<RoomTypeVO> room = roomTypeService.findById(roomTypeId);
        return room.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/saveRoom")
    @ResponseBody
    public ResponseEntity<RoomTypeVO> saveRoom(@RequestBody RoomTypeVO roomReview) {
    	RoomTypeVO savedRoom = roomTypeService.saveRoom(roomReview);
        return ResponseEntity.ok(savedRoom);
    }

    @PutMapping("/updateRoomStatus/{roomTypeId}")
    @ResponseBody
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
    @GetMapping("/roomtypeReview/{roomTypeId}")
    public String showRoomTypeReviewPage(@PathVariable Integer roomTypeId, Model model) {
        RoomTypeVO roomType = roomTypeService.getRoomTypeById(roomTypeId);
        if (roomType == null || roomType.getRoomTypeImgs() == null) {
            return "redirect:/error";
        }
        model.addAttribute("roomType", roomType);
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
            roomType.setStatus(status);
            
            // 儲存更新
            RoomTypeVO updatedRoomType = roomTypeService.saveRoom(roomType);
            return ResponseEntity.ok(updatedRoomType);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新審核狀態失敗：" + e.getMessage());
        }
    }
    
    @GetMapping("/image/{imageId}")
    @ResponseBody
    public ResponseEntity<byte[]> getRoomImage(@PathVariable Integer imageId) {
        try {
            // 直接使用 RoomTypeImgVO
            RoomTypeImgVO image = roomTypeImgService.findById(imageId);
            if (image != null && image.getPicture() != null) {
                return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(image.getPicture());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/firstImage/{roomTypeId}")
    public ResponseEntity<byte[]> getFirstRoomTypeImage(@PathVariable Integer roomTypeId) {
        try {
            byte[] imageBytes = roomTypeImgService.getFirstImageByRoomTypeId(roomTypeId);
            if (imageBytes != null && imageBytes.length > 0) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    //---------------------------------------------
}
