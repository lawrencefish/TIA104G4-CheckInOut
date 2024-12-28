package com.employee.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 更新員工電話與電子郵件
     *
     * @param request HttpServletRequest 用於獲取 HttpSession
     * @param phone   更新後的電話號碼
     * @param email   更新後的電子郵箱
     * @return 重定向到個人資料頁面
     */
    @PostMapping("/update")
    public String updateEmployeeInfo(HttpServletRequest request, String phone, String email) {
        // 從 Session 中獲取員工資料
        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");

        if (employee != null) {
            // 更新電話與電子郵件
            employee.setPhoneNumber(phone);
            employee.setEmail(email);

            // 調用服務層保存更改
            employeeService.updateEmployee(employee);

            // 更新 Session 中的員工資料
            request.getSession().setAttribute("employee", employee);
        }

        return "redirect:/account/personalAccount"; // 重定向到個人資料頁面
    }
}