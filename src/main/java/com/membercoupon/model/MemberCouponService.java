package com.membercoupon.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberCouponService {
    
    @Autowired
    private MemberCouponRepository memberCouponRepository;
    
    public List<MemberCouponVO> findByMemberIdAndCouponStatus(Integer memberId, Byte status) {
    	return memberCouponRepository.findByMember_MemberIdAndCouponStatus(memberId,status);
    }
    
    public MemberCouponVO getById(Integer coupon) {
    	return memberCouponRepository.getById(coupon);
    }
    
    @Transactional    
    public MemberCouponVO save(MemberCouponVO coupon) {
    	return memberCouponRepository.save(coupon);
    }

    	
}
