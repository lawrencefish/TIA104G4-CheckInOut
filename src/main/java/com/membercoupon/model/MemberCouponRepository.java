
package com.membercoupon.model;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface MemberCouponRepository extends JpaRepository <MemberCouponVO, Integer> {
	  List<MemberCouponVO> findByCouponStatus(byte status);
	  List<MemberCouponVO> findByMember_MemberId(Integer memberId);
	
	 
	}
	
	
