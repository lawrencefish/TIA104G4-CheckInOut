package com.websocket.pojo;

import lombok.Data;

@Data
public class WebSocketReq<T> {

    private String action;

    private T message;
}
