package com.contactus.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.contactus.model.ContactUsService;
import com.contactus.model.ContactUsVO;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {
	
	@Autowired
    private ContactUsService contactUsService;
	
//	@PostMapping("/contact")
//    public ResponseEntity<Map<String, Object>> submitContact(
//            @RequestParam String firstName,
//            @RequestParam String email,
//            @RequestParam(required = false) String orderNumber,
//            @RequestParam(required = false) String hotelName,
//            @RequestParam(required = false) String checkInDate,
//            @RequestParam String message,
//            @RequestParam(required = false) MultipartFile photo) {
//        
//        try {
//            ContactForm contactForm = new ContactForm();
//            contactForm.setFirstName(firstName);
//            contactForm.setEmail(email);
//            contactForm.setOrderNumber(orderNumber);
//            contactForm.setHotelName(hotelName);
//            contactForm.setCheckInDate(checkInDate);
//            contactForm.setMessage(message);
//            
//            if (photo != null && !photo.isEmpty()) {
//                // 處理照片上傳
//                String photoUrl = contactService.savePhoto(photo);
//                contactForm.setPhotoUrl(photoUrl);
//            }
//            
//            contactService.saveContactForm(contactForm);
//            
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "表單提交成功");
//            
//            return ResponseEntity.ok(response);
//            
//        } catch (Exception e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", false);
//            response.put("message", "表單提交失敗: " + e.getMessage());
//            
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(response);
//        }
//    }

	// 1. 消費者提交
    @PostMapping
    public ContactUsVO createContactUs(@RequestBody ContactUsVO contactUs) {
        return contactUsService.createContactUs(contactUs);
    }

    // 2. 查詢單筆
    @GetMapping("/{id}")
    public ContactUsVO getContactUs(@PathVariable Integer id) {
        return contactUsService.getContactUs(id);
    }

    // 3. 查詢全部 (通常是管理員用)
    @GetMapping
    public List<ContactUsVO> getAllContactUs() {
        return contactUsService.getAllContactUs();
    }

    // 4. 管理員審核
    @PostMapping("/{id}/review")
    public ContactUsVO reviewContactUs(@PathVariable Integer id,
                                     @RequestParam Byte reviewStatus,
                                     @RequestParam(required = false) String replyContent,
                                     @RequestParam Integer adminId) {
        return contactUsService.reviewContactUs(id, reviewStatus, replyContent, adminId);
    }
}

