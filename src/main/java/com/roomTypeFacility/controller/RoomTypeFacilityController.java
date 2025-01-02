package com.roomTypeFacility.controller;

import com.facility.model.FacilityVO;
import com.roomTypeFacility.model.RoomTypeFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roomTypeFacilities")
public class RoomTypeFacilityController {

    @Autowired
    private RoomTypeFacilityService roomTypeFacilityService;

    /**
     * 根據房型 ID 獲取設施
     * @param roomTypeId 房型 ID
     * @return 該房型的設施列表
     */
    @GetMapping("/{roomTypeId}/facilities")
    public ResponseEntity<List<FacilityVO>> getFacilitiesByRoomType(@PathVariable Integer roomTypeId) {
        List<FacilityVO> facilities = roomTypeFacilityService.getFacilitiesOrServicesByRoomType(roomTypeId, 0);
        return ResponseEntity.ok(facilities);
    }

    /**
     * 根據房型 ID 獲取服務
     * @param roomTypeId 房型 ID
     * @return 該房型的服務列表
     */
    @GetMapping("/{roomTypeId}/services")
    public ResponseEntity<List<FacilityVO>> getServicesByRoomType(@PathVariable Integer roomTypeId) {
        List<FacilityVO> services = roomTypeFacilityService.getFacilitiesOrServicesByRoomType(roomTypeId, 1);
        return ResponseEntity.ok(services);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updateFacilitiesAndServices(
            @RequestBody Map<String, Object> payload) {
        try {
            // 從請求中提取房型 ID、設施 ID 和服務 ID
            Integer roomTypeId = (Integer) payload.get("roomTypeId");
            List<Integer> facilityIds = (List<Integer>) payload.getOrDefault("facilities", Collections.emptyList());
            List<Integer> serviceIds = (List<Integer>) payload.getOrDefault("services", Collections.emptyList());

            // 調用服務層方法更新設施與服務
            roomTypeFacilityService.updateFacilitiesAndServices(roomTypeId, facilityIds, serviceIds);

            // 返回成功響應
            Map<String, String> response = new HashMap<>();
            response.put("message", "房型設施與服務更新成功！");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 返回失敗響應
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "更新失敗：" + e.getMessage()));
        }
    }
}