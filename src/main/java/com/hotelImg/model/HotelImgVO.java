package com.hotelImg.model;

import com.hotel.model.HotelVO;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "hotel_img")
public class HotelImgVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_img_id")
    private Integer hotelImgId;

    // 多對一：多張圖片對應同一家 Hotel
    // hotel_id INT NOT NULL
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @NotNull(message = "必須對應到某家飯店")
    private HotelVO hotel;

    // picture MEDIUMBLOB
    @Lob
    @Column(name = "picture")
    private byte[] picture;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
    // DB 自動更新，可設 insertable=false, updatable=false
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;

    // ----------------------------
    // Constructor, Getter, Setter
    // ----------------------------
    public HotelImgVO() {
    }

    public Integer getHotelImgId() {
        return hotelImgId;
    }

    public void setHotelImgId(Integer hotelImgId) {
        this.hotelImgId = hotelImgId;
    }

    public HotelVO getHotel() {
        return hotel;
    }

    public void setHotel(HotelVO hotel) {
        this.hotel = hotel;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
