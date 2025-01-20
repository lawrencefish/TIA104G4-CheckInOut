package com.frequentReply.controller;

import com.frequentReply.model.FrequentReplyService;
import com.frequentReply.model.FrequentReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/comment/frequentlyReply")
@RestController
public class CommentFrequentlyReplyController {

    @Autowired
    private FrequentReplyService frequentReplyService;

    @GetMapping
    public List<FrequentReplyVO> getAllFrequentlyReply(){
        return frequentReplyService.getAllReplies();
    }
}
