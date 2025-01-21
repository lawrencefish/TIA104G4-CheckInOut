package com.event;

import org.springframework.context.ApplicationEvent;

import com.member.model.MemberVO;

public class MemberRegisteredEvent  extends ApplicationEvent{
	
    private final MemberVO member;

    public MemberRegisteredEvent(MemberVO member) {
        super(member);
        this.member = member;
    }

    public MemberVO getMember() {
        return member;
    }
}
