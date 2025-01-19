package com.comment.service;

import com.comment.model.Comment;
import com.comment.pojo.AddCommentRequest;
import com.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}

