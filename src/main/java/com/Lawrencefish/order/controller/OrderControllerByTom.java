package com.Lawrencefish.order.controller;

import com.Lawrencefish.order.model.OrderServiceByTom;
import com.order.model.OrderService;
import com.order.model.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderControllerByTom {
    @Autowired
    private OrderServiceByTom orderService;

    @GetMapping("/today")
    public ResponseEntity<List<Map<String, Object>>> getTodayOrders(@RequestParam Integer hotelId) {
        List<Map<String, Object>> todayOrders = orderService.getTodayOrders(hotelId);
        return ResponseEntity.ok(todayOrders);
    }
}