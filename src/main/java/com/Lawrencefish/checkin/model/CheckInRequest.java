package com.Lawrencefish.checkin.model;

import lombok.Data;

import java.util.List;

@Data
public class CheckInRequest {
    private Integer orderId;
    private Integer orderDetailId;
    private Integer assignedRoomId; // 單一房間分配
    private String customerName;
    private String customerPhoneNumber;

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
