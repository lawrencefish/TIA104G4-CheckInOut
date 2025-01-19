package com.admin.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.admin.model.AdminCouponService;
import com.coupon.model.CouponVO;

@Controller
@RequestMapping("/admin/coupon")
public class AdminCouponController {

	@Autowired
	private AdminCouponService adminCouponService;
	
//	@GetMapping
//    public String list(Model model, 
//                      @RequestParam(defaultValue = "0") int page,
//                      @RequestParam(required = false) String keyword) {
//        
//        Page<CouponVO> couponPage;
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            couponPage = adminCouponService.findByKeyword(keyword, PageRequest.of(page, 10));
//        } else {
//            couponPage = adminCouponService.findAll(PageRequest.of(page, 10));
//        }
//        
//        model.addAttribute("coupons", couponPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", couponPage.getTotalPages());
//        
//        return "admin/coupon-management";
//    }
    
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("coupon", new CouponVO());
        return "admin/coupon-form";
    }
    
    @PostMapping("/create")
    public String create(@Valid CouponVO coupon, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/coupon-form";
        }
        adminCouponService.save(coupon);
        return "redirect:/admin/coupons";
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("coupon", adminCouponService.findById(id));
        return "admin/coupon-form";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, 
                        @Valid CouponVO coupon, 
                        BindingResult result) {
        if (result.hasErrors()) {
            return "admin/coupon-form";
        }
        coupon.setCouponId(id);
        adminCouponService.save(coupon);
        return "redirect:/admin/coupons";
    }
    
//    @PostMapping("/delete/{id}")
//    public String delete(@PathVariable Integer id) {
//    	adminCouponService.deleteById(id);
//        return "redirect:/admin/coupons";
//    }
}
