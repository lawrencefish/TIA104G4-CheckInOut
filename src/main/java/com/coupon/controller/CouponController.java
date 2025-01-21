package com.coupon.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupon.dto.MemberCouponDTO;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.membercoupon.model.MemberCouponVO;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<Void> createCoupon(@RequestBody CouponVO coupon) {
        couponService.createCoupon(coupon);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponVO> getCoupon(@PathVariable Integer id) {
        CouponVO coupon = couponService.getCoupon(id);
        return ResponseEntity.ok(coupon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCoupon(@PathVariable Integer id, @RequestBody CouponVO coupon) {
        coupon.setCouponId(id);
        couponService.updateCoupon(coupon);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Integer id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/issue-new-member/{memberId}")
//    public ResponseEntity<Void> issueNewMemberCoupon(@PathVariable Integer memberId) {
//        couponService.issueNewMemberCoupon(memberId);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/issue-travel-city/{memberId}/{cityCount}")
    public ResponseEntity<Void> issueTravelCityCoupon(@PathVariable Integer memberId, @PathVariable Integer cityCount) {
        couponService.issueCouponBasedOnTravelCities(memberId, cityCount);
        return ResponseEntity.ok().build();
    }
    
//    @PostMapping("/test/create")
//    public ResponseEntity<CouponVO> testCreateCoupon() {
//        CouponVO coupon = new CouponVO();
//        coupon.setCouponName("測試優惠券");
//        coupon.setActiveDate(LocalDateTime.now());
//        coupon.setExpiryDate(LocalDateTime.now().plusDays(30));
//        coupon.setMinSpend(100);
//        coupon.setTravelCityNum(1);
//        coupon.setCouponDetail("這是一個測試優惠券");
//        coupon.setDiscountAmount(50);
//        
//        couponService.createCoupon(coupon);
//        return ResponseEntity.ok(coupon);
//    }

//    @PostMapping("/test/issue/{memberId}")
//    public ResponseEntity<Void> testIssueCoupon(@PathVariable Integer memberId) {
//        couponService.issueNewMemberCoupon(memberId);
//        return ResponseEntity.ok().build();
//    }

//    @GetMapping("/test/check-expiry")
//    public ResponseEntity<String> testCheckExpiry() {
//        couponService.checkCouponExpiry();
//        return ResponseEntity.ok("優惠券過期檢查已執行");
//    }

    @GetMapping("/member-coupons/{memberId}")
    public ResponseEntity<List<MemberCouponDTO>> getMemberCoupons(@PathVariable Integer memberId) {
        try {
            List<MemberCouponDTO> memberCoupons = couponService.getMemberCoupons(memberId);
            System.out.println("Returning " + memberCoupons.size() + " coupons for member " + memberId);
            if (memberCoupons.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(memberCoupons);
        } catch (Exception e) {
            System.err.println("Error getting coupons for member " + memberId + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful");
    }
    
    @PostMapping("/issue-to-all/{couponId}")
    public ResponseEntity<String> issueCouponToAllMembers(@PathVariable Integer couponId) {
        couponService.issueCouponToAllMembers(couponId);
        return ResponseEntity.ok("優惠券已成功發送給所有會員");
    
}
}
