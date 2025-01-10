package com.business.controller;

import java.util.List;
import com.order.dto.*;
import com.order.model.*;
import com.comment.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/comment")
public class CommentController {

	private final OrderService orderService;

    // 通過構造函數注入 OrderService
    public CommentController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    @GetMapping("")
    public String showComment() {
        return "redirect:/comment/allComment";
    }

    @GetMapping("/allComment")
    public String showAllComment(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String hotelName,
            @RequestParam(required = false, defaultValue = "rating_desc") String sort,
            Model model) {

        // 如果沒有搜尋條件，調用獲取所有評論的方法
        List<CommentDTO> comments;
        if (clientName == null && hotelName == null) {
            comments = orderService.getAllComments();
        } else {
            // 有搜尋條件時調用過濾方法
            comments = orderService.getFilteredComments(clientName, hotelName, sort);
        }

        // 將評論數據添加到模型中
        model.addAttribute("comments", comments);
        return "business/allComment";
    }

    @GetMapping("/commentDetail")
    public String showCommentDetail() {
        return "business/commentDetail";
    }
}
