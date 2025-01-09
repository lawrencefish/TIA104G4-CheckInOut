package com.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ChatMessage {

    private String type;
    private String sender;
    private String receiver;
    private String message;
    private String time;
    private String status;
    
    

    // 無參數構造函數
    public ChatMessage() {
    }

    // 帶參數構造函數
    public ChatMessage(String type, String sender, String receiver, String message, String time, String status) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.status = status;
    }

    // Getter 和 Setter 方法

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 可選：重寫 toString() 方法，便於調試
    @Override
    public String toString() {
        return "ChatMessage{" +
                "type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
