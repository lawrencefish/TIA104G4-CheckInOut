package com.coupon.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coupon.model.CouponRepository;
import com.coupon.model.CouponService;
import com.coupon.model.CouponVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponVO;
import com.order.model.OrderRepository;

@RestController
@RequestMapping("/coupon")
public class CouponController {
    
    @Autowired
    private CouponService couponService;
    
    @Autowired
    private MemberCouponRepository memberCouponRepository;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CouponRepository couponRepository;
    
 // 查詢會員的旅遊城市數量
    
    @GetMapping("/city/count/{memberId}")
    public String getCityCount(@PathVariable Integer memberId) {
        try {
            Integer cityCount = orderRepository.countMemberTravelCities(memberId);
            return "目前旅遊城市數量: " + cityCount;
        } catch (Exception e) {
            System.out.println("查詢城市數量失敗:" + e.getMessage());
            return "查詢失敗";
        }
    }
    
 // 查詢會員的所有旅遊城市
    
    @GetMapping("city/list/{memberId}")
    public List<String> getCityList(@PathVariable Integer memberId) {
        try {
            return orderRepository.findDistinctCitiesByMember(memberId);
        } catch (Exception e) {
            System.out.println("查詢城市清單失敗:" + e.getMessage());
            return new ArrayList<>();
        }
    }
    
 // 手動觸發發放城市優惠券
    @PostMapping("issue/{memberId}")
    public String issueCityCoupons(@PathVariable Integer memberId) {
        try {
            MemberVO member = memberService.getOneMem(memberId);
            if (member == null) {
                return "會員不存在";
            }
            couponService.checkAndIssueCityCouponsAfterCheckout(memberId);
            return "優惠券發放檢查完成";
        } catch (Exception e) {
            System.out.println("發放優惠券失敗:" + e.getMessage());
            return "發放失敗:" + e.getMessage();
        }
    }

    // 檢查優惠券狀態
    @GetMapping("check/{memberCouponId}")
    public String checkCouponStatus(@PathVariable Integer memberCouponId) {
        try {
            MemberCouponVO memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new RuntimeException("優惠券不存在"));
            
            String status = memberCoupon.getCouponStatus() == 1 ? "有效" : "已失效";
            return "優惠券狀態: " + status;
        } catch (Exception e) {
            System.out.println("查詢優惠券狀態失敗:" + e.getMessage());
            return "查詢失敗:" + e.getMessage();
        }
    }
    
    @PostMapping("/add")
    public String addCoupon(@RequestBody CouponVO couponVO) {
        try {
            // 設定建立時間
            couponVO.setCreateTime(LocalDateTime.now());
            couponRepository.save(couponVO);
            return "優惠券新增成功";
        } catch (Exception e) {
            System.out.println("新增優惠券失敗:" + e.getMessage());
            return "新增優惠券失敗:" + e.getMessage();
        }
    }
 }
