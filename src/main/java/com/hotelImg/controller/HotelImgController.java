package com.hotelImg.controller;

import com.hotelImg.model.HotelImgService;
import com.hotelImg.model.HotelImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hotel-images")
public class HotelImgController {

    @Autowired
    private HotelImgService hotelImgService;

    // 返回酒店圖片 ID 列表
    @GetMapping("/hotel/{hotelId}/images")
    public ResponseEntity<List<Integer>> getHotelImageIds(@PathVariable Integer hotelId) {
        List<Integer> imageIds = hotelImgService.getImagesByHotelId(hotelId)
                .stream()
                .map(HotelImgVO::getHotelImgId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageIds);
    }

    // 返回具體圖片數據
    @GetMapping("/image/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer imageId) {
        HotelImgVO hotelImg = hotelImgService.getImageById(imageId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(hotelImg.getPicture());
    }

    // 新增圖片上傳功能
    @PostMapping("/hotel/{hotelId}/upload")
    public ResponseEntity<?> uploadImages(@PathVariable Integer hotelId,
                                          @RequestParam("photos") List<MultipartFile> photos) {
        try {
            List<HotelImgVO> savedImages = hotelImgService.uploadImages(photos, hotelId);
            return ResponseEntity.ok(savedImages.stream()
                    .map(HotelImgVO::getHotelImgId)
                    .collect(Collectors.toList())); // 回傳儲存的圖片 ID
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("圖片上傳失敗：" + e.getMessage());
        }
    }

    @DeleteMapping("/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable Integer imageId) {
        try {
            hotelImgService.deleteImage(imageId); // 調用 Service 刪除圖片
            return ResponseEntity.ok(Map.of("message", "圖片已刪除")); // 返回 JSON 格式
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "刪除失敗：" + e.getMessage()));
        }
    }
}

