package com.chat.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatState {

    private  String type;

    private  String user;

    private List<Map<String, String>> userList;

}