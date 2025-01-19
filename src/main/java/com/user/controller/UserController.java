package com.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hotel.model.HotelService;
import com.member.model.MemberService;
import com.member.model.MemberVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
    @Autowired
    private MemberService memberService;
	
    @GetMapping("")
    public String userIndex(HttpSession session, Model model) {
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member != null) {
            String userName = member.getLastName() + member.getFirstName();
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", "訪客"); // 如果未登入，提供默認值
        }
        return "/user/user_index";
    }


    @GetMapping("/chatroom")
    public String chatroom(@RequestParam String userName, Model model, HttpSession session) {
        model.addAttribute("userName", userName);
        return "user/chatroom"; 
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
	
	@GetMapping("/hotel_detail/{id}")
	public String hotelDetail(@PathVariable Integer id) {
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

	@GetMapping("/register")
	public String register() {
		return "/user/register";
	}

	@GetMapping("/search")
	public String search() {
		return "/user/search";
	}

}
