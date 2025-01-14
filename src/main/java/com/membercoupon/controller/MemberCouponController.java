package com.membercoupon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponVO;

@RestController
@RequestMapping("/member/coupon")
public class MemberCouponController {

    @Autowired
    private MemberCouponRepository memberCouponRepository;
    
    @Autowired
    private MemberService memberService;

    // 查詢會員擁有的優惠券
    @GetMapping("/list/{memberId}")
    public List<MemberCouponVO> getMemberCoupons(@PathVariable Integer memberId) {
    	try {
            MemberVO member = memberService.getOneMem(memberId);
            return memberCouponRepository.findByMember(member);
        } catch (Exception e) {
            System.out.println("查詢會員優惠券失敗:" + e.getMessage());
            return new ArrayList<>();
        }
    }
    //查詢會員有效的優惠券
    @GetMapping("/valid/{memberId}")
    public List<MemberCouponVO> getValidMemberCoupons(@PathVariable Integer memberId) {
        try {
            MemberVO member = memberService.getOneMem(memberId);
            return memberCouponRepository.findByMemberAndCouponStatus(member, (byte) 1);
        } catch (Exception e) {
            System.out.println("查詢會員有效優惠券失敗:" + e.getMessage());
            return new ArrayList<>();
        }
    }

    // 修改優惠券狀態 (使用/失效)
    @PutMapping("/status/{memberCouponId}")
    public String updateCouponStatus(
            @PathVariable Integer memberCouponId,
            @RequestParam("status") Byte status) {
        try {
            MemberCouponVO memberCoupon = memberCouponRepository.findById(memberCouponId)
                    .orElseThrow(() -> new RuntimeException("優惠券不存在"));
            
            memberCoupon.setCouponStatus(status);
            memberCouponRepository.save(memberCoupon);
            return "優惠券狀態更新成功";
        } catch (Exception e) {
            System.out.println("更新優惠券狀態失敗:" + e.getMessage());
            return "更新失敗:" + e.getMessage();
        }
    }
}
