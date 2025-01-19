package com.membercoupon.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.member.model.MemberVO;


@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCouponVO, Integer> {
	
	
	List<MemberCouponVO> findByCouponStatus(byte status);
	
	@EntityGraph(attributePaths = {"coupon"})
	List<MemberCouponVO> findByMember_MemberId(Integer memberId);

    List<MemberCouponVO> findByMember_MemberIdAndCouponStatus(Integer memberId, Byte couponStatus);

	MemberCouponVO getById(Integer memberCouponId);
	// 查詢會員優惠券
	List<MemberCouponVO> findByMember(MemberVO member);
	
	boolean existsByMember_MemberIdAndCoupon_CouponId(Integer memberId, Integer couponId);
	List<MemberCouponVO> findByCouponExpiryDateBeforeAndCouponStatus(LocalDateTime date, byte status);
	
	@Query("SELECT mc FROM MemberCouponVO mc JOIN FETCH mc.coupon WHERE mc.member.id = :memberId")
    List<MemberCouponVO> findByMember_MemberIdWithCoupon(@Param("memberId") Integer memberId);
}

