package com.roomInventory.controller;

import com.hotel.model.HotelVO;
import com.roomInventory.model.RoomInventoryService;
import com.roomInventory.model.RoomInventoryVO;
import com.roomType.model.RoomTypeService;
import com.roomType.model.RoomTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roomInventory")
public class RoomInventoryController {
    @Autowired
    RoomInventoryService roomInventoryService;
    @Autowired
    RoomTypeService roomTypeService;

    @GetMapping("/full")
    public ResponseEntity<List<Map<String, Object>>> getFullRoomInventory(
            HttpSession session,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusDays(60);
        }

        // 1. 從 Session 獲取 Hotel 資料，或通過 hotelId 查詢
        HotelVO hotel = (HotelVO) session.getAttribute("hotel");

        // 2. 獲取該 Hotel 的所有房型
        List<RoomTypeVO> roomTypes = roomTypeService.findByHotelId(hotel.getHotelId());

        // 3. 用所有房型的 roomTypeId 查詢 RoomInventory 資料（範圍查詢）
        List<RoomInventoryVO> inventories = roomInventoryService.findByRoomTypesAndDateRange(roomTypes, startDate, endDate);

        // 4. 構造返回數據
        Map<LocalDate, Map<Integer, RoomInventoryVO>> inventoryMap = new HashMap<>();
        for (RoomInventoryVO inventory : inventories) {
            inventoryMap
                    .computeIfAbsent(inventory.getDate(), k -> new HashMap<>())
                    .put(inventory.getRoomType().getRoomTypeId(), inventory);
        }

        // 5. 創建完整的返回結構
        List<Map<String, Object>> result = new ArrayList<>();
        for (RoomTypeVO roomType : roomTypes) {
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                Map<String, Object> record = new HashMap<>();
                record.put("date", date);
                record.put("roomTypeId", roomType.getRoomTypeId());
                record.put("roomName", roomType.getRoomName());
                RoomInventoryVO inventory = inventoryMap.getOrDefault(date, new HashMap<>()).get(roomType.getRoomTypeId());
                record.put("availableQuantity", inventory != null ? inventory.getAvailableQuantity() : null); // 如果沒有數據，返回 null
                result.add(record);
            }
        }

        return ResponseEntity.ok(result);
    }

}
