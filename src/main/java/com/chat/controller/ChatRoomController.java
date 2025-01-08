package com.chat.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.employee.model.EmployeeVO;


@Controller
public class ChatRoomController {
	
	//=========== chatRoom ===========
//	@GetMapping("/fakeLogin")
//	public String fakeLogin(Model mode) {
//		return "front-end/onlinecustomerservice/fakeLogin";
//	}
	
	
	@GetMapping("/chatroom")
	public String chatRoom(Model model, HttpSession session) {
		EmployeeVO empVO = (EmployeeVO) session.getAttribute("auth");
		String userName = empVO.getName(); 
		if (userName != null) {
			model.addAttribute("userName", userName);
		} else {
		    throw new IllegalStateException("Session attribute 'auth' is null.");
		}
		return "User/chatroom";
	}
	
	@GetMapping("/backChatRoom")
	public String backChatRoom(Model mode) {
//		MemVO memVO = (MemVO) session.getAttribute("auths");
//		String userName = memVO.getMemAcct(); 
//		if (userName != null) {
//			model.addAttribute("userName", userName);
//		} else {
//		    throw new IllegalStateException("Session attribute 'auth' is null.");
//		}
		return "business/backChatRoom";
	}
	
}