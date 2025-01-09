package com.coupon.model;


import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {
    
	@Autowired
    private CouponRepository couponRepository;
	
	private SessionFactory sessionFactory;
	
	@Transactional
	public void createCoupon(CouponVO couponVo) {
		couponRepository.save(couponVo);
	}
	
	@Transactional
	public void updateCoupon(CouponVO couponVo) {
		couponRepository.save(couponVo);
	}
	public List<CouponVO> getAll() {
		return couponRepository.findAll();
	}
	
}
	
	
	
	
	
	
	
	
	
	
    
    

//    public void issueNewMemberCoupon(Integer memberId) {
//        // 建立新會員優惠券
//        CouponVO newMemberCoupon = new CouponVO();
//        newMemberCoupon.setCouponName("新會員優惠");
//        newMemberCoupon.setMinSpend(1000);
//        newMemberCoupon.setDiscountAmount(100);
//        newMemberCoupon.setActiveDate(LocalDateTime.now());
//        newMemberCoupon.setExpiryDate(LocalDateTime.now().plusMonths(1));
//        
//        CouponVO savedCoupon = couponRepository.save(newMemberCoupon);
//        issueCouponToMember(memberId, savedCoupon.getCouponId());
//    }
//
//    public void checkAndIssueCityBasedCoupon(Integer memberId) {
//        // 獲取會員的所有訂單
//        List<OrderVO> memberOrders = orderRepository.findByMemberId(memberId);
//        
////        // 計算不重複的城市數量
//        Set<String> uniqueCities = new HashSet<>();
//        for (OrderVO order : memberOrders) {
//            uniqueCities.add(order.getHotel().getCity());
//        }
//        
//        int cityCount = uniqueCities.size();
//        
//        // 根據城市數量發放對應優惠券
//        if (cityCount == 1 || cityCount == 3 || cityCount == 5) {
//            List<CouponVO> cityCoupons = couponRepository.findByTravelCityNum(cityCount);
//            for (CouponVO coupon : cityCoupons) {
//                issueCouponToMember(memberId, coupon.getCouponId());
//            }
//        }
//
//    private void issueCouponToMember(Integer memberId, Integer couponId) {
//    	List<MemberCouponVO> memberList = memberCouponRepository.findByMemberId(memberId);
//    	MemberCouponVO memberCoupon = memberList.get(0);
//        MemberCouponVO  memberCoupon = new MemberCouponVO();
//        memberCoupon.setMember(memberId);
//        memberCoupon.setCoupon(couponId);
//        memberCoupon.setCouponStatus(new Byte((byte) 1)); // 設置為生效狀態
//        memberCoupon.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        
//        memberCouponRepository.save(memberCoupon);
//    }
