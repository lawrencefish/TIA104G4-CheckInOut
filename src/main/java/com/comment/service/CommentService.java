package com.comment.service;

import com.comment.model.Comment;
import com.comment.pojo.AddCommentRequest;
import com.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<Map<String, Object>> addComment(Integer memberId, Integer hotelId, AddCommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setHotelId(hotelId);
        comment.setMemberId(memberId);
        comment.setCommentContent(commentRequest.getCommentContent());

        commentRepository.save(comment);

        return ResponseEntity.ok(new HashMap<>());
    }
    public String getCommentContentByHotelAndMember(Integer hotelId, Integer memberId) {
        List<String> comments = commentRepository.findCommentsByHotelIdAndMemberId(hotelId, memberId);
        if (comments.isEmpty()) {
            return "無備註";
        }

        // 處理評論，加上編號並換行
        StringBuilder formattedComments = new StringBuilder();
        for (int i = 0; i < comments.size(); i++) {
            formattedComments.append(i + 1) // 編號從 1 開始
                    .append(". ")
                    .append(comments.get(i))
                    .append("\n"); // 換行符號
        }

        return formattedComments.toString().trim(); // 去掉最後的多餘換行
    }

}

