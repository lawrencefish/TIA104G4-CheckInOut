package com.order.dto;

import java.util.Date;


public class CommentDTO {

    private Integer orderId;
    private String clientLastName;
    private String clientFirstName;
    private String hotelName;
    private Date commentCreateTime;
    private int stars;
    private String commentContent;

    // Constructor
    public CommentDTO(Integer orderId, String clientLastName, String clientFirstName, String hotelName,  Date commentCreateTime, int stars, String commentContent) {
        this.orderId = orderId;
        this.clientLastName = clientLastName;
        this.clientFirstName = clientFirstName;
        this.hotelName = hotelName;
        this.commentCreateTime = commentCreateTime;
        this.stars = stars;
        this.commentContent = commentContent;
    }

    // Getters and setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }
    
    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Date getCommentCreateTime() {
        return commentCreateTime;
    }

    public void setCommentCreateTime(Date commentCreateTime) {
        this.commentCreateTime = commentCreateTime;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
    
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
