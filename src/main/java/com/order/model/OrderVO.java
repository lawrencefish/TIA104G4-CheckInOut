package com.order.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import com.orderDetail.model.OrderDetailVO;

@Entity
@Table(name = "orders")
public class OrderVO implements java.io.Serializable {
	
	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderId;
	@Column(name = "create_time")
	private Timestamp createTime;
	@Column(name = "status")
	private byte status;
	@Column(name = "check_in_date", nullable = false)
	@Future
	@NotBlank(message = "入住日期不可為空")
	private Date checkInDate;
	@Column(name = "check_out_date", nullable = false)
	@Future
	@NotBlank(message = "退房日期不可為空")
	private Date checkOutDate;
	@Column(name = "hotel_id", nullable = false)
	@NotBlank(message = "飯店不可為空")
	private Integer hotelId;
	@Column(name = "member_id", nullable = false)
	@NotBlank(message = "會員不可為空")
	private Integer memberId;
	@Column(name = "creditcard_id")
	private Integer creditcardId;
	@Column(name = "member_coupon_id")
	private Integer memberCouponId;
	@Column(name = "total_amount", nullable = false)
	@NotBlank(message = "價格不得為空")
	private Integer totalAmount;
	@Column(name = "guest_last_name", nullable = false)
	@Size(max = 20)
	@NotBlank(message = "名字不得為空")
	private String guestLastName;
	@NotBlank(message = "姓氏不得為空")
	@Column(name = "guest_first_name", nullable = false)
	@Size(max = 20)
	private String guestFirstName;
	@Column(name = "memo")
	private String memo;
	@NotBlank(message = "評分不得為空")
	@Range(min = 1, max = 5)
	@Column(name = "rating", nullable = false)
	private Integer rating;
	@Column(name = "comment_content")
	private String commentContent;
	@Column(name = "comment_reply")
	private String commentReply;
	@Column(name = "comment_create_time")
	private String commentCreateTime;
	// 連接到orderDetail
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderDetailVO> orderDetail;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Date getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getCreditcardId() {
		return creditcardId;
	}

	public void setCreditcardId(Integer creditcardId) {
		this.creditcardId = creditcardId;
	}

	public Integer getMemberCouponId() {
		return memberCouponId;
	}

	public void setMemberCouponId(Integer memberCouponId) {
		this.memberCouponId = memberCouponId;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getGuestLastName() {
		return guestLastName;
	}

	public void setGuestLastName(String guestLastName) {
		this.guestLastName = guestLastName;
	}

	public String getGuestFirstName() {
		return guestFirstName;
	}

	public void setGuestFirstName(String guestFirstName) {
		this.guestFirstName = guestFirstName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentReply() {
		return commentReply;
	}

	public void setCommentReply(String commentReply) {
		this.commentReply = commentReply;
	}

	public String getCommentCreateTime() {
		return commentCreateTime;
	}

	public void setCommentCreateTime(String commentCreateTime) {
		this.commentCreateTime = commentCreateTime;
	}

	public List<OrderDetailVO> getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(List<OrderDetailVO> orderDetail) {
		this.orderDetail = orderDetail;
	}

	@Override
	public String toString() {
		return "OrderVO [orderId=" + orderId + ", createTime=" + createTime + ", status=" + status + ", checkInDate="
				+ checkInDate + ", checkOutDate=" + checkOutDate + ", hotelId=" + hotelId + ", memberId=" + memberId
				+ ", creditcardId=" + creditcardId + ", memberCouponId=" + memberCouponId + ", totalAmount="
				+ totalAmount + ", guestLastName=" + guestLastName + ", guestFirstName=" + guestFirstName + ", memo="
				+ memo + ", rating=" + rating + ", commentContent=" + commentContent + ", commentReply=" + commentReply
				+ ", commentCreateTime=" + commentCreateTime + ", orderDetailID="
				+ orderDetail+ "]";
	}
}
