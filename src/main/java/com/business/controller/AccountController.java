package com.business.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("")
    public String show() {
        return "redirect:/account/personalAccount";
    }

    @GetMapping("/personalAccount")
    public String showPersonalAccount(HttpServletRequest request) {
        // 從 Session 中獲取當前員工
        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");
        if (employee != null) {
            // 使用 Optional 安全地查找員工資料
            Optional<EmployeeVO> updatedEmployee = employeeService.findByEmployeeId(employee.getEmployeeId());
            // 如果查詢到資料，更新 Session，否則記錄錯誤或設置默認值
            if (updatedEmployee.isPresent()) {
                request.getSession().setAttribute("employee", updatedEmployee.get());
            } else {
                // 如果查無資料，可選擇設置默認值或記錄警告
                System.err.println("No employee found with ID: " + employee.getEmployeeId());
            }
        } else {
            // 如果 Session 中沒有員工對象，處理異常情況
            System.err.println("No employee found in session.");
            // 可能需要重定向到登錄頁面或顯示錯誤訊息
            return "redirect:/login";
        }
        // 返回頁面
        return "business/personalAccount";
    }

    @GetMapping("/accountSet")
    public String showAccountSet(HttpServletRequest request, Model model) {
        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");
        String title = employee.getTitle();
        if (!"負責人".equals(title) && !"總經理".equals(title) && !"經理".equals(title)) {
            // 若非授權角色，返回個人頁面
            return "redirect:/account/personalAccount";
        }

        // 從 session 獲取 hotel 資訊
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");
        Integer hotelId = hotel.getHotelId();
        List<EmployeeVO> employees = employeeService.getEmployeesByHotelId(hotelId);

        // 定義職稱優先級
        Map<String, Integer> titlePriority = Map.of(
                "負責人", 1,
                "總經理", 2,
                "經理", 3,
                "襄理", 4,
                "員工", 5
        );

        // 按照職稱排序
        employees.sort(Comparator.comparingInt(emp -> titlePriority.getOrDefault(emp.getTitle(), Integer.MAX_VALUE)));

        // 傳遞員工資料到頁面
        model.addAttribute("employees", employees);

        return "business/accountSet";
    }
}
