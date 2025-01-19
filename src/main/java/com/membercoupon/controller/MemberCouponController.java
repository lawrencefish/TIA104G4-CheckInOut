package com.membercoupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.membercoupon.model.MemberCouponService;
import com.membercoupon.model.MemberCouponVO;

public class MemberCouponController {
	
	@Autowired
    private MemberCouponService memberCouponService;

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MemberCouponVO>> getMemberCoupons(@PathVariable Integer memberId) {
        return ResponseEntity.ok(memberCouponService.getMemberCoupons(memberId));
    }

    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<MemberCouponVO>> getActiveMemberCoupons(@PathVariable Integer memberId) {
        return ResponseEntity.ok(memberCouponService.getActiveMemberCoupons(memberId));
    }

    @PostMapping("/use/{memberCouponId}")
    public ResponseEntity<Void> useCoupon(@PathVariable Integer memberCouponId) {
        memberCouponService.useCoupon(memberCouponId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberCouponId}")
    public ResponseEntity<Void> deleteMemberCoupon(@PathVariable Integer memberCouponId) {
        memberCouponService.deleteMemberCoupon(memberCouponId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/issue")
    public ResponseEntity<Void> issueCouponToMember(@RequestParam Integer memberId, @RequestParam Integer couponId) {
        memberCouponService.issueCouponToMember(memberId, couponId);
        return ResponseEntity.ok().build();
    }
}

