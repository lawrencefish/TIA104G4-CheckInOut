package com.employee.controller;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    public String addEmployee(
            @ModelAttribute EmployeeVO employeeVO,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");
        if (hotel == null) {
            redirectAttributes.addFlashAttribute("addError", "無法新增員工，請先登入公司。");
            return "redirect:/login/business";
        }

        employeeVO.setHotel(hotel);

        // 驗證員工資料是否重複
        if (employeeService.existsByHotelIdAndEmployeeNumber(hotel.getHotelId(), employeeVO.getEmployeeNumber())) {
            redirectAttributes.addFlashAttribute("addError", "該旅館已存在相同員工編號，請使用其他編號。");
            return "redirect:/account/accountSet";
        }

        if (employeeService.existsByPhoneNumber(employeeVO.getPhoneNumber())) {
            redirectAttributes.addFlashAttribute("addError", "電話號碼已被註冊，請使用其他號碼。");
            return "redirect:/account/accountSet";
        }

        if (employeeService.existsByEmail(employeeVO.getEmail())) {
            redirectAttributes.addFlashAttribute("addError", "Email 已被註冊，請使用其他 Email。");
            return "redirect:/account/accountSet";
        }

        try {
            employeeService.addEmployee(employeeVO);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("addError", "員工新增失敗，請稍後重試。");
            e.printStackTrace();
        }

        return "redirect:/account/accountSet";
    }




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

    @PostMapping("/set")
    public String setEmployee(@ModelAttribute EmployeeVO employeeVO, RedirectAttributes redirectAttributes) {
        // 根據 employeeId 查詢現有的員工資料
        EmployeeVO existingEmployee = employeeService.getEmployeeById(employeeVO.getEmployeeId());
        if (existingEmployee == null) {
            redirectAttributes.addFlashAttribute("error", "未找到對應的員工資料！");
            return "redirect:/account/accountSet"; // 跳轉回員工列表頁面或其他適合的頁面
        }

        // 更新員工資料，包括 employeeNumber 和 name
        existingEmployee.setEmployeeNumber(employeeVO.getEmployeeNumber());
        existingEmployee.setName(employeeVO.getName());
        existingEmployee.setTitle(employeeVO.getTitle());
        existingEmployee.setPhoneNumber(employeeVO.getPhoneNumber());
        existingEmployee.setEmail(employeeVO.getEmail());

        // 保存更新後的員工資料
        employeeService.updateEmployee(existingEmployee);

        // 添加成功訊息
        redirectAttributes.addFlashAttribute("success", "員工資料更新成功！");
        return "redirect:/account/accountSet"; // 跳轉回員工列表頁面或其他適合的頁面
    }

    @PostMapping("/updatePassword")
    public String updatePassword(
            HttpServletRequest request,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordCheck") String newPasswordCheck,
            Model model) {

        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");

        // 驗證輸入
        if (oldPassword == null || oldPassword.trim().isEmpty() ||
                newPassword == null || newPassword.trim().isEmpty() ||
                newPasswordCheck == null || newPasswordCheck.trim().isEmpty()) {
            model.addAttribute("error", "所有欄位均為必填");
            return "business/personalAccount"; // 返回包含彈窗的頁面
        }

        if (!newPassword.equals(newPasswordCheck)) {
            model.addAttribute("error", "新密碼與確認密碼不一致");
            return "business/personalAccount";
        }

        if (!oldPassword.equals(employee.getPassword())) {
            model.addAttribute("error", "舊密碼不正確");
            return "business/personalAccount";
        }

        // 更新密碼
        employee.setPassword(newPassword);
        employeeService.update(employee);
        request.getSession().setAttribute("employee", employee);

        return "redirect:/account/personalAccount"; // 返回頁面以顯示成功消息
    }

    @PostMapping("/setPassword")
    public String setPassword(
            HttpServletRequest request,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordCheck") String newPasswordCheck,
            Model model) {
        EmployeeVO employee = (EmployeeVO) request.getSession().getAttribute("employee");

        // 驗證輸入
        if (newPassword == null || newPassword.trim().isEmpty() ||
                newPasswordCheck == null || newPasswordCheck.trim().isEmpty()) {
            model.addAttribute("error", "所有欄位均為必填");
            return "business/accountSet"; // 返回包含彈窗的頁面
        }

        if (!newPassword.equals(newPasswordCheck)) {
            model.addAttribute("error", "新密碼與確認密碼不一致");
            return "business/accountSet";
        }

        // 更新密碼
        employee.setPassword(newPassword);
        employeeService.update(employee);
        request.getSession().setAttribute("employee", employee);

        return "redirect:/account/accountSet"; // 返回頁面以顯示成功消息
    }
}