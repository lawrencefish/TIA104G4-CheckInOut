package com.coupon.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<CouponVO, Integer> {
	List<CouponVO> findByTravelCityNum(Integer travelCityNum);

}
