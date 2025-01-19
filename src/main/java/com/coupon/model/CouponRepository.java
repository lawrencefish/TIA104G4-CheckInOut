package com.coupon.model;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponVO, Integer> {

	

	
	//List<CouponVO> findByTravelCityNumLessThanEqual(Integer travelCityCount);
	
}
