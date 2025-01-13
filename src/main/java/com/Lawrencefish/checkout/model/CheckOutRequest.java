package com.Lawrencefish.checkout.model;

import lombok.Data;

import java.util.List;

@Data
public class CheckOutRequest {
    private Integer orderId; // 訂單 ID
    private List<Integer> roomIds; // 房間 ID 列表
}