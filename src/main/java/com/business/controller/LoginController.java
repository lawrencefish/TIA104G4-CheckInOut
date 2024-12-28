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
    public String show(HttpServletRequest request) {
        // 獲取 session 並清除所有屬性
        request.getSession().invalidate();
        return "redirect:/login/business";
    }

    @GetMapping("/business")
    public String showLogin1(HttpServletRequest request) {
        // 檢查是否已登入業者
        HotelVO loggedInHotel = (HotelVO) request.getSession().getAttribute("hotel");
        if (loggedInHotel != null) {
            // 如果業者已登入，直接跳轉到系統頁面
            return "redirect:/login/employee"; // 替換為業者進入的頁面
        }
        // 如果未登入，顯示登入頁面
        return "business/login-1";
    }

    @PostMapping("/business")
    public String processBusinessLogin(
            @RequestParam(value = "taxId", required = false) String taxId,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request,
            Model model
    ) {
        // 回填用戶輸入
        model.addAttribute("taxId", taxId);

        // 清理輸入
        taxId = taxId == null ? "" : taxId.trim();
        password = password == null ? "" : password.trim();

        // 驗證輸入是否為空
        boolean hasError = false;
        if (taxId.isEmpty()) {
            model.addAttribute("taxIdError", "統一編號必填");
            hasError = true;
        }
        if (password.isEmpty()) {
            model.addAttribute("passwordError", "密碼必填");
            hasError = true;
        }

        if (hasError) {
            return "business/login-1";
        }

        // 查詢業者
        Optional<HotelVO> hotelOpt = hotelService.findByTaxId(taxId);
        if (hotelOpt.isEmpty()) {
            model.addAttribute("generalError", "統一編號或密碼錯誤");
            return "business/login-1";
        }

        HotelVO hotel = hotelOpt.get();
        if (!hotel.getPassword().equals(password)) {
            model.addAttribute("generalError", "統一編號或密碼錯誤");
            return "business/login-1";
        }

        // 驗證成功 -> 存入 Session
        request.getSession().setAttribute("hotel", hotel);

        // 導向員工登入頁
        return "redirect:/login/employee";
    }

    @GetMapping("/employee")
    public String showLogin2(HttpServletRequest request) {
        // 檢查是否已登入員工
        EmployeeVO loggedInEmployee = (EmployeeVO) request.getSession().getAttribute("employee");
        if (loggedInEmployee != null) {
            // 如果員工已登入，直接跳轉到員工的系統頁面
            return "redirect:/frontDesk"; // 替換為員工的主頁
        }
        // 如果未登入，顯示登入頁面
        return "business/login-2";
    }


    @PostMapping("/employee")
    public String processEmployeeLogin(
            @RequestParam(value = "employeeNumber", required = false) String employeeNumber,
            @RequestParam(value = "password", required = false) String password,
            HttpServletRequest request,
            Model model
    ) {
        // 回填用戶輸入
        model.addAttribute("employeeNumber", employeeNumber);

        // 清理輸入
        employeeNumber = employeeNumber == null ? "" : employeeNumber.trim();
        password = password == null ? "" : password.trim();

        // 驗證業者是否已登入
        HotelVO loggedInHotel = (HotelVO) request.getSession().getAttribute("hotel");
        if (loggedInHotel == null) {
            return "redirect:/login/business";
        }

        // 驗證輸入是否為空
        boolean hasError = false;
        if (employeeNumber.isEmpty()) {
            model.addAttribute("employeeNumberError", "員工編號必填");
            hasError = true;
        }
        if (password.isEmpty()) {
            model.addAttribute("passwordError", "密碼必填");
            hasError = true;
        }

        if (hasError) {
            return "business/login-2";
        }

        // 查詢員工
        Optional<EmployeeVO> employeeOpt = employeeService.findByEmployeeNumberAndHotel_HotelId(employeeNumber, loggedInHotel.getHotelId());
        if (employeeOpt.isEmpty() || !employeeOpt.get().getPassword().equals(password)) {
            model.addAttribute("generalError", "員工編號或密碼錯誤");
            return "business/login-2";
        }

        // 驗證成功 -> 存入 Session
        EmployeeVO employee = employeeOpt.get();
        request.getSession().setAttribute("employee", employee);

        // 導向員工後台
        return "redirect:/frontDesk";
    }


}
