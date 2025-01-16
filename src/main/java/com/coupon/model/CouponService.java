//package com.coupon.model;
//
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.member.model.MemberService;
//import com.member.model.MemberVO;
//import com.membercoupon.model.MemberCouponRepository;
//import com.membercoupon.model.MemberCouponVO;
//import com.order.model.OrderRepository;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//@Transactional
//public class CouponService {
//
//    @Autowired
//    private CouponRepository couponRepository;
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private MemberCouponRepository memberCouponRepository;
//
//    @Autowired
//    private MemberService memberService;
//
//	private SessionFactory sessionFactory;
//
//
//	@Transactional
//	public void createCoupon(CouponVO couponVo) {
//		couponRepository.save(couponVo);
//	}
//
//	@Transactional
//	public void updateCoupon(CouponVO couponVo) {
//		couponRepository.save(couponVo);
//	}
//	public List<CouponVO> getAll() {
//		return couponRepository.findAll();
//	}
//
//	//新會員註冊
//
//	public void issueNewMemberCoupon(MemberVO member) {
//		try {
//			CouponVO coupon = new CouponVO();
//			coupon.setCouponName("新會員優惠券");
//			coupon.setActiveDate(LocalDateTime.now());
//			coupon.setExpiryDate(LocalDateTime.now().plusMonths(1));
//			coupon.setMinSpend(1000);
//			coupon.setTravelCityNum(0);
//			coupon.setCouponDetail("新會員註冊即得訂單滿1000即可折300");
//			coupon.setDiscountAmount(300);
//
//			CouponVO savedCoupon =couponRepository.save(coupon);
//
//			MemberCouponVO memberCoupon = new MemberCouponVO();
//			memberCoupon.setMember(member);
//			memberCoupon.setCoupon(savedCoupon);
//			memberCoupon.setCouponStatus((byte)1);
//
//			memberCouponRepository.save(memberCoupon);
//			System.out.println("新會員優惠券發送成功 ID=" + member.getMemberId());
//
//		} catch (Exception e) {
//			System.out.println("新會員優惠券發送失敗" + e.getMessage());
//		}
//		}
//		// 檢查並發放城市優惠券
//	    public void checkAndIssueCityCouponsAfterCheckout(Integer memberId) {
//	        try {
//	            // 取得不重複的城市清單
//	            List<String> cities = orderRepository.findDistinctCitiesByMember(memberId);
//
//	            System.out.println("會員ID: " + memberId + " 已完成退房的不重複城市數量: " + cities.size());
//
//	            if (cities.isEmpty()) {
//	                return;
//	            }
//
//	            // 根據不重複城市數量發放優惠券
//	            int cityCount = cities.size();
//	            if (cityCount >= 5) {
//	                issueCityCoupon(memberId, 5);
//	            }
//	            if (cityCount >= 3) {
//	                issueCityCoupon(memberId, 3);
//	            }
//	            if (cityCount >= 1) {
//	                issueCityCoupon(memberId, 1);
//	            }
//
//	        } catch (Exception e) {
//	            System.out.println("城市優惠券檢查發放失敗: " + e.getMessage());
//	        }
//	    }
//
//	    private void issueCityCoupon(Integer memberId, Integer cityNum) {
//	        try {
//	                // 檢查是否已經有這個城市數量的優惠券
//	                boolean hasExistingCoupon = memberCouponRepository.existsByMemberAndTravelCityNum(
//	                    memberId, cityNum);
//
//	                if (hasExistingCoupon) {
//	                    System.out.println("會員: " + memberId + " 已經有 " + cityNum + "城市優惠券");
//	                    return;
//	        }
//	            MemberVO member = memberService.getOneMem(memberId);
//	            if (member == null) {
//	                return;
//	            }
//
//	            // 建立優惠券
//	            CouponVO coupon = new CouponVO();
//	            coupon.setCouponName(cityNum + "城優惠");
//	            coupon.setActiveDate(LocalDateTime.now());
//	            coupon.setExpiryDate(LocalDateTime.now().plusMonths(1));  // 效期一個月
//	            coupon.setMinSpend(cityNum * 1000);
//	            coupon.setTravelCityNum(cityNum);
//	            coupon.setCouponDetail(cityNum + "個城市旅遊達成優惠券");
//	            coupon.setDiscountAmount(cityNum * 150);
//
//	            CouponVO savedCoupon = couponRepository.save(coupon);
//
//	            // 發放優惠券
//	            MemberCouponVO memberCoupon = new MemberCouponVO();
//	            memberCoupon.setMember(member);
//	            memberCoupon.setCoupon(savedCoupon);
//	            memberCoupon.setCouponStatus((byte) 1);
//	            memberCouponRepository.save(memberCoupon);
//
//	            System.out.println("已發放" + cityNum + "城市優惠券給會員: " + memberId);
//
//	        } catch (Exception e) {
//	            System.out.println(cityNum + "城市優惠券發放失敗: " + e.getMessage());
//	        }
//	    }
//    }
//
//
