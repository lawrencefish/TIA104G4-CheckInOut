package com.frequentReply.model;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrequentReplyService {

    @Autowired
    private FrequentReplyRepository frequentReplyRepository; 

    /**
     * 獲取所有常用回覆
     * @return List<FrequentReplyVO>
     */
    public List<FrequentReplyVO> getAllReplies() {
        return frequentReplyRepository.findAll();
    }

    /**
     * 新增或更新常用回覆
     * @param reply FrequentReplyVO
     */
    public void saveOrUpdateReply(FrequentReplyVO reply) {
        frequentReplyRepository.save(reply);
    }

    /**
     * 刪除常用回覆
     * @param id 常用回覆的 ID
     */
    public void deleteReply(Integer id) {
        if (frequentReplyRepository.existsById(id)) {
            frequentReplyRepository.deleteById(id);
        } else {
            throw new RuntimeException("無法刪除，ID 為 " + id + " 的常用回覆不存在");
        }
    }
}