package com.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.admin.model.Admin;
import com.admin.model.*;

import java.util.List;

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
    
//    @GetMapping("/test")
//    public String test(Model model) {
//        List<Admin> admins = adminService.getAll(1);
//        model.addAttribute("admins", admins);
//        return "admin/test";
//    }
}