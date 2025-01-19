package com.admin.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coupon.model.CouponRepository;
import com.coupon.model.CouponVO;

@Service
public class AdminCouponService {

    @Autowired
    private CouponRepository couponRepository;

    // 分頁查詢所有優惠券
//    public Page<CouponVO> findAll(Pageable pageable) {
//        return couponRepository.findAll(pageable);
//    }

    // 關鍵字搜尋優惠券(搜尋名稱和描述)
//    public Page<CouponVO> findByKeyword(String keyword, Pageable pageable) {
//        return couponRepository.findByCouponNameContainingOrCouponDetailContaining(
//            keyword, keyword, pageable);
//    }

    // 根據ID查詢單個優惠券
    public CouponVO findById(Integer id) {
        return couponRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("優惠券不存在"));
    }

    // 創建或更新優惠券
    @Transactional
    public CouponVO save(CouponVO coupon) {
        // 驗證日期
        validateCouponDates(coupon);
        // 驗證金額
        validateAmounts(coupon);
        return couponRepository.save(coupon);
    }

    // 刪除優惠券
//    @Transactional
//    public void deleteById(Integer id) {
//        CouponVO coupon = findById(id);
//        // 檢查是否有人已經領取此優惠券
//        if (!coupon.getMemberCoupons().isEmpty()) {
//            throw new RuntimeException("已有會員領取此優惠券，無法刪除");
//        }
//        couponRepository.deleteById(id);
//    }

    // 批次刪除優惠券
//    @Transactional
//    public void deleteByIds(List<Integer> ids) {
//        for (Integer id : ids) {
//            deleteById(id);
//        }
//    }

    // 檢查優惠券日期
    private void validateCouponDates(CouponVO coupon) {
        LocalDateTime now = LocalDateTime.now();
        
        if (coupon.getActiveDate().isBefore(now)) {
            throw new RuntimeException("生效日期不能早於現在");
        }
        
        if (coupon.getExpiryDate().isBefore(coupon.getActiveDate())) {
            throw new RuntimeException("到期日期不能早於生效日期");
        }
    }

    // 檢查優惠券金額
    private void validateAmounts(CouponVO coupon) {
        if (coupon.getMinSpend() <= 0) {
            throw new RuntimeException("最低消費金額必須大於0");
        }
        
        if (coupon.getDiscountAmount() <= 0) {
            throw new RuntimeException("折扣金額必須大於0");
        }
        
        if (coupon.getDiscountAmount() >= coupon.getMinSpend()) {
            throw new RuntimeException("折扣金額不能大於等於最低消費金額");
        }
    }

    // 檢查優惠券是否可用
    public boolean isCouponValid(CouponVO coupon) {
        LocalDateTime now = LocalDateTime.now();
        return coupon.getActiveDate().isBefore(now) && 
               coupon.getExpiryDate().isAfter(now);
    }

//    // 查詢有效的優惠券
//    public Page<CouponVO> findValidCoupons(Pageable pageable) {
//        LocalDateTime now = LocalDateTime.now();
//        return couponRepository.findByActiveDateBeforeAndExpiryDateAfter(
//            now, now, pageable);
//    }
}