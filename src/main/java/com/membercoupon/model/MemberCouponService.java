package com.membercoupon.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coupon.model.CouponRepository;
import com.coupon.model.CouponVO;
import com.coupon.model.ResourceNotFoundException;
import com.member.model.MemberRepository;
import com.member.model.MemberVO;

@Service
public class MemberCouponService {
    
	@Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;
    
    
    public MemberCouponVO getById(Integer memberCouponId) {
    	return memberCouponRepository.getById(memberCouponId);
    }
    
    public List<MemberCouponVO> getMemberCoupons(Integer memberId) {
        List<MemberCouponVO> coupons = memberCouponRepository.findByMember_MemberId(memberId);
        coupons.forEach(mc -> {
            if (mc.getCoupon() != null) {
                System.out.println("Coupon: " + mc.getCoupon().getCouponName());  // 添加這行來調試
            } else {
                System.out.println("No coupon for member coupon: " + mc.getMemberCouponId());
            }
        });
        return coupons;
    }

//    public List<MemberCouponVO> getMemberCoupons(Integer memberId) {
//        return memberCouponRepository.findByMember_MemberId(memberId);
//    }
    
//    public List<MemberCouponVO> getMemberCoupons(Integer memberId) {
//        List<MemberCouponVO> coupons = memberCouponRepository.findByMember_MemberId(memberId);
//        coupons.forEach(mc -> {
//            if (mc.getCoupon() != null) {
//                System.out.println("Coupon: " + mc.getCoupon().getCouponName());  // 添加這行來調試
//            } else {
//                System.out.println("No coupon for member coupon: " + mc.getMemberCouponId());
//            }
//        });
//        return coupons;
//    }
    
//    public List<MemberCouponVO> getMemberCoupons(Integer memberId) {
//        List<MemberCouponVO> coupons = memberCouponRepository.findByMember_MemberId(memberId);
//        // 如果需要，可以在這裡添加額外的邏輯，比如過濾過期優惠券等
//        return coupons;
//    }
    public List<MemberCouponVO> getActiveMemberCoupons(Integer memberId) {
        return memberCouponRepository.findByMember_MemberIdAndCouponStatus(memberId, (byte) 1);
    }

    @Transactional
    public void useCoupon(Integer memberCouponId) {
        MemberCouponVO memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new ResourceNotFoundException("Member coupon not found"));

        if (memberCoupon.getCouponStatus() != 1) {
            throw new IllegalStateException("This coupon is not valid");
        }

        memberCoupon.setCouponStatus((byte) 2); // 假設 2 表示已使用
        memberCouponRepository.save(memberCoupon);
    }
    
    @Transactional
    public void returnCoupon(Integer memberCouponId) {
        MemberCouponVO memberCoupon = memberCouponRepository.findById(memberCouponId)
                .orElseThrow(() -> new ResourceNotFoundException("Member coupon not found"));

        if (memberCoupon.getCouponStatus() != 2) {
            throw new IllegalStateException("This coupon is not valid");
        }

        memberCoupon.setCouponStatus((byte) 1); // 假設 2 表示已使用
        memberCouponRepository.save(memberCoupon);
    }


    @Transactional
    public void deleteMemberCoupon(Integer memberCouponId) {
        if (!memberCouponRepository.existsById(memberCouponId)) {
            throw new ResourceNotFoundException("Member coupon not found");
        }
        memberCouponRepository.deleteById(memberCouponId);
    }

    @Transactional
    public void issueCouponToMember(Integer memberId, Integer couponId) {
        MemberVO member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        CouponVO coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        if (!memberCouponRepository.existsByMember_MemberIdAndCoupon_CouponId(memberId, couponId)) {
            MemberCouponVO memberCoupon = new MemberCouponVO();
            memberCoupon.setMember(member);
            memberCoupon.setCoupon(coupon);
            memberCoupon.setCouponStatus((byte) 1);
            memberCoupon.setCreateTime(LocalDateTime.now());
            memberCouponRepository.save(memberCoupon);
        }
    }

    @Transactional
    public void checkCouponExpiry() {
        LocalDateTime now = LocalDateTime.now();
        List<MemberCouponVO> expiredCoupons = memberCouponRepository.findByCouponExpiryDateBeforeAndCouponStatus(now, (byte) 1);
        for (MemberCouponVO memberCoupon : expiredCoupons) {
            memberCoupon.setCouponStatus((byte) 0); // 假設 0 表示已過期
            memberCouponRepository.save(memberCoupon);
        }
    }
}