package com.faq.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class FaqService {
    
    @Autowired
    private FaqRepository faqRepository;
    
    public List<FaqVO> getAllFaqs() {
        return faqRepository.findAll();
    }
    
    public FaqVO getFaqById(Integer id) {
        return faqRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "FAQ not found"));
    }
    
    public FaqVO createFaq(FaqVO faq) {
        return faqRepository.save(faq);
    }
    
    public FaqVO updateFaq(Integer id, FaqVO faq) {
    	FaqVO existingfaq = getFaqById(id);
        faq.setQuestion(faq.getQuestion());
        faq.setAnswer(faq.getAnswer());
        return faqRepository.save(existingfaq);
    }
    
    public void deleteFaq(Integer id) {
    	if (!faqRepository.existsById(id)) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FAQ NOT FOUND");
    	}
    	faqRepository.deleteById(id);
    }
}