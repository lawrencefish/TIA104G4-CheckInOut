package com.coupon.model;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactVO {
	 // Required fields
    private String name;        // 姓名
    private String email;       // 信箱  
    private String message;     // 請輸入內容
    
    // Optional fields
    private String orderNumber; // 訂單編號
    private String hotelName;   // 飯店名
    private LocalDate checkInDate; // 入住日期
    private byte[] photo;       // 上傳照片
    
    // Validation methods
    public boolean isValid() {
        return isNameValid() && 
               isEmailValid() && 
               isMessageValid();
    }
    
    private boolean isNameValid() {
        return name != null && !name.trim().isEmpty();
    }
    
    private boolean isEmailValid() {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email format validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }
    
    private boolean isMessageValid() {
        return message != null && !message.trim().isEmpty();
    }
    
    // Custom toString to avoid printing large photo data
    @Override
    public String toString() {
        return "ContactFormVO{" +
               "name='" + name + '\'' +
               ", email='" + email + '\'' +
               ", orderNumber='" + orderNumber + '\'' +
               ", hotelName='" + hotelName + '\'' +
               ", checkInDate=" + checkInDate +
               ", message='" + message + '\'' +
               ", hasPhoto=" + (photo != null) +
               '}';
    }
}

