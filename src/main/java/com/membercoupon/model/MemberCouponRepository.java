
package com.membercoupon.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository <MemberCouponVO, Integer> {
	
	List<MemberCouponVO> findByMemberMemberIdAndCouponStatus(Integer memberId, Byte status);
	
	List<MemberCouponVO> findByMemberMemberId(Integer memberId);

	
	
}
