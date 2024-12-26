package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // 清除所有 Session 資訊
        session.invalidate();

        // 返回到 login-1 頁面
        return "redirect:/login/business";
    }

    @PostMapping("/switch-user")
    public String switchUser(HttpSession session) {
        // 保留需要的 hotel 資訊
        Object hotel = session.getAttribute("hotel");

        // 清除其他 Session 資訊
        session.invalidate();

        // 重新設置 hotel 資訊
        HttpSession newSession = session; // 或使用其他方法重新初始化 Session
        newSession.setAttribute("hotel", hotel);

        // 返回到 login-2 頁面
        return "redirect:/login/employee";
    }
}
