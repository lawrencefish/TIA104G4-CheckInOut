package com.contactus.model;

import javax.persistence.*;

import com.admin.model.Admin;
import com.hotel.model.HotelVO;
import com.member.model.MemberVO;
import com.order.model.OrderVO;

import java.sql.Timestamp;
import java.time.LocalDateTime;
	
	@Entity
	@Table(name = "contact_us")
	public class ContactUsVO {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "contact_us_id")
	    private Integer contactUsId;

	    // 外鍵到 orders
	    @ManyToOne
	    @JoinColumn(name = "order_id") 
	    private OrderVO order;

	    // 外鍵到 hotel
	    @ManyToOne
	    @JoinColumn(name = "hotel_id")
	    private HotelVO hotel;

	    // 外鍵到 member
	    @ManyToOne
	    @JoinColumn(name = "member_id")
	    private MemberVO member;

	    // 這裡直接存文字內容，不一定非要做成關聯
	    @Column(name = "contact_us_text", nullable = false, columnDefinition = "TEXT")
	    private String contactUsText;

	    @Column(name = "create_time", nullable = false)
	    private LocalDateTime createTime;

	    // 客訴圖片 (如果不想在Entity存byte[]，可考慮只存路徑或改其他型態)
	    @Lob
	    @Column(name = "complaint_pic")
	    private byte[] complaintPic;

	    @Column(name = "email", length = 100, nullable = false)
	    private String email;

	    // 0=未審核, 1=已處理, 2=不處理
	    @Column(name = "review_status", nullable = false)
	    private Byte reviewStatus;

	    @Column(name = "review_time", nullable = false)
	    private Timestamp reviewTime;

	    @Column(name = "reply_content", columnDefinition = "TEXT")
	    private String replyContent;

	    // 外鍵到 admin
	    @ManyToOne
	    @JoinColumn(name = "admin_id", nullable = false)
	    private Admin admin;

		public Integer getContactUsId() {
			return contactUsId;
		}

		public void setContactUsId(Integer contactUsId) {
			this.contactUsId = contactUsId;
		}

		public OrderVO getOrder() {
			return order;
		}

		public void setOrder(OrderVO order) {
			this.order = order;
		}

		public HotelVO getHotel() {
			return hotel;
		}

		public void setHotel(HotelVO hotel) {
			this.hotel = hotel;
		}

		public MemberVO getMember() {
			return member;
		}

		public void setMember(MemberVO member) {
			this.member = member;
		}

		public String getContactUsText() {
			return contactUsText;
		}

		public void setContactUsText(String contactUsText) {
			this.contactUsText = contactUsText;
		}

//		public Timestamp getCreateTime() {
//			return createTime;
//		}
//
//		public void setCreateTime(Timestamp createTime) {
//			this.createTime = createTime;
//		}

		public byte[] getComplaintPic() {
			return complaintPic;
		}

		public void setComplaintPic(byte[] complaintPic) {
			this.complaintPic = complaintPic;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Byte getReviewStatus() {
			return reviewStatus;
		}

		public void setReviewStatus(Byte reviewStatus) {
			this.reviewStatus = reviewStatus;
		}

		public Timestamp getReviewTime() {
			return reviewTime;
		}

		public void setReviewTime(Timestamp reviewTime) {
			this.reviewTime = reviewTime;
		}

		public String getReplyContent() {
			return replyContent;
		}

		public void setReplyContent(String replyContent) {
			this.replyContent = replyContent;
		}

		public Admin getAdmin() {
			return admin;
		}

		public void setAdmin(Admin admin) {
			this.admin = admin;
		}
		
		public Admin getAdminId() {
			return admin;
		}

		public void setAdminId(Integer adminId) {
			// TODO Auto-generated method stub
			
		}

		public void setCreateTime(LocalDateTime now) {
			// TODO Auto-generated method stub
			
		}
	}



