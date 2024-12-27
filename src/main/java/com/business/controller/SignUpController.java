package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signUp")
public class SignUpController {

    @GetMapping("")
    public String show() {
        return "redirect:/signUp/signUp-1";
    }

    @GetMapping("/signUp-1")
    public String showSignUp1() {
        return "business/signup-1";
    }

    @GetMapping("/signUp-2")
    public String showSignUp2() {
        return "business/signup-2";
    }

    @GetMapping("/signUp-3")
    public String showSignUp3() {
        return "business/signup-3";
    }


}
