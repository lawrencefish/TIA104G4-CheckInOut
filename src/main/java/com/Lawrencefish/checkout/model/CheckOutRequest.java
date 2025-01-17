package com.Lawrencefish.checkout.model;

import lombok.Data;

import java.util.List;

@Data
public class CheckOutRequest {
    private Integer orderId; // 訂單 ID
    private List<Integer> roomIds; // 房間 ID 列表

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<Integer> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<Integer> roomIds) {
        this.roomIds = roomIds;
    }
}