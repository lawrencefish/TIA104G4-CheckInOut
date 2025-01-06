package com.faq.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.faq.model.FaqService;
import com.faq.model.FaqVO;

public class FaqController {

    @Autowired
    private FaqService faqService;

    @GetMapping
    public List<FaqVO> getAllFaqs() {
        return faqService.getAllFaqs();
    }

    @GetMapping("/{id}")
    public FaqVO getFaqById(@PathVariable int id) {
        return faqService.getFaqById(id);
    }

    @PostMapping
    public FaqVO createFaq(@RequestBody FaqVO faq) {
        return faqService.createFaq(faq);
    }

    @PutMapping("/{id}")
    public FaqVO updateFaq(@PathVariable int id, @RequestBody FaqVO faq) {
        return faqService.updateFaq(id, faq);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable int id) {
        faqService.deleteFaq(id);
    }
}
    