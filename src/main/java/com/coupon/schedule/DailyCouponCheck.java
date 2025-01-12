package com.coupon.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.coupon.model.CouponRepository;
import com.coupon.model.CouponVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponVO;

//@Component
//public class DailyCouponCheck {
//	
//	@Autowired
//    private MemberCouponRepository couponRepository;
//	
//	@Scheduled(cron = "0 0 1 * * ?")
//    public void executeAtOneAM() {
//		
//		byte byte1=1;
//		List<MemberCouponVO> couponVoList = couponRepository.findByCouponStatus(byte1);
//		for (int i=0; i<couponVoList.size(); i++) {
//			System.out.println(couponVoList.get(i));
//			
//		}
//	           
//		
//        System.out.println("Task executed at 1 AM: " + System.currentTimeMillis());
//        
//        
//    
//	}
//	
	

