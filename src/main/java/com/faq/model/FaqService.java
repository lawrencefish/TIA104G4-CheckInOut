package com.faq.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FaqService {
 
 private final FaqRepository faqRepository;
 
 @Autowired
 public FaqService(FaqRepository faqRepository) {
     this.faqRepository = faqRepository;
 }
 
 // 查詢所有FAQ
 public List<FaqVO> getAllFaqs() {
     return faqRepository.findAll();
 }
 
 // 根據ID查詢單個FAQ
 public Optional<FaqVO> getFaqById(Integer id) {
     return faqRepository.findById(id);
 }
 
 // 創建新FAQ
 public FaqVO createFaq(FaqVO faq) {
     return faqRepository.save(faq);
 }
 
 // 更新FAQ
 public FaqVO updateFaq(FaqVO faq) {
     return faqRepository.save(faq);
 }
 
 // 刪除FAQ
 public void deleteFaq(Integer id) {
     faqRepository.deleteById(id);
 }
}
