package com.contactus.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    public ContactUsVO save(ContactUsVO contact) {
        contact.setCreated_at(LocalDateTime.now());
        contact.setProcessed_at(LocalDateTime.now());
        contact.setStatus("0");  // 設置默認狀態
        
        return contactUsRepository.save(contact);
    }
}
//管理員
//    public List<ContactUsVO> getContacts(String status, String date) {
//        if (status != null && !status.isEmpty()) {
//            return contactUsRepository.findByStatus(status);
//        }
//        if (date != null && !date.isEmpty()) {
//            LocalDateTime startDate = LocalDate.parse(date).atStartOfDay();
//            LocalDateTime endDate = startDate.plusDays(1);
//            return contactUsRepository.findByCreatedAtBetween(startDate, endDate);
//        }
//        return contactUsRepository.findAll();
//    }
//    
//    public ContactUsVO getContactById(Integer id) {
//        return contactUsRepository.findById(id)
//            .orElseThrow(() -> new RuntimeException("Contact not found"));
//    }
//    
//    public void reviewContact(ContactReviewDTO dto) {
//        ContactUsVO contact = getContactById(dto.getContact_us_id());
//        contact.setStatus(dto.getStatus());
//        contact.setReply(dto.getReply());
//        contact.setAdmin(dto.getAdmin_id());
//        contact.setProcessed_at(LocalDateTime.now());
//        contactUsRepository.save(contact);
//    }
//}
        
        // 如果需要寄送email通知
//        if ("1".equals(dto.getStatus())) {
//            sendReplyEmail(contact);
//        }
//    }
//    
    //private void sendReplyEmail(ContactUs contact) {
        // 實作email寄送邏輯
    
   

   