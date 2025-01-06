package com.membercoupon.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.membercoupon.model.MemberCouponService;
import com.membercoupon.model.MemberCouponVO;

@RestController
@RequestMapping("/api/member-coupons")
public class MemberCouponController {

    @Autowired
    private MemberCouponService memberCouponService;

    
    @PostMapping
    public ResponseEntity<MemberCouponVO> addMemberCoupon(@RequestBody MemberCouponVO memberCouponDTO) {
        MemberCouponVO createdCoupon = memberCouponService.addMemberCoupon(memberCouponDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<MemberCouponVO>> getMemberCoupons(@PathVariable Integer memberId) {
        List<MemberCouponVO> memberCoupons = memberCouponService.getMemberCouponsByMemberId(memberId);
        return ResponseEntity.ok(memberCoupons);
    }

    
    @PutMapping("/{id}/status")
    public ResponseEntity<MemberCouponVO> updateCouponStatus(
            @PathVariable Integer id,
            @RequestParam Byte status) {
        MemberCouponVO updatedCoupon = memberCouponService.updateCouponStatus(id, status);
        return ResponseEntity.ok(updatedCoupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemberCoupon(@PathVariable Integer id) {
        memberCouponService.deleteMemberCoupon(id);
        return ResponseEntity.noContent().build();
    }
}

