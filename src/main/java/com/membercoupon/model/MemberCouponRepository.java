package com.membercoupon.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.member.model.MemberVO;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCouponVO, Integer> {
	List<MemberCouponVO> findByCouponStatus(byte status);

	List<MemberCouponVO> findByMember_MemberId(Integer memberId);

    List<MemberCouponVO> findByMember_MemberIdAndCouponStatus(Integer memberId, Byte couponStatus);

	MemberCouponVO getById(Integer memberCouponId);
	// 查詢會員優惠券
	List<MemberCouponVO> findByMember(MemberVO member);

}
