package com.roomTypeImg.model;


import com.roomType.model.RoomTypeVO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_type_img")
public class RoomTypeImgVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_img_id")
    private Integer roomTypeImgId;

    // 多對一：多張圖片對應一個房型
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomTypeVO roomType;

    // picture MEDIUMBLOB
    @Lob
    @Column(name = "picture")
    private byte[] picture;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
    // DB 自動更新，可設定 insertable=false, updatable=false，避免程式端覆寫
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;

    // ---------------------------
    // Constructor, Getter, Setter
    // ---------------------------
    public RoomTypeImgVO() {
    }

    public Integer getRoomTypeImgId() {
        return roomTypeImgId;
    }

    public void setRoomTypeImgId(Integer roomTypeImgId) {
        this.roomTypeImgId = roomTypeImgId;
    }

    public RoomTypeVO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeVO roomType) {
        this.roomType = roomType;
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
