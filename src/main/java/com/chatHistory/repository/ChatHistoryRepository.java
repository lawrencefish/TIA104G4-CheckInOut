package com.chatHistory.repository;

import com.chatHistory.model.ChatHistory;
import org.springframework.data.repository.query.Param;
import com.chatHistory.pojo.response.MemberChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {

    List<ChatHistory> findByMemberIdAndHotelId(Long memberId, Long hotelId);

    @Query(value = """
        SELECT 
            ch.member_id AS memberId, 
            CONCAT(m.last_name, '', m.first_name) AS memberName, 
            ch.message AS latestChatMessage, 
            ch.send_time AS latestChatTime 
        FROM chat_history ch 
        LEFT JOIN member m 
        ON ch.member_id = m.member_id 
        WHERE ch.hotel_id = :hotelId 
        AND ch.send_time = (
            SELECT MAX(inner_ch.send_time) 
            FROM chat_history inner_ch 
            WHERE inner_ch.member_id = ch.member_id 
            AND inner_ch.hotel_id = ch.hotel_id
        ) 
        ORDER BY ch.send_time DESC
        """, nativeQuery = true)
    List<Object[]> selectAllMembersLatestChat(@Param("hotelId") String hotelId);

}
