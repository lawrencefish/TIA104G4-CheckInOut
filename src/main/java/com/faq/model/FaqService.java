package com.faq.model;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {
	
	
    private FaqRepository faqRepository;

    public List<FaqVO> getAllFaqs() {
        return faqRepository.findAll();
    }

    public FaqVO getFaqById(int id) {
        Optional<FaqVO> faq = faqRepository.findById(id);
        return faq.orElseThrow(() -> new RuntimeException("找不到"));
    }

    public FaqVO createFaq(FaqVO faq) {
        return faqRepository.save(faq);
    }

    public FaqVO updateFaq(int id, FaqVO updatedFaq) {
        FaqVO existingFaq = getFaqById(id);
        existingFaq.setQuestion(updatedFaq.getQuestion());
        existingFaq.setAnswer(updatedFaq.getAnswer());
        return faqRepository.save(existingFaq);
    }

    public void deleteFaq(int id) {
        faqRepository.deleteById(id);
    }
}
