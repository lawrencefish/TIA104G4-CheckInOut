package com.contactus.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactus.model.ContactUsRepository;
import com.contactus.model.ContactUsVO;

@RestController
@RequestMapping("/api")
public class ContactUsController {
	
	@Autowired
    private ContactUsRepository contactUsRepository;
	
	@PostMapping("/submit")
    public ContactUsVO save(@RequestBody ContactUsVO contact) {
        return contactUsRepository.save(contact);
    }
}