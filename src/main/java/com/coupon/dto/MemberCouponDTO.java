package com.coupon.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class MemberCouponDTO {
    private Integer memberCouponId;
    private LocalDateTime createTime;
    private Byte couponStatus;
    private CouponDTO coupon;
    
    // 預設建構子
    public MemberCouponDTO() {
    }
    
    // 帶參數建構子
    public MemberCouponDTO(Integer memberCouponId, LocalDateTime createTime, 
                          Byte couponStatus, CouponDTO coupon) {
        this.memberCouponId = memberCouponId;
        this.createTime = createTime;
        this.couponStatus = couponStatus;
        this.coupon = coupon;
    }
    
    // Getter methods
    public Integer getMemberCouponId() {
        return memberCouponId;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public Byte getCouponStatus() {
        return couponStatus;
    }
    
    public CouponDTO getCoupon() {
        return coupon;
    }
    
    // Setter methods
    public void setMemberCouponId(Integer memberCouponId) {
        this.memberCouponId = memberCouponId;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public void setCouponStatus(Byte couponStatus) {
        this.couponStatus = couponStatus;
    }
    
    public void setCoupon(CouponDTO coupon) {
        this.coupon = coupon;
    }
    
    // 業務方法
    public boolean isActive() {
        return couponStatus == 1 && 
               coupon != null && 
               LocalDateTime.now().isBefore(coupon.getExpiryDate());
    }
    
    // equals 方法
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        MemberCouponDTO that = (MemberCouponDTO) o;
        
        if (!Objects.equals(memberCouponId, that.memberCouponId)) return false;
        if (!Objects.equals(createTime, that.createTime)) return false;
        if (!Objects.equals(couponStatus, that.couponStatus)) return false;
        return Objects.equals(coupon, that.coupon);
    }
    
    // hashCode 方法
    @Override
    public int hashCode() {
        int result = memberCouponId != null ? memberCouponId.hashCode() : 0;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (couponStatus != null ? couponStatus.hashCode() : 0);
        result = 31 * result + (coupon != null ? coupon.hashCode() : 0);
        return result;
    }
    
    // toString 方法
    @Override
    public String toString() {
        return "MemberCouponDTO{" +
                "memberCouponId=" + memberCouponId +
                ", createTime=" + createTime +
                ", couponStatus=" + couponStatus +
                ", coupon=" + coupon +
                '}';
    }
}
