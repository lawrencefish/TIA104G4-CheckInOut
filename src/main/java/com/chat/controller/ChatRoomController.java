package com.chat.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee.model.EmployeeVO;
import com.hotel.model.HotelVO;
import com.member.model.MemberVO;


@Controller
public class ChatRoomController {
	
	@GetMapping("/chatroom")
	public String chatRoom(@RequestParam String hotel, Model model, HttpSession session) {
	    MemberVO memVO = (MemberVO) session.getAttribute("member");
	    if (memVO != null) {
	        String userName = memVO.getLastName() + memVO.getFirstName();
	        model.addAttribute("userName", userName);
	    } else {
	        System.out.println("Member not found in session.");
	        model.addAttribute("userName", "Unknown User");
	    }

	    model.addAttribute("hotel", hotel);
	    System.out.println("Hotel: " + hotel);

	    return "user/chatroom";
	}
	
	@GetMapping("/hotel_detail")
	public String hotelDetail(Model model, HttpSession session) {
	    // 獲取當前登錄的客戶資訊
	    MemberVO member = (MemberVO) session.getAttribute("member");
	    if (member != null) {
	        String userName = member.getLastName() + member.getFirstName();
	        model.addAttribute("userName", userName);
	        System.out.println("UserName: " + userName);
	    } else {
	        System.out.println("Member not found in session.");
	        model.addAttribute("userName", "Guest"); 
	    }
	    
	    HotelVO hotel = (HotelVO) session.getAttribute("hotel");
	    if (hotel != null) {
	        model.addAttribute("hotelName", hotel.getName());
	        System.out.println("HotelName: " + hotel.getName());
	    } else {
	        System.out.println("Hotel not found in session.");
	        model.addAttribute("hotelName", "Unknown Hotel"); 
	    }
	    return "user/hotel_detail"; 
	}
	
	@GetMapping("/backChatRoom")
	public String backChatRoom(Model model, HttpSession session) {
//		MemberVO memVO = (MemberVO) session.getAttribute("member");
//		String userName = memVO.getLastName() +memVO.getFirstName(); 
//		model.addAttribute("userName", userName);
		return "business/backChatRoom";
	}
	
}