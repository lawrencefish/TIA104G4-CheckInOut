package com.websocket.pojo;

import lombok.Data;

@Data
public class WebSocketRes<T> {

    private String type;

    private String message;

    private T result;
}
