package com.business.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.order.dto.CommentDTO;
import com.order.model.OrderService;

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
    
    private final OrderService orderService;

    // 使用構造函數注入 OrderService
    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/comment")
    public String getComments(Model model) {
        // 通過 orderService 實例調用 getAllComments 方法
        List<CommentDTO> comments = orderService.getAllComments();
        model.addAttribute("comments", comments);
        return "comments";
    }
}
