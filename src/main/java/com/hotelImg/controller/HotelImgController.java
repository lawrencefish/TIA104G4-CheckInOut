package com.hotelImg.controller;

import com.hotelImg.model.HotelImgService;
import com.hotelImg.model.HotelImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
}

