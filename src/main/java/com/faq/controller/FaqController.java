package com.faq.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    
    @Autowired
    private FaqService faqService;
    
    @GetMapping
    public ResponseEntity<List<FaqVO>> getAllFaqs() {
        List<FaqVO> faq = faqService.getAllFaqs();
        return ResponseEntity.ok(faqService.getAllFaqs());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FaqVO> getFaqById(@PathVariable Integer id) {
        return ResponseEntity.ok(faqService.getFaqById(id));
    }
    
    @PostMapping
    public ResponseEntity<FaqVO> createFaq(@RequestBody FaqVO faq) {
    	FaqVO createFaq = faqService.createFaq(faq);
        return new ResponseEntity<>(createFaq, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FaqVO> updateFaq(@PathVariable Integer id, @RequestBody FaqVO faq) {
        return ResponseEntity.ok(faqService.updateFaq(id, faq));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Integer id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}

//@RestController
//@RequestMapping("/api/faq")
//public class FaqController {
//
//    @Autowired
//    private FaqService faqService;
//
//    @GetMapping
//    public ResponseEntity<List<FaqVO>> getdAll() {
//        return ResponseEntity.ok(faqService.findAll());
//    }
//
//    @GetMapping("/{id}")
//    public FaqVO getFaqById(@PathVariable int id) {
//        return faqService.getFaqById(id);
//    }
//
//    @PostMapping
//    public ResponseEntity<FaqVO> create(@RequestBody FaqVO faq) {
//        return ResponseEntity.ok(faqService.createFaq(faq));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<FaqVO> update(@PathVariable int id, @RequestBody FaqVO faq) {
//        faq.setFaqId(id);
//        return ResponseEntity.ok(faqService.updateFaq(faq));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable int id) {
//        faqService.deleteFaq(id);
//        return ResponseEntity.ok().build();
//    }
    