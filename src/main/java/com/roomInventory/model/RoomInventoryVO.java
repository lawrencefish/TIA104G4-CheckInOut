package com.roomInventory.model;


import com.roomType.model.RoomTypeVO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_inventory",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"room_type_id", "date"})
        }
)
public class RoomInventoryVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    // 多對一: room_type_id -> RoomTypeVO
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "必須對應到某個房型")
    private RoomTypeVO roomType;

    // date DATE NOT NULL
    @NotNull(message = "日期不可為空")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    // delete_quantity INT NOT NULL
    @NotNull(message = "刪減數量不可為空")
    @Column(name = "delete_quantity", nullable = false)
    private Integer deleteQuantity;

    // available_quantity INT NOT NULL
    @NotNull(message = "可訂數量不可為空")
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    // ON UPDATE CURRENT_TIMESTAMP NOT NULL
    // 由 DB 自動更新，可設定 insertable=false, updatable=false
    @Column(name = "update_time", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updateTime;

    // -----------------------------------
    // Constructors, Getter, Setter
    // -----------------------------------
    public RoomInventoryVO() {
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public RoomTypeVO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeVO roomType) {
        this.roomType = roomType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDeleteQuantity() {
        return deleteQuantity;
    }

    public void setDeleteQuantity(Integer deleteQuantity) {
        this.deleteQuantity = deleteQuantity;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
