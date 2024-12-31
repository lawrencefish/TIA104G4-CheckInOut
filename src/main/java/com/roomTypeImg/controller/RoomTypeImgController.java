package com.roomTypeImg.controller;

import com.roomTypeImg.model.RoomTypeImgService;
import com.roomTypeImg.model.RoomTypeImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/room-images")
public class RoomTypeImgController {

    @Autowired
    private RoomTypeImgService roomTypeImgService;

    // 獲取某房型的圖片 ID 列表
    @GetMapping("/room/{roomTypeId}/images")
    public ResponseEntity<List<Integer>> getRoomImageIds(@PathVariable Integer roomTypeId) {
        List<Integer> imageIds = roomTypeImgService.findImagesByRoomTypeId(roomTypeId)
                .stream()
                .map(RoomTypeImgVO::getRoomTypeImgId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageIds);
    }

    // 返回具體圖片數據
    @GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer imageId) {
        RoomTypeImgVO roomTypeImg = roomTypeImgService.findById(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 假設圖片為 JPEG 格式
                .body(roomTypeImg.getPicture());
    }

    // 上傳圖片
    @PostMapping("/room/{roomTypeId}/upload")
    public ResponseEntity<?> uploadImages(@PathVariable Integer roomTypeId,
                                          @RequestParam("photos") List<MultipartFile> photos) {
        try {
            List<RoomTypeImgVO> savedImages = roomTypeImgService.uploadImages(photos, roomTypeId);
            return ResponseEntity.ok(savedImages.stream()
                    .map(RoomTypeImgVO::getRoomTypeImgId)
                    .collect(Collectors.toList())); // 回傳儲存的圖片 ID
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗：" + e.getMessage());
        }
    }

    // 刪除圖片
    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer imageId) {
        try {
            roomTypeImgService.deleteImage(imageId); // 調用 Service 刪除圖片
            return ResponseEntity.ok(Map.of("message", "圖片已刪除")); // 返回 JSON 格式
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "刪除失敗：" + e.getMessage()));
        }
    }
}