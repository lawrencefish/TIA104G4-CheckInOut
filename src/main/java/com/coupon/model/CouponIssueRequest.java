package com.coupon.model;

import java.util.List;

public class CouponIssueRequest {
	
	private Integer couponId;
    private List<Integer> memberIds;
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public List<Integer> getMemberIds() {
		return memberIds;
	}
	public void setMemberIds(List<Integer> memberIds) {
		this.memberIds = memberIds;
	}

}
