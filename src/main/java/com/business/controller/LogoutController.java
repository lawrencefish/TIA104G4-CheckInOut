package com.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 清除 session 資料
        HttpSession session = request.getSession(false); // 避免創建新會話
        if (session != null) {
            session.invalidate();
        }

        // 返回登入頁面
        return "redirect:/login/business";
    }

    @PostMapping("/switch-user")
    public String switchUser(HttpSession session, HttpServletRequest request) {
        // 暫存需要保留的資料
        Object hotel = session.getAttribute("hotel");

        // 清除當前的 Session
        session.invalidate();

        // 建立新的 Session
        HttpSession newSession = request.getSession(true); // 重新建立新的 Session
        newSession.setAttribute("hotel", hotel);

        // 返回到 login-2 頁面
        return "redirect:/login/employee";
    }

}
