package com.membercoupon.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.coupon.model.CouponVO;
import com.member.model.MemberVO;

@Entity
@Table(name = "member_coupon")
public class MemberCouponVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberCouponId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberVO member;

    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private CouponVO coupon;

    private Byte couponStatus;
    private Timestamp createTime;
	public Integer getMemberCouponId() {
		return memberCouponId;
	}
	public void setMemberCouponId(Integer memberCouponId) {
		this.memberCouponId = memberCouponId;
	}
	public MemberVO getMember() {
		return member;
	}
	public void setMember(MemberVO member) {
		this.member = member;
	}
	public CouponVO getCoupon() {
		return coupon;
	}
	public void setCoupon(CouponVO coupon) {
		this.coupon = coupon;
	}
	public Byte getCouponStatus() {
		return couponStatus;
	}
	public void setCouponStatus(Byte couponStatus) {
		this.couponStatus = couponStatus;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

    
}
