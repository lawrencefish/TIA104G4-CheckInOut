package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrdersController {

    @GetMapping("")
    public String showOrders() {
        return "redirect:/orders/allOrders";
    }

    @GetMapping("/allOrders")
    public String showAllOrders() {
        return "business/allOrders";
    }

    @GetMapping("/orderDetail")
    public String showOrderDetail() {
        return "business/orderDetail";
    }
}
