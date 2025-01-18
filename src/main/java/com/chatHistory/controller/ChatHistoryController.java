package com.chatHistory.controller;

import com.chatHistory.model.ChatHistory;
import com.chatHistory.pojo.response.MemberChatResponse;
import com.chatHistory.service.ChatHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    /**
     * 測試用，未來可以刪除
     */
    @GetMapping("/chatHistory")
    public List<ChatHistory> getAllChatHistory(){
        return chatHistoryService.getAllChatHistoryByMemberIdAndHotelId(1L, 21L);
    }

    /**
     * 取得有聊天過的所有會員
     */
    @GetMapping("/members")
    public List<MemberChatResponse> getMembersWhoChatted(@RequestParam String hotelId){
        return chatHistoryService.getMembersWhoChatted(hotelId);
    }
}
