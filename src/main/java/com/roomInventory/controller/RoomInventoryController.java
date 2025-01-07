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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

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
                record.put("roomNum", roomType.getRoomNum()); // 添加固定房間數量
                RoomInventoryVO inventory = inventoryMap.getOrDefault(date, new HashMap<>()).get(roomType.getRoomTypeId());
//                record.put("inventoryId", inventory.getInventoryId()); // 添加 inventoryId
//                record.put("deleteQuantity", inventory.getDeleteQuantity());
//                record.put("availableQuantity", inventory != null ? inventory.getAvailableQuantity() : null); // 如果沒有數據，返回 null
                if (inventory != null) {
                    record.put("inventoryId", inventory.getInventoryId());
                    record.put("deleteQuantity", inventory.getDeleteQuantity());
                    record.put("availableQuantity", inventory.getAvailableQuantity());
                } else {
                    // 如果 inventory 為 null，設置默認值
                    record.put("inventoryId", null);
                    record.put("deleteQuantity", 0);
                    record.put("availableQuantity", null);
                }
                result.add(record);
            }
        }

        return ResponseEntity.ok(result);
    }


    @PutMapping("/update")
    public ResponseEntity<List<RoomInventoryVO>> updateRoomInventories(@RequestBody List<RoomInventoryVO> inventories) {
        System.out.println("收到更新請求: " + inventories); // 打印接收到的更新數據

        if (inventories.isEmpty()) {
            System.out.println("接收到空的更新數據");
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<RoomInventoryVO> updatedInventories = new ArrayList<>();

        for (RoomInventoryVO inventory : inventories) {
            try {
                System.out.println("正在處理房型庫存: " + inventory);

                // 更新庫存數據
                RoomInventoryVO updatedInventory = roomInventoryService.updateRoomInventory(inventory);
                updatedInventories.add(updatedInventory);

                System.out.println("成功更新: " + updatedInventory);
            } catch (Exception e) {
                System.err.println("更新失敗: 房型庫存ID=" + inventory.getInventoryId() + "，錯誤=" + e.getMessage());
                e.printStackTrace(); // 打印錯誤堆疊信息
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
            }
        }

        return ResponseEntity.ok(updatedInventories); // 返回更新後的完整數據
    }
}
