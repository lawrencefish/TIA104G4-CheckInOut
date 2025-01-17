package com.Lawrencefish.checkin.model;

import lombok.Data;

@Data
public class CheckInRequest {
    private Integer orderId; // 訂單 ID
    private Integer orderDetailId; // 訂單明細 ID
    private Integer assignedRoomId; // 分配的房間 ID
    private String customerName; // 住客姓名
    private String customerPhoneNumber; // 住客電話

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getAssignedRoomId() {
        return assignedRoomId;
    }

    public void setAssignedRoomId(Integer assignedRoomId) {
        this.assignedRoomId = assignedRoomId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }
}
