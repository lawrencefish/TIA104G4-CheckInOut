package com.coupon.dto;

import java.time.LocalDateTime;

public class CouponDTO {
	
	private Integer couponId;
    private LocalDateTime createTime;
    private LocalDateTime activeDate;
    private LocalDateTime expiryDate;
    private String couponName;
    private Integer minSpend;
    private Integer travelCityNum;
    private String couponDetail;
    private Integer discountAmount;
	public Integer getCouponId() {
		return couponId;
		
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	public LocalDateTime getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(LocalDateTime activeDate) {
		this.activeDate = activeDate;
	}
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Integer getMinSpend() {
		return minSpend;
	}
	public void setMinSpend(Integer minSpend) {
		this.minSpend = minSpend;
	}
	public Integer getTravelCityNum() {
		return travelCityNum;
	}
	public void setTravelCityNum(Integer travelCityNum) {
		this.travelCityNum = travelCityNum;
	}
	public String getCouponDetail() {
		return couponDetail;
	}
	public void setCouponDetail(String couponDetail) {
		this.couponDetail = couponDetail;
	}
	public Integer getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
	}
    
    
}

