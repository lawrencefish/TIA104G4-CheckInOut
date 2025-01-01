package com.contactUs.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "contact_us")
public class ContactUsVO {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_us_id")
    private Integer contactUsId;
    
    @Column(name = "member_id")
    private Integer memberId;
    
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "hotel_id")
    private Integer hotelId;
    
    @Column(name = "contact_us_text")
    private String contactUsText;
    
    @Column(name = "create_time")
    private LocalDateTime createTime;
    
    @Lob
    @Column(name = "complaint_pic")
    private byte[] complaintPic;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "review_status")
    private Integer reviewStatus;
    
    @Column(name = "review_time")
    private LocalDateTime reviewTime;
    
    @Column(name = "method")
    private String method;
    
    @Column(name = "reply_content")
    private String replyContent;
    
    @Column(name = "admin_id")
    private Integer adminId;
}

