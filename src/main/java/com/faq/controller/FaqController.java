package com.faq.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.faq.model.FaqService;
import com.faq.model.FaqVO;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {
    
    private final FaqService faqService;
    
    @Autowired
    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }
    
    // 獲取所有FAQ
    @GetMapping
    public ResponseEntity<List<FaqVO>> getAllFaqs() {
        List<FaqVO> faqs = faqService.getAllFaqs();
        return ResponseEntity.ok(faqs);
    }
    
    // 根據ID獲取FAQ
    @GetMapping("/{id}")
    public ResponseEntity<FaqVO> getFaqById(@PathVariable Integer id) {
        return faqService.getFaqById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // 創建新FAQ
    @PostMapping
    public ResponseEntity<FaqVO> createFaq(@RequestBody FaqVO faq) {
    	FaqVO createdFaq = faqService.createFaq(faq);
        return ResponseEntity.ok(createdFaq);
    }
    
    // 更新FAQ
    @PutMapping("/{id}")
    public ResponseEntity<FaqVO> updateFaq(@PathVariable Integer id, @RequestBody FaqVO faq) {
        faq.setFaqId(id);
        FaqVO updatedFaq = faqService.updateFaq(faq);
        return ResponseEntity.ok(updatedFaq);
    }
    
    // 刪除FAQ
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Integer id) {
        faqService.deleteFaq(id);
        return ResponseEntity.ok().build();
    }
}