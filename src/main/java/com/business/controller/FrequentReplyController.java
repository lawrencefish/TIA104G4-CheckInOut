package com.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.frequentReply.model.FrequentReplyService;
import com.frequentReply.model.FrequentReplyVO;

@Controller
@RequestMapping("/frequentReply")
public class FrequentReplyController {

    @Autowired
    private FrequentReplyService frequentReplyService;

    /**
     * 顯示頁面
     */
    @GetMapping("/frequentReply")
    public String showFrequentReplies() {
        return "business/frequentReply";
    }

    /**
     * 獲取所有回覆資料
     */
    @GetMapping("/all")
    @ResponseBody
    public List<FrequentReplyVO> getAllReplies() {
        return frequentReplyService.getAllReplies();
    }

    /**
     * 儲存或更新回覆
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> saveOrUpdateReply(@RequestBody FrequentReplyVO reply) {
        frequentReplyService.saveOrUpdateReply(reply);
        return ResponseEntity.ok("儲存成功！");
    }

    /**
     * 刪除指定回覆
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteReply(@PathVariable Integer id) {
        frequentReplyService.deleteReply(id);
        return ResponseEntity.ok("刪除成功！");
    }
}
