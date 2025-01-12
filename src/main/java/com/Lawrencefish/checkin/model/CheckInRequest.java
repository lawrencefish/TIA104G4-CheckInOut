package com.Lawrencefish.checkin.model;

import lombok.Data;

@Data
public class CheckInRequest {
    private Integer orderId; // 訂單 ID
    private Integer orderDetailId; // 訂單明細 ID
    private Integer assignedRoomId; // 分配的房間 ID
    private String customerName; // 住客姓名
    private String customerPhoneNumber; // 住客電話
}
