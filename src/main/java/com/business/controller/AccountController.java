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
    public String showPersonalAccount() {
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
