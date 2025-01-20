package com.coupon.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.member.model.MemberRepository;
import com.member.model.MemberVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponVO;

@Service
public class CouponService {

	
    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void createCoupon(CouponVO coupon) {
        couponRepository.save(coupon);
    }

    public CouponVO getCoupon(Integer couponId) {
        return couponRepository.findById(couponId).orElse(null);
    }

    @Transactional
    public void updateCoupon(CouponVO coupon) {
        couponRepository.save(coupon);
    }

    @Transactional
    public void deleteCoupon(Integer couponId) {
        couponRepository.deleteById(couponId);
    }

    

    @Transactional
    public void issueCouponBasedOnTravelCities(Integer memberId, Integer travelCityCount) {
        MemberVO member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));
        List<CouponVO> eligibleCoupons = getCouponsForTravelCityCount(travelCityCount);
        for (CouponVO coupon : eligibleCoupons) {
            issueCouponToMember(member, coupon);
        }
    }

    @Transactional
    public void checkCouponExpiry() {
        List<MemberCouponVO> activeCoupons = memberCouponRepository.findByCouponStatus((byte)1);
        LocalDateTime now = LocalDateTime.now();
        for (MemberCouponVO memberCoupon : activeCoupons) {
            if (memberCoupon.getCoupon().getExpiryDate().isBefore(now)) {
                memberCoupon.setCouponStatus((byte)0);
                memberCouponRepository.save(memberCoupon);
            }
        }
    }

    private void issueCouponToMember(MemberVO member, CouponVO coupon) {
        MemberCouponVO memberCoupon = new MemberCouponVO();
        memberCoupon.setMember(member);
        memberCoupon.setCoupon(coupon);
        memberCoupon.setCouponStatus((byte)1);
        memberCoupon.setCreateTime(LocalDateTime.now());
        memberCouponRepository.save(memberCoupon);
    }

//    private CouponVO getCouponForNewMember() {
//        // Logic to get the coupon for new members
//        return null;
//    }

    private List<CouponVO> getCouponsForTravelCityCount(Integer travelCityCount) {
        // Logic to get coupons based on travel city count
        return null;
    }
    
    public List<MemberCouponVO> getMemberCoupons(Integer memberId) {
        return memberCouponRepository.findByMember_MemberId(memberId);
    }
    
    //發給所有會員
    @Transactional
    public void issueCouponToAllMembers(Integer couponId) {
        CouponVO coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        
        List<MemberVO> allMembers = memberRepository.findAll();
        
        for (MemberVO member : allMembers) {
            if (!memberCouponRepository.existsByMember_MemberIdAndCoupon_CouponId(member.getMemberId(), couponId)) {
                MemberCouponVO memberCoupon = new MemberCouponVO();
                memberCoupon.setMember(member);
                memberCoupon.setCoupon(coupon);
                memberCoupon.setCouponStatus((byte) 1); // 假設 1 表示有效
                memberCoupon.setCreateTime(LocalDateTime.now());
                memberCouponRepository.save(memberCoupon);
            }
        }
        
        }
//    @Transactional
//    public void createAndIssueNewMemberCoupon(MemberVO member) {
//        CouponVO newMemberCoupon = createNewMemberCoupon();
//        newMemberCoupon = couponRepository.save(newMemberCoupon);
//        issueCouponToMember(member, newMemberCoupon);
//    }
    //監聽事件
    @Transactional
    public void createWelcomeCouponForMember(MemberVO member) {
        CouponVO coupon = new CouponVO();
        coupon.setCouponName("新會員優惠");
        coupon.setDiscountAmount(300);
        coupon.setMinSpend(1000);
        coupon.setActiveDate(LocalDateTime.now());
        coupon.setExpiryDate(LocalDateTime.now().plusMonths(1));
        coupon.setTravelCityNum(0);
        coupon.setCouponDetail("歡迎加入！首次訂房享有 300 元折扣");
   
        // 先儲存優惠券以產生 coupon_id
        coupon = couponRepository.save(coupon);
        
        
        memberCouponRepository.save(memberCoupon);
    }

   
        
    
 

//    private void issueCouponToMember(MemberVO member, CouponVO coupon) {    	    	
//        MemberCouponVO memberCoupon = new MemberCouponVO();
//        memberCoupon.setMember(member);
//        memberCoupon.setCoupon(coupon);
//        memberCoupon.setCouponStatus((byte) 1);
//        memberCoupon.setCreateTime(LocalDateTime.now());
//        memberCouponRepository.save(memberCoupon);
//    }
        //	註冊發送優惠券
//    @Transactional
//    public void issueNewMemberCoupon(Integer memberId) {
//        MemberVO member = memberRepository.findById(memberId)
//            .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
//
//        // 獲取或創建新會員優惠券
//        CouponVO newMemberCoupon = getOrCreateNewMemberCoupon();
//
//        MemberCouponVO memberCoupon = new MemberCouponVO();
//        memberCoupon.setMember(member);
//        memberCoupon.setCoupon(newMemberCoupon);
//        memberCoupon.setCouponStatus((byte) 1); // 1 表示有效
//        memberCoupon.setCreateTime(LocalDateTime.now());
//
//        memberCouponRepository.save(memberCoupon);
//    }
//
//    private CouponVO getOrCreateNewMemberCoupon() {
//        return couponRepository.findByCouponName("新會員歡迎優惠")
//            .orElseGet(this::createNewMemberCoupon);
//    }
//
//    private CouponVO createNewMemberCoupon() {
//        CouponVO coupon = new CouponVO();
//        coupon.setCouponName("新會員歡迎優惠");
//        coupon.setDiscountAmount(100); // 假設折扣 100 元
//        coupon.setMinSpend(500); // 最低消費 500 元
//        coupon.setActiveDate(LocalDateTime.now());
//        coupon.setExpiryDate(LocalDateTime.now().plusMonths(1)); // 一個月有效期
//        coupon.setCouponDetail("歡迎加入！首次訂房享有 100 元折扣");
//        coupon.setStatus((byte) 1); // 1 表示有效
//
//        return couponRepository.save(coupon);
//    }
}

    