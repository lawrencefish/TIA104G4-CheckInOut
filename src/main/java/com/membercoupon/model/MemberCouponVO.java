package com.membercoupon.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.coupon.model.CouponVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.member.model.MemberVO;

@Entity
@Table(name = "member_coupon")
public class MemberCouponVO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_coupon_id")
    private Integer memberCouponId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private MemberVO member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "coupon_id", nullable = false)
    private CouponVO coupon;

    @Column(name = "coupon_status", nullable = false)
    private Byte couponStatus;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime = LocalDateTime.now();

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

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	
	public boolean isValid() {
        return this.couponStatus == 1 && 
               this.coupon.getExpiryDate().isAfter(LocalDateTime.now());
    }
}
    
    

    
    