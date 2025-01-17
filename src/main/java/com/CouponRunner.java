package com;
//import java.time.LocalDateTime;
//
//import org.hibernate.SessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import com.coupon.model.CouponRepository;
//import com.coupon.model.CouponVO;
//
//@SpringBootApplication
//public class CouponRunner implements CommandLineRunner {
//	
//	@Autowired
//    private CouponRepository couponRepository;
//	
//	@Autowired
//	private SessionFactory sessionFactory;
//	
//	public static void main(String[] args) {
//        SpringApplication.run(CouponRunner.class);
//    }
//	
//	@Override
//    public void run(String... args) throws Exception{
//        
//		//新增
//		
//        CouponVO couponVO1 = new CouponVO();
//        couponVO1.setActiveDate(LocalDateTime.of(2025, 1, 10, 0, 0));
//        couponVO1.setExpiryDate(LocalDateTime.of(2025, 2, 10, 0, 0));
//        couponVO1.setCouponName("恭喜成功造訪第一個城市");
//        couponVO1.setMinSpend(1000);
//        couponVO1.setTravelCityNum(1);
//        couponVO1.setCouponDetail("10% off for travel to city 1");
//        couponVO1.setDiscountAmount(100);
//
////        Coupon coupon2 = new Coupon();
////        coupon2.setActiveDate(LocalDateTime.of(2025, 1, 15, 0, 0));
////        coupon2.setExpiryDate(LocalDateTime.of(2025, 3, 15, 0, 0));
////        coupon2.setCouponName("DISCOUNT20");
////        coupon2.setMinSpend(200);
////        coupon2.setTravelCityNum(2);
////        coupon2.setCouponDetail("20% off for travel to city 2");
////        coupon2.setDiscountAmount(20);
////
////        // Save to database
////        couponRepository.save(coupon1);
//        couponRepository.save(couponVO1);
//
//        System.out.println("Coupons initialized successfully!");
//    }
//
//}