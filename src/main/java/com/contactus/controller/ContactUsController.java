package com.contactus.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.contactus.model.ContactUsService;
import com.contactus.model.ContactUsVO;
import com.member.model.MemberService;
import com.member.model.MemberVO;
import com.order.model.OrderService;
import com.order.model.OrderVO;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {
	
	@Autowired
    private ContactUsService contactUsService;
    
    @Autowired 
    private MemberService memberService;
    
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showContactForm(Model model, HttpSession session) {
        ContactUsVO contactUs = new ContactUsVO();
        model.addAttribute("contactUs", contactUs);
        
        // 檢查是否登入
        Long memberId = (Long) session.getAttribute("memberId");
        model.addAttribute("isLoggedIn", memberId != null);
        
        return "contact/form";
    }

    @PostMapping
    public String submitContact(@ModelAttribute ContactUsVO contactUs,
                              @RequestParam(required = false) MultipartFile complaintPicFile,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            // 檢查會員登入狀態
            Integer memberId = (Integer) session.getAttribute("memberId");
            
            // 如果有訂單或飯店ID但未登入，返回錯誤
            if (memberId == null && (contactUs.getOrder() != null || contactUs.getHotel() != null)) {
                redirectAttributes.addFlashAttribute("error", "需要登入才能關聯訂單或飯店");
                return "redirect:/contact-us";
            }
            
            // 如果已登入，設置會員資訊
            if (memberId != null) {
                MemberVO member = memberService.findById(memberId);
                contactUs.setMember(member);
            }
            
            contactUs.setCreateTime(LocalDateTime.now());
            contactUs.setReviewStatus((byte) 0); // 設置為未審核
            
            // 處理圖片上傳
            if (complaintPicFile != null && !complaintPicFile.isEmpty()) {
                contactUs.setComplaintPic(complaintPicFile.getBytes());
            }
            
//            // 驗證訂單編號
//            if (contactUs.getOrder() != null) {
//                OrderVO order = orderService.findById(contactUs.getOrder().getOrderId());
//                if (order == null || !order.getMember().getMemberId().equals(memberId)) {
//                    redirectAttributes.addFlashAttribute("error", "無效的訂單編號");
//                    return "redirect:/contact-us";
//                }
//            }
            
            contactUsService.save(contactUs);
            redirectAttributes.addFlashAttribute("success", "訊息已送出，我們會盡快處理您的問題");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "送出失敗，請稍後再試");
        }
        
        return "redirect:/contact-us";
    }
    
//    @PostMapping
//    public ResponseEntity<ContactUsResponse> submitForm(
//            @Valid @ModelAttribute ContactUsRequest request,
//            BindingResult bindingResult) {
//            
//        if (bindingResult.hasErrors()) {
//            String errorMessage = bindingResult.getFieldErrors().stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(Collectors.joining(", "));
//            return ResponseEntity.badRequest()
//                .body(ContactUsResponse.error(errorMessage));
//        }
//        
//        try {
//            // 轉換為實體類
//            ContactUsVO contactUs = contactUsMapper.toEntityWithPhoto(request);
//            
//            // 如果有訂單編號，查詢訂單
//            if (StringUtils.hasText(request.getOrderNumber())) {
//                OrderVO order = orderService.findByOrderNumber(request.getOrderNumber())
//                    .orElse(null);
//                contactUs.setOrder(order);
//            }
//            
//            // 如果有飯店名稱，查詢飯店
//            if (StringUtils.hasText(request.getHotelName())) {
//                HotelVO hotel = hotelService.findByName(request.getHotelName())
//                    .orElse(null);
//                contactUs.setHotel(hotel);
//            }
//            
//            // 保存資料
//            ContactUs saved = contactUsService.save(contactUs);
//            
//            return ResponseEntity.ok(ContactUsResponse.success(saved.getContactUsId()));
//            
//        } catch (IOException e) {
//            return ResponseEntity.badRequest()
//                .body(ContactUsResponse.error("圖片處理失敗"));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                .body(ContactUsResponse.error("表單提交失敗"));
//        }
//    }
}
    
// // 管理員查看列表
//    @GetMapping("/admin/list")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String listContacts(Model model,
//                             @RequestParam(defaultValue = "0") int page,
//                             @RequestParam(defaultValue = "10") int size) {
//        Page<ContactUsVO> contacts = contactUsService.findAll(PageRequest.of(page, size));
//        model.addAttribute("contacts", contacts);
//        return "contact/admin-list";
//    }
//    // 管理員處理回覆
//    @PostMapping("/admin/reply/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String replyContact(@PathVariable Integer id,
//                             @RequestParam String replyContent,
//                             @RequestParam Integer reviewStatus,
//                             @AuthenticationPrincipal Admin admin) {
//        ContactUsVO contact = contactUsService.findById(id);
//        contact.setReplyContent(replyContent);
//        contact.setReviewStatus(reviewStatus);
//        contact.setAdmin(admin);
//        contact.setReviewTime(LocalDateTime.now());
//        
//        contactUsService.save(contact);
//        return "redirect:/contact-us/admin/list";
//    }
//	
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

//	// 1. 消費者提交
//    @PostMapping
//    public ContactUsVO createContactUs(@RequestBody ContactUsVO contactUs) {
//        return contactUsService.createContactUs(contactUs);
//    }
//
//    // 2. 查詢單筆
//    @GetMapping("/{id}")
//    public ContactUsVO getContactUs(@PathVariable Integer id) {
//        return contactUsService.getContactUs(id);
//    }
//
//    // 3. 查詢全部 (通常是管理員用)
//    @GetMapping
//    public List<ContactUsVO> getAllContactUs() {
//        return contactUsService.getAllContactUs();
//    }
//
//    // 4. 管理員審核
//    @PostMapping("/{id}/review")
//    public ContactUsVO reviewContactUs(@PathVariable Integer id,
//                                     @RequestParam Byte reviewStatus,
//                                     @RequestParam(required = false) String replyContent,
//                                     @RequestParam Integer adminId) {
//        return contactUsService.reviewContactUs(id, reviewStatus, replyContent, adminId);
//    }

