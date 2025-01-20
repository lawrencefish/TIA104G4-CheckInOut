package com.websocket.endpoint;

import com.websocket.pojo.WebSocketReq;
import com.chatHistory.model.ChatHistory;
import com.chatHistory.service.ChatHistoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.websocket.config.WsEndpointConfigurator;
import com.websocket.pojo.ChatRequest;
import com.websocket.pojo.WebSocketRes;
import com.websocket.sessionManager.ChatSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Slf4j
@Scope("prototype")
@ServerEndpoint(value = "/ws/chat", configurator = WsEndpointConfigurator.class)
@Component
public class ChatEndpoint extends BaseWsEndpoint {

    @Autowired
    private ChatSessionManager sessionManager;
    @Autowired
    private ChatHistoryService chatHistoryService;
    @Autowired
    private ChatListEndpoint chatListEndpoint;

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket 連線已建立，Session ID: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        WebSocketReq<ChatRequest> req = parseJson(message, new TypeReference<>() {
        });
        ChatRequest chatRequest = req.getMessage();

        sessionManager.addSession(session, req.getMessage().getMemberId(), req.getMessage().getHotelId());

        // 第一次連線後發送 history 取得歷史聊天記錄
        if ("history".equalsIgnoreCase(req.getAction())) {
            sendMessage(chatRequest);
        }else if("message".equalsIgnoreCase(req.getAction())) {
            save(chatRequest);
            sendMessage(chatRequest);
            refreshChatList(chatRequest.getHotelId());
        } else {
            throw new IllegalArgumentException("action 只能為 history 或 message");
        }
    }

    /**
     * 發送消息
     */
    private void sendMessage(ChatRequest chatRequest) throws Exception {
        Long memberId = chatRequest.getMemberId();
        Long hotelId = chatRequest.getHotelId();

        List<ChatHistory> chatHistoryList = chatHistoryService.getAllChatHistoryByMemberIdAndHotelId(memberId, hotelId);

        WebSocketRes<List<ChatHistory>> response = buildSuccessResponse(chatHistoryList);

        String jsonResponse = objectMapper.writeValueAsString(response);

        Set<Session> sessions = sessionManager.getSessions(memberId, hotelId);
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

    private void save(ChatRequest request) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setMemberId(request.getMemberId());
        chatHistory.setHotelId(request.getHotelId());
        chatHistory.setMessage(request.getMessage());
        chatHistory.setSender(request.getSender());
        chatHistory.setSendTime(new Timestamp(System.currentTimeMillis()));

        chatHistoryService.create(chatHistory);
    }

    private void refreshChatList(Long hotelId) throws Exception {
        chatListEndpoint.sendMessage(hotelId);
    }

}
