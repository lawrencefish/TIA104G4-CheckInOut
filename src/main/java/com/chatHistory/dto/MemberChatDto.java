package com.chatHistory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberChatDto {

    private Long memberId;

    private String memberName;

    private Long latestChatTime;

    private String latestChatMessage;
}
