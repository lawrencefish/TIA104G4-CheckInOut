package com.coupon.dto;

import java.time.LocalDateTime;

public class CouponDetailDTO {
	
	private Integer couponId;
    private String couponName;
    private Integer discountAmount;
    private Integer minSpend;
    private Integer travelCityNum;
    private LocalDateTime expiryDate;
    private String couponDetail;
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public Integer getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
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
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCouponDetail() {
		return couponDetail;
	}
	public void setCouponDetail(String couponDetail) {
		this.couponDetail = couponDetail;
	}
    
    
	
	 
	
	 

}
