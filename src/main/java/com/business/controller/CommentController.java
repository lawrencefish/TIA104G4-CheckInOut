package com.business.controller;

import java.util.List;
import com.order.dto.*;
import com.order.model.*;
import com.comment.model.*;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String getCommentDetail(@RequestParam Integer orderId, Model model) {
        CommentDTO comment = orderService.getCommentById(orderId);
        model.addAttribute("comment", comment);
        return "business/commentDetail";
    }
    
    @PostMapping("/saveReply")
    @ResponseBody
    public ResponseEntity<String> saveReply(@RequestParam Integer orderId, @RequestParam String commentReply) {
        try {
            // 儲存回覆邏輯，假設有一個 service 可以儲存回覆
            orderService.saveReply(orderId, commentReply);

            // 回傳成功訊息
            return ResponseEntity.ok("回覆成功！");
        } catch (Exception e) {
            // 回傳失敗訊息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("回覆失敗：" + e.getMessage());
        }
    }


}
