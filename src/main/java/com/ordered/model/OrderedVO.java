package com.ordered.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderedVO {
	
	public enum OrderStatus {
	    FUTURE("未來訂單"),
	    ONGOING("進行中"),
	    CANCELLED("已取消");

	    private final String description;

	    OrderStatus(String description) {
	        this.description = description;
	    }

	    public String getDescription() {
	        return description;
	    }
	}

	/**
	 * 訂單主要資訊VO
	 */
	public class OrderVO {
	    private String orderId;          // 訂單編號
	    private String hotelName;        // 飯店名稱
	    private String roomType;         // 房型
	    private LocalDateTime checkInDate;  // 入住日期
	    private BigDecimal price;        // 價格
	    private String customerName;     // 客戶姓名
	    private String phoneNumber;      // 聯絡電話
	    private OrderStatus status;      // 訂單狀態
	    private OrderReviewVO review;    // 評論資訊

	    // Getters and Setters
	    public String getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(String orderId) {
	        this.orderId = orderId;
	    }

	    public String getHotelName() {
	        return hotelName;
	    }

	    public void setHotelName(String hotelName) {
	        this.hotelName = hotelName;
	    }

	    public String getRoomType() {
	        return roomType;
	    }

	    public void setRoomType(String roomType) {
	        this.roomType = roomType;
	    }

	    public LocalDateTime getCheckInDate() {
	        return checkInDate;
	    }

	    public void setCheckInDate(LocalDateTime checkInDate) {
	        this.checkInDate = checkInDate;
	    }

	    public BigDecimal getPrice() {
	        return price;
	    }

	    public void setPrice(BigDecimal price) {
	        this.price = price;
	    }

	    public String getCustomerName() {
	        return customerName;
	    }

	    public void setCustomerName(String customerName) {
	        this.customerName = customerName;
	    }

	    public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	    public OrderStatus getStatus() {
	        return status;
	    }

	    public void setStatus(OrderStatus status) {
	        this.status = status;
	    }

	    public OrderReviewVO getReview() {
	        return review;
	    }

	    public void setReview(OrderReviewVO review) {
	        this.review = review;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        OrderVO orderVO = (OrderVO) o;
	        return Objects.equals(orderId, orderVO.orderId);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(orderId);
	    }
	}

	/**
	 * 訂單評論VO
	 */
	public class OrderReviewVO {
	    private String orderId;          // 訂單編號
	    private Integer rating;          // 評分 (1-5星)
	    private String comment;          // 評論內容
	    private LocalDateTime reviewTime; // 評論時間
	    private boolean reviewed;        // 是否已評論

	    // Getters and Setters
	    public String getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(String orderId) {
	        this.orderId = orderId;
	    }

	    public Integer getRating() {
	        return rating;
	    }

	    public void setRating(Integer rating) {
	        if (rating != null && (rating < 1 || rating > 5)) {
	            throw new IllegalArgumentException("Rating must be between 1 and 5");
	        }
	        this.rating = rating;
	    }

	    public String getComment() {
	        return comment;
	    }

	    public void setComment(String comment) {
	        this.comment = comment;
	    }

	    public LocalDateTime getReviewTime() {
	        return reviewTime;
	    }

	    public void setReviewTime(LocalDateTime reviewTime) {
	        this.reviewTime = reviewTime;
	    }

	    public boolean isReviewed() {
	        return reviewed;
	    }

	    public void setReviewed(boolean reviewed) {
	        this.reviewed = reviewed;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        OrderReviewVO reviewVO = (OrderReviewVO) o;
	        return Objects.equals(orderId, reviewVO.orderId);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(orderId);
	    }
	}

	/**
	 * 訂單查詢條件VO
	 */
	public class OrderQueryVO {
	    private String orderId;          // 訂單編號
	    private OrderStatus status;      // 訂單狀態
	    private LocalDateTime startDate; // 查詢起始日期
	    private LocalDateTime endDate;   // 查詢結束日期
	    private String customerName;     // 客戶姓名
	    private String hotelName;        // 飯店名稱

	    // Getters and Setters
	    public String getOrderId() {
	        return orderId;
	    }

	    public void setOrderId(String orderId) {
	        this.orderId = orderId;
	    }

	    public OrderStatus getStatus() {
	        return status;
	    }

	    public void setStatus(OrderStatus status) {
	        this.status = status;
	    }

	    public LocalDateTime getStartDate() {
	        return startDate;
	    }

	    public void setStartDate(LocalDateTime startDate) {
	        this.startDate = startDate;
	    }

	    public LocalDateTime getEndDate() {
	        return endDate;
	    }

	    public void setEndDate(LocalDateTime endDate) {
	        this.endDate = endDate;
	    }

	    public String getCustomerName() {
	        return customerName;
	    }

	    public void setCustomerName(String customerName) {
	        this.customerName = customerName;
	    }

	    public String getHotelName() {
	        return hotelName;
	    }

	    public void setHotelName(String hotelName) {
	        this.hotelName = hotelName;
	    }
	}

}
