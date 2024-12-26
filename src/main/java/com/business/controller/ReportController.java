package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {
    @GetMapping("")
    public String show() {
        return "redirect:/report/sales";
    }

    @GetMapping("/sales")
    public String showSales() {
        return "business/sales";
    }

    @GetMapping("/orderQuantity")
    public String showOrderQuantity() {
        return "business/orderQuantity";
    }
}
