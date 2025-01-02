package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.admin.model.Admin;
import com.admin.model.*;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller  // 標示這是一個控制器
@RequestMapping("/admin")  // 所有路徑都會以/admin 開頭
public class AdminController {
    
    @Autowired
    private AdminService adminService; // 注入Service層
    
    
    // 列出所有管理員
    @GetMapping("/list")  // 處理GET /admin/list 請求
    public String list(@RequestParam(defaultValue = "1") Integer page, Model model) {
    	// @RequestParam(defaultValue = "1") : 如果網址沒有給 page 參數，預設為第 1 頁
    	
    	List<Admin> admins = adminService.getAll(page);
        model.addAttribute("admins", admins);  // 把資料放入Model
        return "admin/list";  
    }

    // 顯示新增表單
    @GetMapping("/add")  // 處理GET /admin/add 請求
    public String showAddForm(Model model) {
        model.addAttribute("admin", new Admin());  // 準備空的表單物件
        return "admin/add";  
    }

    // 處理新增請求
    @PostMapping("/add")  // 處理POST /admin/add 請求
    public String add(@ModelAttribute Admin admin) {
    	// @ModelAttribute 會自動把表單資料綁定到Admin物件
    	
        adminService.insert(admin);
        return "redirect:/admin/list"; // 新增完成後重導向到列表頁
    }

    // 顯示編輯表單
    @GetMapping("/edit/{id}")  // 處理GET /admin/edit/1 這樣的請求
    public String showEditForm(@PathVariable Integer id, Model model) {
        // @PathVariable 可以取得網址中的變數
    	
    	Admin admin = adminService.getById(id);
        model.addAttribute("admin", admin);
        return "admin/edit"; 
    }

    // 處理編輯請求 
    @PostMapping("/edit")  // 處理POST /admin/edit 請求
    public String edit(@ModelAttribute Admin admin) {
        adminService.update(admin);
        return "redirect:/admin/list";  // 編輯完成後重導向到列表頁
    }

    // 處理刪除請求
    @GetMapping("/delete/{id}")  // 處理GET /admin/delete/1 這樣的請求
    public String delete(@PathVariable Integer id) {
        adminService.delete(id);
        return "redirect:/admin/list";  // 刪除完成後重導向到列表頁
    }
    
    @GetMapping("couponManagement")
    public String showCouponManagement() {
    	return "admin/coupon-management";
    }
    
    @GetMapping("appealManagement")
    public String showAppealManagement() {
    	return "admin/appeal-management";
    }
    
    @GetMapping("latestNews")
    public String showLatestNews() {
    	return "admin/latest-news";
    }
    
    @GetMapping("/reviewBackend")
    public String showReviewBackend() {
    	return "admin/review-backend";
    }
    
    @GetMapping("/adminBackend")
    public String showAdminBackend() {
    	return "admin/admin-backend";
    }
    
    @GetMapping("/userBackend")
    public String showUserBackend() {
    	return "admin/user-backend";
    }
    
    @GetMapping("/login")
    public String showAdminLoginPage() {
        return "admin/adminlogin";  // 這裡返回的是 templates 目錄下的 adminlogin.html
    }
    
    // 處理登入表單提交
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
    	
    	System.out.println("Received email:" + email);
    	System.out.println("Received password:" + password);
    	
    	Admin admin = adminService.adminLogin(email, password);
    	
    	if (admin != null) {
    		
    		System.out.println("登入成功" + admin.getEmail());
    		// 登入成功 管理員ID存入session
    		session.setAttribute("adminId", admin.getAdminId());
    		session.setAttribute("adminEmail", admin.getEmail());
    		return "redirect:/admin/adminBackend"; // 登入成功後轉到管理員頁面
    	} else {
    		System.out.println("登入失敗:" + email);
    		// 登入失敗 錯誤訊息
    		redirectAttributes.addFlashAttribute("error", "帳號或密碼錯誤");
    		return "redirect:/admin/login"; // 回到登入頁面
    	}
    }
    
//    @GetMapping("/test")
//    public String test(Model model) {
//        List<Admin> admins = adminService.getAll(1);
//        model.addAttribute("admins", admins);
//        return "admin/test";
//    }
}