package com.membercoupon.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


import com.coupon.model.CouponVO;
import com.member.model.MemberVO;


@Entity
@Table (name = "member_coupon")
public class MemberCouponVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotBlank(message = "不可為空")
	@Column(name= "membercoupon_id")
	private int memberCouponId;
	
	@ManyToOne
	@JoinColumn(name= "member_id", nullable = false)
    private MemberVO member;
	
	@ManyToOne
	@JoinColumn(name= "coupon_id", nullable = false)
    private CouponVO coupon;
	
	@Column(name= "coupon_status", nullable = false)
	@NotNull(message = "狀態不可為空")
    private byte couponStatus; // true = 生效, false = 失效
	
	
	@Column(name= "create_time")
    private Timestamp createTime;

    public int getMemberCouponId() {
        return memberCouponId;
    }

    public void setMemberCouponId(int memberCouponId) {
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

	public byte getCouponStatus() {
		return couponStatus;
	}

	public byte isCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(byte couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
