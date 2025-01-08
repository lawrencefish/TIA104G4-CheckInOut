package com.chat.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.employee.model.EmployeeService;
import com.employee.model.EmployeeVO;



@RestController
public class ChatController {

	@Autowired
	EmployeeService empSvc;

	@GetMapping("/member/{userName}/avatar")
	public ResponseEntity<String> getMemberAvatar(@PathVariable String userName) {
		System.out.println(userName);
	    EmployeeVO emp = empSvc.getEmployeeByName(userName);
	    
//	    if(emp != null) {
//	    	System.out.println(emp);
//	    }
//	    byte[] memImg = emp.getMemImg();
//	    
//	    if(memImg == null) {
//	    	System.out.println("memImg is null");
//	    }
//	    if (member != null && member.getMemImg() != null) {
//	        // 將二進制圖片數據轉換為 Base64 字符串
//	        String base64Image = Base64.getEncoder().encodeToString(member.getMemImg());
//	        return ResponseEntity.ok("data:image/png;base64," + base64Image);  // 返回 base64 字符串
//	    }
	    // 返回默認圖片 URL
	    return ResponseEntity.ok("/static/images/default-avatar.png");
	}
	
	
    
    
    
}