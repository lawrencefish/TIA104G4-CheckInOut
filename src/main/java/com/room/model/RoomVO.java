package com.room.model;


import com.roomType.model.RoomTypeVO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@Entity
@Table(name = "room")
public class RoomVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    // 多對一： room_type_id -> RoomTypeVO
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "必須對應到某個房型")
    private RoomTypeVO roomType;

    // number INT NOT NULL (房號/房間編號)
    @NotNull(message = "房間編號不可為空")
    @Column(name = "number", nullable = false)
    private Integer number;

    // 多對一： order_detail_id -> OrderDetailVO (可為 null)
    // 若不需要雙向關係，可不在 OrderDetailVO 上寫 @OneToMany
//    @ManyToOne
//    @JoinColumn(name = "order_detail_id", nullable = true)
//    private OrderDetailVO orderDetail;
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    // customer_name VARCHAR(50) DEFAULT NULL
    @Size(max = 50, message = "住客姓名最多 50 字")
    @Column(name = "customer_name", length = 50)
    private String customerName;

    // customer_phone_number VARCHAR(15) DEFAULT NULL
    @Size(max = 15, message = "住客電話最多 15 字")
    @Column(name = "customer_phone_number", length = 15)
    private String customerPhoneNumber;

    // status TINYINT NOT NULL COMMENT '0=可用, 1=已預訂, 2=維修中'
    @NotNull(message = "狀態不可為空")
    @Column(name = "status", nullable = false)
    private Byte status;

    // -----------------------------------------
    // Constructor, Getter, Setter
    // -----------------------------------------
    public RoomVO() {
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public RoomTypeVO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeVO roomType) {
        this.roomType = roomType;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

//    public OrderDetailVO getOrderDetail() {
//        return orderDetail;
//    }
//
//    public void setOrderDetail(OrderDetailVO orderDetail) {
//        this.orderDetail = orderDetail;
//    }

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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

}
