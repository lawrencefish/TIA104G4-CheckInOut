package com.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.coupon.model.CouponService;
import com.member.model.MemberVO;

@Component
public class NewMemberEventListener {

	@Autowired
	private CouponService couponService;

@Async
@EventListener
public void handleNewMemberRegistration(MemberRegisteredEvent event) {
	MemberVO member = event.getMember();
	
	couponService.createWelcomeCouponForMember(member);
	
	
	System.out.println("發送新會員優惠券給" + member.getMemberId());

}



}