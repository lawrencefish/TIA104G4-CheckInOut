package com.membercoupon.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.model.CouponRepository;
import com.member.model.MemberRepository;

@Service
@Transactional
public class MemberCouponService {
    
//    @Autowired
//    private MemberCouponRepository memberCouponRepository;
//    
//    @Autowired
//    private MemberRepository memberRepository;
//    
//    @Autowired
//    private CouponRepository couponRepository;
//    
//    public MemberCouponVO issueCouponToMember(Long memberId, String couponCode) {
//        Member member = memberRepository.findById(memberId)
//            .orElseThrow(() -> new NotFoundException("會員不存在"));
//            
//        Coupon coupon = couponRepository.findByCode(couponCode)
//            .orElseThrow(() -> new NotFoundException("優惠券不存在"));
//            
//        // 檢查優惠券是否已過期
//        if (LocalDateTime.now().isAfter(coupon.getValidTo())) {
//            throw new InvalidOperationException("優惠券已過期");
//        }
//        
//        // 檢查會員是否已擁有此優惠券
//        if (memberCouponRepository.findByMemberIdAndCouponCode(memberId, couponCode).isPresent()) {
//            throw new InvalidOperationException("會員已擁有此優惠券");
//        }
//        
//        MemberCoupon memberCoupon = new MemberCoupon();
//        memberCoupon.setMember(member);
//        memberCoupon.setCoupon(coupon);
//        memberCoupon.setIssuedDate(LocalDateTime.now());
//        memberCoupon.setUsed(false);
//        
//        return memberCouponRepository.save(memberCoupon);
//    }
//    
//    public void useCoupon(Long memberId) {
//        MemberCouponVO memberCoupon = memberCouponRepository.findByMemberIdAndCouponCode(memberId)
//            .orElseThrow(() -> new NotFoundException("找不到此優惠券"));
//            
//        if (memberCoupon.isUsed()) {
//            throw new InvalidOperationException("優惠券已使用");
//        }
//        
//        if (LocalDateTime.now().isAfter(memberCoupon.getCoupon().getValidTo())) {
//            throw new InvalidOperationException("優惠券已過期");
//        }
//        
//        memberCoupon.setUsed(true);
//        memberCoupon.setUsedDate(LocalDateTime.now());
//        memberCouponRepository.save(memberCoupon);
//    }
//    
//    public List<MemberCouponVO> getMemberUnusedCoupons(Long memberId) {
//        return memberCouponRepository.findByMemberIdAndUsed(memberId, false);
//    }
}
