package com.chatHistory.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="chat_history")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender", nullable = false)
    private String sender;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "hotel_id", nullable = false)
    private Long hotelId;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "send_time", nullable = false, updatable = false)
    private Timestamp sendTime;
}
