package com.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class BookingWebDirectController {

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

    @GetMapping("/couponMemberFinal")
    public String couponMemberFinal() {
        return "/user/couponMemberFinal";
    }

    @GetMapping("/faq")
    public String faq() {
        return "/user/faq";
    }

    @GetMapping("/favMember")
    public String favMember() {
        return "/user/favMember";
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

    @GetMapping("/orderedfinal")
    public String orderedFinal() {
        return "/user/orderedfinal";
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
