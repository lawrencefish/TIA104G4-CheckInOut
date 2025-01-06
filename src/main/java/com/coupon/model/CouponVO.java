package com.coupon.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupon")
public class CouponVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Integer couponId;

    @Column(name = "create_time", nullable = false, updatable = false, 
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(name = "active_date", nullable = false)
    private LocalDateTime activeDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "coupon_name", length = 11, nullable = false)
    private String couponName;

    @Column(name = "min_spend", nullable = false)
    private Integer minSpend;

    @Column(name = "travel_city_num", nullable = false)
    private Integer travelCityNum;

    @Column(name = "coupon_detail", columnDefinition = "TEXT", nullable = false)
    private String couponDetail;

    @Column(name = "discount_amount", nullable = false)
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
