package com.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.member.model.MemberVO;

@Component
public class NewMemberEventListener {
	
	@Async
	@EventListener
	public void handleNewMemberRegistration(MemberRegisteredEvent event) {
		MemberVO member = event.getMember();
		
		
		System.out.println("發送新會員優惠券給" + member.getMemberId());
		
	}
	
	

}
