package com.order.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.orderDetail.model.OrderDetailDTO;

public class OrderDTO {
		private Integer memberId;
	    private Integer orderId;
	    private Date createTime;
	    private byte status;
	    private Date checkInDate;
	    private Date checkOutDate;
	    private Integer totalAmount;
	    private String guestLastName;
	    private String guestFirstName;
	    private String memo;
	    private Integer rating;
	    private String commentContent;
	    private String commentReply;
	    private Date commentCreateTime;
	    private Integer memberCouponId;
	    private Integer hotelId;
	    private String hotelName;
	    private String city;
	    private String district;
	    private String address;
	    private String phoneNumber;
	    private String email;
	    private String creditcardNum;
	    private List<OrderDetailDTO> orderDetails; // 新增這個屬性
	    private Integer discount;


	public OrderDTO(Integer memberId, Integer orderId, Date createTime, byte status, Date checkInDate,
				Date checkOutDate, Integer totalAmount, String guestLastName, String guestFirstName, String memo,
				Integer rating, String commentContent, String commentReply, Date commentCreateTime,
				Integer memberCouponId, Integer hotelId, String hotelName, String city, String district, String address,
				String phoneNumber, String email, String creditcardNum) {
			super();
			this.memberId = memberId;
			this.orderId = orderId;
			this.createTime = createTime;
			this.status = status;
			this.checkInDate = checkInDate;
			this.checkOutDate = checkOutDate;
			this.totalAmount = totalAmount;
			this.guestLastName = guestLastName;
			this.guestFirstName = guestFirstName;
			this.memo = memo;
			this.rating = rating;
			this.commentContent = commentContent;
			this.commentReply = commentReply;
			this.commentCreateTime = commentCreateTime;
			this.memberCouponId = memberCouponId;
			this.hotelId = hotelId;
			this.hotelName = hotelName;
			this.city = city;
			this.district = district;
			this.address = address;
			this.phoneNumber = phoneNumber;
			this.email = email;
			this.creditcardNum = creditcardNum;
			this.orderDetails = null;
			this.discount = 0 ;
		}


	public Integer getMemberId() {
		return memberId;
	}


	public Integer getOrderId() {
		return orderId;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public byte getStatus() {
		return status;
	}


	public Date getCheckInDate() {
		return checkInDate;
	}


	public Date getCheckOutDate() {
		return checkOutDate;
	}


	public Integer getTotalAmount() {
		return totalAmount;
	}


	public String getGuestLastName() {
		return guestLastName;
	}


	public String getGuestFirstName() {
		return guestFirstName;
	}


	public String getMemo() {
		return memo;
	}


	public Integer getRating() {
		return rating;
	}


	public String getCommentContent() {
		return commentContent;
	}


	public String getCommentReply() {
		return commentReply;
	}


	public Date getCommentCreateTime() {
		return commentCreateTime;
	}


	public Integer getMemberCouponId() {
		return memberCouponId;
	}


	public Integer getHotelId() {
		return hotelId;
	}


	public String getHotelName() {
		return hotelName;
	}


	public String getCity() {
		return city;
	}


	public String getDistrict() {
		return district;
	}


	public String getAddress() {
		return address;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public String getCreditcardNum() {
		return creditcardNum;
	}


	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}


	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public void setStatus(byte status) {
		this.status = status;
	}


	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}


	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}


	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}


	public void setGuestLastName(String guestLastName) {
		this.guestLastName = guestLastName;
	}


	public void setGuestFirstName(String guestFirstName) {
		this.guestFirstName = guestFirstName;
	}


	public void setMemo(String memo) {
		this.memo = memo;
	}


	public void setRating(Integer rating) {
		this.rating = rating;
	}


	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}


	public void setCommentReply(String commentReply) {
		this.commentReply = commentReply;
	}


	public void setCommentCreateTime(Date commentCreateTime) {
		this.commentCreateTime = commentCreateTime;
	}


	public void setMemberCouponId(Integer memberCouponId) {
		this.memberCouponId = memberCouponId;
	}


	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}


	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setCreditcardNum(String creditcardNum) {
		this.creditcardNum = creditcardNum;
	}


	public List<OrderDetailDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}


	public Integer getDiscount() {
		return discount;
	}


	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
}