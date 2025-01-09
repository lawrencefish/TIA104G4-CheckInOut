package com.price.controller;

import com.hotel.model.HotelVO;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/price")
public class PriceController {
    @Autowired
    private PriceService priceService;
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/prices/{roomTypeId}")
    public ResponseEntity<List<PriceVO>> getPricesByRoomType(@PathVariable Integer roomTypeId) {
        List<PriceVO> prices = priceService.getPricesByRoomTypeId(roomTypeId);
        return ResponseEntity.ok(prices);
    }

    @PostMapping("/{roomTypeId}/add")
    public ResponseEntity<String> addPrices(
            @PathVariable Integer roomTypeId,
            @RequestBody List<PriceVO> priceVOList) {
        try {
            priceService.addPrices(roomTypeId, priceVOList); // 調用 Service
            return ResponseEntity.ok("所有價格新增成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("價格新增失敗：" + e.getMessage());
        }
    }

    // 更新房型價格
    @PutMapping("/prices/{roomTypeId}")
    public ResponseEntity<String> updatePrices(
            @PathVariable Integer roomTypeId,
            @RequestBody List<PriceVO> priceVOs) {
        try {
            priceService.updatePrices(roomTypeId, priceVOs);
            return ResponseEntity.ok("價格更新成功！");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("價格更新失敗：" + e.getMessage());
        }
    }

    // 新增特殊價格
    @PostMapping("/special/{roomTypeId}")
    public ResponseEntity<String> addSpecialPrice(
            @PathVariable Integer roomTypeId,
            @RequestBody PriceVO priceVO) {
        try {
            priceService.addSpecialPrice(roomTypeId, priceVO);
            return ResponseEntity.ok("特殊价格已成功新增");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("新增特殊价格失败：" + e.getMessage());
        }
    }

    // 获取所有特殊价格
    // 获取所有特殊价格
    @GetMapping("/specials")
    public ResponseEntity<List<PriceVO>> getSpecialPricesBySession(HttpSession session) {
        // 从 Session 获取当前酒店信息
        HotelVO hotel = (HotelVO) session.getAttribute("hotel");
        if (hotel == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        // 查询当前酒店的房型
        List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotel.getHotelId());
        if (roomTypes.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // 获取房型的 ID 列表
        List<Integer> roomTypeIds = roomTypes.stream()
                .map(RoomTypeVO::getRoomTypeId)
                .collect(Collectors.toList());

        // 查询这些房型的特殊价格
        List<PriceVO> specialPrices = priceService.findSpecialPricesByRoomTypeIds(roomTypeIds);

        return ResponseEntity.ok(specialPrices);
    }

    // 删除特殊价格
    @DeleteMapping("/special/{priceId}")
    public ResponseEntity<String> deleteSpecialPrice(@PathVariable Integer priceId) {
        try {
            priceService.deleteSpecialPrice(priceId);
            return ResponseEntity.ok("删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除失败：" + e.getMessage());
        }
    }
}
