package com.contactus.controller;

import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactUsController {
    
    private static final Logger logger = LoggerFactory.getLogger(ContactFormController.class);
    
    @Autowired
    private ContactFormService contactFormService;
    
    @PostMapping("/submit")
    public ResponseEntity<?> submitContactForm(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "hotelName", required = false) String hotelName,
            @RequestParam(value = "checkInDate", required = false) String checkInDate,
            @RequestParam("message") String message,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {
        
        try {
            // 建立 VO
            ContactsVO.ContactFormVOBuilder builder = ContactusVO.builder()
                .name(name)
                .email(email)
                .orderNumber(orderNumber)
                .hotelName(hotelName)
                .message(message);
            
            // 處理日期
            if (checkInDate != null && !checkInDate.isEmpty()) {
                builder.checkInDate(LocalDate.parse(checkInDate));
            }
            
            // 處理照片
            if (photo != null && !photo.isEmpty()) {
                builder.photo(photo.getBytes());
            }
            
            ContactusVO formVO = builder.build();
            
            // 驗證表單
            if (!formVO.isValid()) {
                return ResponseEntity.badRequest()
                    .body("表單驗證失敗：請確認必填欄位皆已填寫且格式正確");
            }
            
            // 儲存表單
            contactFormService.saveContactForm(formVO);
            
            return ResponseEntity.ok("表單提交成功！我們將在三到五個工作天內回覆");
            
        } catch (IOException e) {
            logger.error("處理表單時發生錯誤", e);
            return ResponseEntity.internalServerError()
                .body("處理表單時發生錯誤：" + e.getMessage());
        } catch (Exception e) {
            logger.error("未預期的錯誤", e);
            return ResponseEntity.internalServerError()
                .body("系統發生未預期的錯誤，請稍後再試");
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Contact Form API is running");
    }
}
