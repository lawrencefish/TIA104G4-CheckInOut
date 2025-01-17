package com.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
     * 顯示frequentReply.html頁面
     */
    @GetMapping
    public String showFrequentReplies(Model model) {
        List<FrequentReplyVO> replies = frequentReplyService.getAllReplies();
        model.addAttribute("replies", replies);
        return "business/frequentReply"; // 確保模板名稱正確
    }


    /**
     * 獲取所有回覆資料
     */
    @GetMapping("/all")
    @ResponseBody
    public List<FrequentReplyVO> getAllReplies() {
        return frequentReplyService.getAllReplies();
    }
    
    @GetMapping("/all/{id}")
    public ResponseEntity<FrequentReplyVO> getReplyById(@PathVariable Integer id) {
        FrequentReplyVO reply = frequentReplyService.getReplyById(id);
        if (reply == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reply);
    }


    /**
     * 儲存或更新回覆
     */
    @PostMapping("/save")
    @ResponseBody
    public ResponseEntity<String> updateReply(@RequestBody FrequentReplyVO reply) {
        frequentReplyService.saveOrUpdateReply(reply); // 更新既有記錄
        return ResponseEntity.ok("更新成功！");
    }

    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<String> addReply(@RequestBody FrequentReplyVO reply) {
        frequentReplyService.addReply(reply); // 需要在 Service 中實現該方法
        return ResponseEntity.ok("新增成功！");
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
