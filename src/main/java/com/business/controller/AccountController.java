package com.business.controller;

import com.employee.model.EmployeeVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

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
    public String showAccountSet(HttpServletRequest request) {
        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");
        String title = employee.getTitle();
        if (!"負責人".equals(title) && !"總經理".equals(title) && !"經理".equals(title)) {
            // 若非授權角色，返回個人頁面
            return "redirect:/account/personalAccount";
        }

        return "business/accountSet";
    }
}
