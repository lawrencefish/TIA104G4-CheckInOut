package com.chat.controller;

import java.util.ArrayList;
import java.util.List;

import com.chat.model.ChatMessage;
import com.chat.util.JedisUtil;
import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisHandleMessage {

    private static final JedisPool jedisPool = JedisUtil.getJedisPool(); // 使用連接池
    private static final Gson gson = new Gson();

    /**
     * 從 Redis 中獲取聊天訊息的歷史記錄
     *
     * @param sender   發送者
     * @param receiver 接收者
     * @return 聊天歷史記錄
     */
    public static List<String> getHistoryMsg(String sender, String receiver) {
        String key = sender + ":" + receiver;
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, 0, -1);
        }
    }

    /**
     * 將指定發送者和接收者之間的聊天訊息標記為已讀
     */
    public static void readAll(String sender, String receiver) {
        String key = sender + ":" + receiver;
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> historyData = jedis.lrange(key, 0, -1);
            jedis.del(key);
            for (String oneChat : historyData) {
                ChatMessage cm = gson.fromJson(oneChat, ChatMessage.class);
                if (cm.getStatus() != null) {
                    cm.setStatus("read");
                }
                jedis.rpush(key, gson.toJson(cm));
            }
        }
    }

    /**
     * 保存聊天訊息到 Redis
     *
     * @param message 聊天訊息
     */
    public static void saveChatMessage(ChatMessage message) {
        try (Jedis jedis = jedisPool.getResource()) {
            if ("host".equals(message.getReceiver())) {
                jedis.rpush("host:" + message.getSender(), gson.toJson(message));
                jedis.rpush("member:" + message.getSender(), gson.toJson(message));
            } else {
                jedis.rpush("host:" + message.getReceiver(), gson.toJson(message));
                jedis.rpush("member:" + message.getReceiver(), gson.toJson(message));
            }
        }
    }

    /**
     * 獲得聊天室清單
     *
     * @return 聊天室清單
     */
    public static List<String> getChatRoomList() {
        List<String> allKey = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            for (String key : jedis.keys("host*")) {
                if ("list".equals(jedis.type(key))) {
                    allKey.add(key.replace("host:", ""));
                }
            }
        }
        return allKey;
    }

    /**
     * 客服獲取會員聊天訊息
     *
     * @param member 會員名稱
     * @return 聊天歷史記錄
     */
    public static List<String> getHostMemberMsg(String member) {
        String key = "host:" + member;
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrange(key, 0, -1);
        }
    }

    /**
     * 客服獲取會員最後聊天訊息
     *
     * @param member 會員名稱
     * @return 最後一條聊天訊息
     */
    public static ChatMessage getHostMemberLastMsg(String member) {
        String key = "host:" + member;
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> lastRow = jedis.lrange(key, -1, -1);
            if (lastRow != null && !lastRow.isEmpty()) {
                return gson.fromJson(lastRow.get(0), ChatMessage.class);
            }
        }
        return null;
    }
}