
package com.membercoupon.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.member.model.MemberVO;

public interface MemberCouponRepository extends JpaRepository <MemberCouponVO, Integer> {
	
	List<MemberCouponVO> findByMemberMemberIdAndCouponStatus(Integer memberId, Byte status);
	
	List<MemberCouponVO> findByMemberMemberId(Integer memberId);


	// 查詢會員優惠券
    List<MemberCouponVO> findByMember(MemberVO member);
    
    // 查詢會員有效優惠券
    @Query("SELECT mc FROM MemberCouponVO mc WHERE mc.member = :member AND mc.couponStatus = 1")
    List<MemberCouponVO> findValidCoupons(@Param("member") MemberVO member);
    
 // 查詢會員有效的優惠券(狀態為1)
    List<MemberCouponVO> findByMemberAndCouponStatus(MemberVO member, Byte status);

    // 檢查是否已經擁有特定類型的優惠券
    @Query("SELECT COUNT(*) > 0 FROM MemberCouponVO mc " +
           "WHERE mc.member.memberId = :memberId " +
           "AND mc.coupon.travelCityNum = :cityNum " +
           "AND mc.couponStatus = 1")
    boolean existsByMemberAndTravelCityNum(
        @Param("memberId") Integer memberId, 
        @Param("cityNum") Integer cityNum
    );
	
	
}
