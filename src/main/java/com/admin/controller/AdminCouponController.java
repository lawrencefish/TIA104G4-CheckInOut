package com.admin.controller;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.model.AdminCouponService;
import com.coupon.model.CouponVO;

@Controller
@RequestMapping("/admin/coupon")
public class AdminCouponController {

	@Autowired
	private AdminCouponService adminCouponService;
	
	@GetMapping("")
    public String list(Model model, 
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword) {

	PageRequest pageRequest = PageRequest.of(page, 10);
	Page<CouponVO> couponPage;
	
		if (keyword != null && !keyword.trim().isEmpty()) {
		  couponPage = adminCouponService.findByKeyword(keyword, pageRequest);
		} else {
		  couponPage = adminCouponService.findAll(pageRequest);
		}
	
		model.addAttribute("coupons", couponPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", couponPage.getTotalPages());
		
		return "admin/coupon-management";
	}
	
	//創建優惠券表單
	@GetMapping("/create")
    public String createForm(Model model) {
        CouponVO coupon = new CouponVO();
        
        // 設置預設值
        LocalDateTime now = LocalDateTime.now();
        coupon.setActiveDate(now);
        coupon.setExpiryDate(now.plusMonths(1));
        coupon.setTravelCityNum(0);
        
        model.addAttribute("coupon", coupon);
        return "admin/edit-coupon";
    }
	
	// 編輯優惠券表單
	@GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        try {
            CouponVO coupon = adminCouponService.findById(id);
            model.addAttribute("coupon", coupon);
            model.addAttribute("isEdit", true); // 添加標記，用於前端判斷是否為編輯模式
            return "admin/edit-coupon";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/coupon";
        }
    }
	
	// 保存優惠券（處理創建和編輯的提交）
	@PostMapping("/save")
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute("coupon") CouponVO coupon, 
	                            BindingResult result) {
	    try {
	        System.out.println("接收到的優惠券數據：" + coupon);
	        
	        // 基本驗證
	        if (result.hasErrors()) {
	            List<Map<String, String>> errors = result.getFieldErrors().stream()
	                .map(error -> {
	                    Map<String, String> errorMap = new HashMap<>();
	                    errorMap.put("field", error.getField());
	                    errorMap.put("message", error.getDefaultMessage());
	                    return errorMap;
	                })
	                .collect(Collectors.toList());
	            return ResponseEntity.badRequest().body(errors);
	        }

	        // 設置預設值
	        if (coupon.getTravelCityNum() == null) {
	            coupon.setTravelCityNum(0); // 設置預設值為 0
	        }

	        // 設定創建時間
	        if (coupon.getCouponId() == null) {
	            coupon.setCreateTime(LocalDateTime.now());
	        }
	        
	        // 保存優惠券
	        CouponVO savedCoupon = adminCouponService.save(coupon);
	        return ResponseEntity.ok(savedCoupon);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, String> error = new HashMap<>();
	        error.put("message", "保存優惠券失敗: " + e.getMessage());
	        return ResponseEntity.badRequest().body(error);
	    }
	}
	
	// 日期格式轉換
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
	    
	    binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	            System.out.println("正在轉換日期：" + text);
	            
	            try {
	                if (text == null || text.trim().isEmpty()) {
	                    setValue(null);
	                    return;
	                }
	                
	                // 使用 ISO_DATE 格式解析日期 (YYYY-MM-DD)
	                LocalDate date = LocalDate.parse(text.trim(), formatter);
	                LocalDateTime dateTime = date.atStartOfDay();
	                setValue(dateTime);
	                
	                System.out.println("日期轉換成功：" + dateTime);
	            } catch (Exception e) {
	                System.out.println("日期轉換失敗：" + e.getMessage());
	                throw new IllegalArgumentException("日期格式不正確，請使用 YYYY-MM-DD 格式");
	            }
	        }

	        @Override
	        public String getAsText() {
	            LocalDateTime value = (LocalDateTime) getValue();
	            return value == null ? "" : value.toLocalDate().format(formatter);
	        }
	    });
	}
	
	@PostMapping("/delete/{id}")
	@ResponseBody
	public ResponseEntity<String> delete(@PathVariable Integer id) {
	    try {
	        adminCouponService.deleteById(id);
	        return ResponseEntity.ok("刪除成功");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                           .body("刪除失敗: " + e.getMessage());
	    }
	}
}