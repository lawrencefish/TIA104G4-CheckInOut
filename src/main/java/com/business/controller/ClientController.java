package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @GetMapping("")
    public String showClient() {
        return "redirect:/client/allClient";
    }

    @GetMapping("/allClient")
    public String showAllClient() {
        return "business/allClient";
    }

    @GetMapping("/clientDetail")
    public String showClientDetail() {
        return "business/clientDetail";
    }
}
