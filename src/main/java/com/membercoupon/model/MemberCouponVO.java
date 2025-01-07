package com.membercoupon.model;

import java.sql.Timestamp;

public class MemberCouponVO {
    private int memberCouponId;
    private int memberId;
    private int couponId;
    private boolean couponStatus; // true = 生效, false = 失效
    private Timestamp createTime;

    public int getMemberCouponId() {
        return memberCouponId;
    }

    public void setMemberCouponId(int memberCouponId) {
        this.memberCouponId = memberCouponId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public boolean isCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(boolean couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
