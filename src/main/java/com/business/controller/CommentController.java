package com.business.controller;

import java.util.List;
import com.order.dto.*;
import com.order.model.*;
import com.comment.model.*;

import org.springframework.data.domain.Page;
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
    		@RequestParam(defaultValue = "") String clientName,
            @RequestParam(defaultValue = "") String hotelName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Model model) {

        // 呼叫服務層取得分頁評論資料
        Page<CommentDTO> commentPage = orderService.getFilteredComments(clientName, hotelName, page, size);

        // 將分頁數據加入模型
        model.addAttribute("comments", commentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentPage.getTotalPages());
        model.addAttribute("clientName", clientName);
        model.addAttribute("hotelName", hotelName);

        return "business/allComment";
    }


    @GetMapping("/commentDetail")
    public String showCommentDetail() {
        return "business/commentDetail";
    }
}
