package com.business.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.hotel.model.HotelService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("")
    public String show() {
        return "redirect:/login/business";
    }

    @GetMapping("/business")
    public String showLogin1() {
        // 回傳的字串會對應 resources/templates/business/login-1.html
        return "business/login-1";
    }

    @PostMapping("/business")
    public String processBusinessLogin(
            @RequestParam("taxId") String taxId,
            @RequestParam("password") String password,
            HttpServletRequest request,
            Model model
    ) {
        boolean hasError = false;

        // 檢查 taxId
        if (taxId == null || taxId.isBlank()) {
            model.addAttribute("taxIdError", "必填");
            hasError = true;
        }

        // 檢查 password
        if (password == null || password.isBlank()) {
            model.addAttribute("passwordError", "必填");
            hasError = true;
        }

        // 如果有任何欄位錯誤，回到登入頁面
        if (hasError) {
            return "business/login-1";
        }

        // 查詢資料庫
        Optional<HotelVO> hotelOpt = hotelService.findByTaxId(taxId);
        if (hotelOpt.isEmpty()) {
            model.addAttribute("taxIdError", "統一編號不存在");
            return "business/login-1";
        }

        // 驗證密碼
        HotelVO hotel = hotelOpt.get();
        if (!hotel.getPassword().equals(password)) {
            model.addAttribute("passwordError", "密碼錯誤");
            return "business/login-1";
        }

        // 驗證成功，直接存入 Session
        request.getSession().setAttribute("hotel", hotel);

        // 導向後台頁面
        return "redirect:/login/employee";
    }

    @GetMapping("/employee")
    public String showLogin2() {
        // 回傳的字串會對應 resources/templates/business/login-1.html
        return "business/login-2";
    }


    @PostMapping("/employee")
    public String processEmployeeLogin(
            @RequestParam("employeeNumber") String employeeNumber,
            @RequestParam("password") String password,
            HttpServletRequest request,
            Model model
    ) {
        boolean hasError = false;

        // 檢查是否有業者的 Session 資訊
        HotelVO loggedInHotel = (HotelVO) request.getSession().getAttribute("hotel");
        if (loggedInHotel == null) {
            model.addAttribute("generalError", "請先以業者身份登入");
            return "redirect:/login/business";
        }

        // 1. 檢查是否有填寫欄位
        if (employeeNumber == null || employeeNumber.isBlank()) {
            model.addAttribute("employeeNumberError", "員工編號必填");
            hasError = true;
        }
        if (password == null || password.isBlank()) {
            model.addAttribute("passwordError", "密碼必填");
            hasError = true;
        }

        // 2. 有錯誤就回到同一頁
        if (hasError) {
            return "employeeLogin"; // 對應 src/main/resources/templates/employeeLogin.html
        }

        // 3. 查資料庫: 檢查是否有這個員工編號，且隸屬於當前旅館
        Optional<EmployeeVO> employeeOpt = employeeService.findByEmployeeNumberAndHotel_HotelId(employeeNumber, loggedInHotel.getHotelId());
        if (employeeOpt.isEmpty() || !employeeOpt.get().getPassword().equals(password)) {
            // 找不到或密碼錯 -> 統一顯示在 passwordError
            model.addAttribute("passwordError", "員工編號或密碼錯誤");
            return "employeeLogin";
        }

        // 4. 驗證成功 -> 存入 Session
        EmployeeVO employee = employeeOpt.get();
        request.getSession().setAttribute("employee", employee);

        // 導向員工後台頁面
        return "redirect:/frontDesk";
    }

}
