package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {
    @GetMapping("")
    public String show() {
        return "redirect:/account/personalAccount";
    }

    @GetMapping("/personalAccount")
    public String showPersonalAccount() {
        return "business/personalAccount";
    }

    @GetMapping("/accountSet")
    public String showAccountSet() {
        return "business/accountSet";
    }
}
