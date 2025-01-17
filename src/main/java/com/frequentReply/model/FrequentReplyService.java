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

    public FrequentReplyVO getReplyById(Integer id) {
        return frequentReplyRepository.findById(id).orElse(null);
    }
    
    /**
     * 更新常用回覆
     * @param reply FrequentReplyVO
     */
    public void saveOrUpdateReply(FrequentReplyVO reply) {
        frequentReplyRepository.save(reply);
    }

    public void addReply(FrequentReplyVO reply) {
        reply.setReplyId(null); // 或依據資料庫的自動生成策略
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
