package com.websocket.endpoint;

import com.chatHistory.dto.MemberChatDto;
import com.chatHistory.service.ChatHistoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.websocket.config.WsEndpointConfigurator;
import com.websocket.pojo.ChatRequest;
import com.websocket.pojo.WebSocketReq;
import com.websocket.pojo.WebSocketRes;
import com.websocket.sessionManager.ChatListSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Set;

@Slf4j
@Scope("prototype")
@ServerEndpoint(value = "/ws/chatList", configurator = WsEndpointConfigurator.class)
@Component
public class ChatListEndpoint extends BaseWsEndpoint {

    @Autowired
    private ChatListSessionManager sessionManager;
    @Autowired
    private ChatHistoryService chatHistoryService;

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket 連線已建立，Session ID: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        WebSocketReq<ChatRequest> req = parseJson(message, new TypeReference<>() {
        });
        ChatRequest chatRequest = req.getMessage();

        sessionManager.addSession(session, chatRequest.getHotelId());

        if ("message".equalsIgnoreCase(req.getAction())) {
            sendMessage(chatRequest.getHotelId());
        } else {
            throw new IllegalArgumentException("action 只能為 message");
        }
    }

    /**
     * 發送消息
     */
    public void sendMessage(Long hotelId) throws Exception {
        List<MemberChatDto> dtoList = chatHistoryService.getMembersWhoChatted(hotelId);
        WebSocketRes<List<MemberChatDto>> response = buildSuccessResponse(dtoList);
        String jsonResponse = objectMapper.writeValueAsString(response);

        Set<Session> sessions = sessionManager.getSessions(hotelId);
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(jsonResponse);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket 發生錯誤，Session ID: {}", session.getId(), throwable);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("WebSocket 連接已關閉，Session ID: {}", session.getId());
        sessionManager.removeSession(session);
    }
}
