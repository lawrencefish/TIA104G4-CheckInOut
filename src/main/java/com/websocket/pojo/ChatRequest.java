package com.websocket.pojo;

import lombok.Data;

@Data
public class ChatRequest {

    private Long memberId;

    private Long hotelId;

    private String sender;

    private String message;

}
