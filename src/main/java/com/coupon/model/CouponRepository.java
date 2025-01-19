package com.coupon.model;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponVO, Integer> {

	

	
	//List<CouponVO> findByTravelCityNumLessThanEqual(Integer travelCityCount);
	
	
	// 管理員查詢使用 查第一個參數紀錄或者第二個參數紀錄 By Barry
    Page<CouponVO> findByCouponNameContainingOrCouponDetailContaining(
        String couponName, 
        String couponDetail, 
        Pageable pageable
    );
}
