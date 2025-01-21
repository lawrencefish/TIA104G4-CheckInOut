package com.comment.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "hotel_id")
    private Integer hotelId;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "comment_content", columnDefinition = "TEXT")
    private String commentContent;

}
