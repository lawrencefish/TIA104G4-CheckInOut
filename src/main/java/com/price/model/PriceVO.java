package com.price.model;

import com.roomType.model.RoomTypeVO;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "price")
public class PriceVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "price_id")
    private Integer priceId;

    // 多對一：對應 room_type (room_type_id)
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeVO roomType;

    // start_date DATE DEFAULT NULL
    @Column(name = "start_date")
    private LocalDate startDate;

    // end_date DATE DEFAULT NULL
    @Column(name = "end_date")
    private LocalDate endDate;

    // price_type TINYINT NOT NULL COMMENT '1=week, 2=weekend, 3=special'
    @Column(name = "price_type", nullable = false)
    private Byte priceType;

    // breakfast_price INT DEFAULT NULL
    @Column(name = "breakfast_price")
    private Integer breakfastPrice;

    // price INT NOT NULL
    @Column(name = "price", nullable = false)
    private Integer price;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    // ON UPDATE CURRENT_TIMESTAMP NOT NULL
    // 建議設 insertable=false, updatable=false，讓 DB 自動維護
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private java.time.LocalDateTime updateTime;

    @Column(name = "remarks")
    private String remarks;

    // Getters and Setters
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // -------------------------------------------
    // Constructors, Getter, Setter
    // -------------------------------------------
    public PriceVO() {
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public RoomTypeVO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeVO roomType) {
        this.roomType = roomType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Byte getPriceType() {
        return priceType;
    }

    public void setPriceType(Byte priceType) {
        this.priceType = priceType;
    }

    public Integer getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(Integer breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
