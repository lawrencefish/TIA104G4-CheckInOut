package com.chatHistory.service;

import com.chatHistory.model.ChatHistory;
import com.chatHistory.dto.MemberChatDto;
import com.chatHistory.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatHistoryService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    public List<ChatHistory> getAllChatHistoryByMemberIdAndHotelId(Long memberId, Long hotelId) {
        return chatHistoryRepository.findByMemberIdAndHotelId(memberId, hotelId);
    }

    public void create(ChatHistory chatHistory) {
        chatHistoryRepository.save(chatHistory);
    }

    public List<MemberChatDto> getMembersWhoChatted(Long hotelId) {
        List<Object[]> rawResults = chatHistoryRepository.selectAllMembersLatestChat(hotelId);

        List<MemberChatDto> responses = new ArrayList<>();

        for (Object[] row : rawResults) {
            MemberChatDto response = new MemberChatDto(
                    ((BigInteger) row[0]).longValue(),
                    (String) row[1],
                    ((Timestamp) row[3]).getTime(),
                    (String) row[2]

            );
            responses.add(response);
        }
        return responses;
    }
}
