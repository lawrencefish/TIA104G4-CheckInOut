package com.Lawrencefish.order.controller;

import com.Lawrencefish.order.model.OrderServiceByTom;
import com.hotel.model.HotelVO;
import com.order.model.OrderService;
import com.order.model.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderControllerByTom {
    @Autowired
    private OrderServiceByTom orderService;

    @GetMapping("/today/checkin")
    public ResponseEntity<List<Map<String, Object>>> getTodayOrders(@RequestParam Integer hotelId) {
        List<Map<String, Object>> todayOrders = orderService.getTodayOrders(hotelId);
        return ResponseEntity.ok(todayOrders);
    }

    @GetMapping("/today/checkout")
    public ResponseEntity<List<Map<String, Object>>> getTodayCheckoutOrders(@RequestParam Integer hotelId) {
        List<Map<String, Object>> todayCheckoutOrders = orderService.getTodayCheckoutOrders(hotelId);
        return ResponseEntity.ok(todayCheckoutOrders);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllOrdersWithMemberInfo(@RequestParam("hotelId") Integer hotelId) {
        List<Map<String, Object>> orders = orderService.getOrdersWithMemberInfo(hotelId);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchOrders(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session) {
        try {
            // 從 Session 中取得 hotelId
            HotelVO hotelVO = (HotelVO) session.getAttribute("hotel");
            Integer hotelId = hotelVO.getHotelId();

            // 確保至少有一個參數存在
            if ((date == null || date.isEmpty()) && (keyword == null || keyword.isEmpty())) {
                return ResponseEntity.badRequest().body("至少需要提供日期或關鍵字");
            }

            // 調用 Service 查詢
            List<Map<String, Object>> results = orderService.searchOrders(hotelId, date, keyword);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查詢失敗，原因：" + e.getMessage()));
        }
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Integer orderId, HttpSession session) {
        try {
            // 從 Session 中取得 hotelId
//            System.out.println("有進來");
            HotelVO hotelVO = (HotelVO) session.getAttribute("hotel");
//            if (hotelVO == null) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "未授權存取"));
//            }
            Integer hotelId = hotelVO.getHotelId();
//            System.out.println(hotelId);
            // 獲取訂單詳情
            Map<String, Object> orderDetails = orderService.getOrderDetails(orderId);

            // 檢查訂單是否屬於當前飯店
            Integer orderHotelId = ((HotelVO) orderDetails.get("hotel")).getHotelId();
//            System.out.println(orderHotelId);
            if (!orderHotelId.equals(hotelId)) {
//                System.out.println("123");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "無權查詢其他飯店的訂單"));
            }

//            System.out.println("ok");
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer orderId = requestBody.get("orderId");

            // 調用服務層方法取消訂單並恢復庫存
            boolean isSuccess = orderService.cancelOrderAndRestoreInventory(orderId);

            if (isSuccess) {
                return ResponseEntity.ok(Map.of("message", "訂單已成功取消，庫存已恢復"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "無法取消訂單"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "取消訂單失敗，原因：" + e.getMessage()));
        }
    }
}
