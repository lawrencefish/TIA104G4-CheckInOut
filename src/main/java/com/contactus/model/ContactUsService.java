package com.contactus.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.member.model.MemberVO;



@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;
    
    
    public ContactUsVO save(ContactUsVO contactUs) {
        return contactUsRepository.save(contactUs);
    }
    
    public Page<ContactUsVO> findAll(Pageable pageable) {
        return contactUsRepository.findAll(pageable);
    }
    
    public ContactUsVO findById(Integer id) {
        return contactUsRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found with id: " + id));
    }
    
    public List<ContactUsVO> findByMember(MemberVO member) {
        return contactUsRepository.findByMemberOrderByCreateTimeDesc(member);
    }
		
	}

//    // 1. 消費者提交: 新增
//    public ContactUsVO createContactUs(ContactUsVO contactUs) {
//        // 預設未審核
//        contactUs.setReviewStatus((byte) 0);
//        contactUs.setCreateTime(new Timestamp(System.currentTimeMillis()));
//        contactUs.setReviewTime(new Timestamp(System.currentTimeMillis())); 
//        // adminId 可留空, 讓後台管理員後續填
//        
//        return contactUsRepository.save(contactUs);
//    }
//
//    // 2. 查詢
//    public ContactUsVO getContactUs(Integer id) {
//        return contactUsRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("ContactUs not found"));
//    }
//
//    // 3. 列出全部 (管理員或系統可用)
//    public List<ContactUsVO> getAllContactUs() {
//        return contactUsRepository.findAll();
//    }
//
//    // 4. 管理員審核
//    public ContactUsVO reviewContactUs(Integer contactUsId, Byte reviewStatus, String replyContent, Integer adminId) {
//        ContactUsVO contactUs = getContactUs(contactUsId);
//
//        // 更新審核狀態
//        contactUs.setReviewStatus(reviewStatus);
//        contactUs.setReplyContent(replyContent);
//        contactUs.setAdminId(adminId);
//        contactUs.setReviewTime(new Timestamp(System.currentTimeMillis())); 
//
//        return contactUsRepository.save(contactUs);
//    }
    
//    @Value("${upload.path}")
//    private String uploadPath;
//    
//    public void saveContactForm(ContactForm contactForm) {
//        contactFormRepository.save(contactForm);
//    }
//    
//    public String savePhoto(MultipartFile photo) throws IOException {
//        String fileName = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
//        Path uploadDir = Paths.get(uploadPath);
//        
//        if (!Files.exists(uploadDir)) {
//            Files.createDirectories(uploadDir);
//        }
//        
//        Path filePath = uploadDir.resolve(fileName);
//        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        
//        return fileName;
   