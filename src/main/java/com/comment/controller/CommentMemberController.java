package com.comment.controller;

import com.comment.pojo.AddCommentRequest;
import com.comment.service.CommentService;
import com.hotel.model.HotelVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/comment/member")
public class CommentMemberController {

    @Autowired
    private CommentService commentService;

    /**
     * 評論客戶
     */
    @PostMapping(value = "/{memberId}")
    public ResponseEntity<Map<String,Object>> addComment(HttpServletRequest request,
                                                           @PathVariable Integer memberId,
                                                           @RequestBody AddCommentRequest commentRequest) throws IOException {
        HotelVO hotel = (HotelVO) request.getSession().getAttribute("hotel");

        return commentService.addComment(memberId, hotel.getHotelId(), commentRequest);
    }
}
