package com.websocket.sessionManager;

import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatListSessionManager {

    // 使用複合鍵管理每個聊天室的所有連線
    private final ConcurrentHashMap<String, Set<Session>> conversationSessions = new ConcurrentHashMap<>();

    /**
     * 新增一個 Session 進入指定的對話（由 memberId 和 hotelId 決定）
     */
    public void addSession(Session session, Long hotelId) {
        String key = String.valueOf(hotelId);
        conversationSessions
                .computeIfAbsent(key, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(session);
    }

    /**
     * 根據 memberId 和 hotelId 獲取該聊天室的所有連線
     */
    public Set<Session> getSessions(Long hotelId) {
        String key = String.valueOf(hotelId);
        return conversationSessions.getOrDefault(key, Collections.emptySet());
    }

    /**
     * 當 Session 關閉時，從所有對話中移除該 Session
     */
    public void removeSession(Session session) {
        conversationSessions.values().forEach(sessions -> sessions.remove(session));
    }

}
