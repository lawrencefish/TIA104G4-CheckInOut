package com.chatHistory.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberChatResponse {

    private Long memberId;

    private String memberName;

    private Long latestChatTime;

    private String latestChatMessage;
}
