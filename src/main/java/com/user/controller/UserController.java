package com.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    private MemberService memberService;

	
	@GetMapping("")
	public String userIndex() {
		return "/user/user_index";
	}

	@GetMapping("/chatroom")
	public String chatroom() {
		return "/user/chatroom";
	}

	@GetMapping("/checkout")
	public String checkout() {
		return "/user/checkout";
	}

	@GetMapping("/contactUs")
	public String contactUs() {
		return "/user/contactUs";
	}

	@GetMapping("/coupon")
	public String coupon() {
		return "/user/coupon";
	}
	
	@GetMapping("/cart")
	public String cart() {
		return "/user/cart";
	}

	@GetMapping("/faq")
	public String faq() {
		return "/user/faq";
	}

	@GetMapping("/favorite")
	public String favorite() {
		return "/user/favorite";
	}

	@GetMapping("/hotel_detail")
	public String hotelDetail() {
		return "/user/hotel_detail";
	}

	@GetMapping("/news_detail")
	public String newsDetail() {
		return "/user/news_detail";
	}

	@GetMapping("/news")
	public String news() {
		return "/user/news";
	}

	@GetMapping("/order")
	public String order() {
		return "/user/order";
	}

	@GetMapping("/profile")
	public String profile() {
		return "/user/profile";
	}

	@GetMapping("/search")
	public String search() {
		return "/user/search";
	}

}
