package com.websocket.endpoint;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.enums.WsType;
import com.websocket.pojo.WebSocketRes;
import lombok.Data;

@Data
public abstract class BaseWsEndpoint {

    ObjectMapper objectMapper = new ObjectMapper();

    private boolean isTokenValidated;

    protected String userId;

    protected <T> WebSocketRes<T> buildSuccessResponse(T data) {
        WebSocketRes<T> response = new WebSocketRes<>();
        response.setType(WsType.SUCCESS.name());
        response.setMessage("");
        response.setResult(data);
        return response;
    }

    protected <T> T parseJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException e) {
            throw new IllegalArgumentException("body 包含無效字段：" + e.getPropertyName());
        } catch (Exception e) {
            throw new IllegalArgumentException("body 格式錯誤");
        }
    }

}