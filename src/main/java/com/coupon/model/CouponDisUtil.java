package com.coupon.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CouponDisUtil {
	
    @Autowired
    private CouponService couponService;

    public void distributeNewMemberCoupon(Integer memberId) {
        System.out.println("Distributing new member coupon to member ID: " + memberId);
        couponService.issueNewMemberCoupon(memberId);
    }

    public void distributeTravelCityCoupons(Integer memberId, Integer cityCount) {
        System.out.println("Distributing travel city coupons to member ID: " + memberId + " for " + cityCount + " cities");
        couponService.issueCouponBasedOnTravelCities(memberId, cityCount);
    }
}