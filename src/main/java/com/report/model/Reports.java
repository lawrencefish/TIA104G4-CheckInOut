package com.report.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reports")
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "report_content", columnDefinition = "TEXT")
    private String reportContent;

    @Lob
    @Column(name = "picture")
    private byte[] picture;
}
