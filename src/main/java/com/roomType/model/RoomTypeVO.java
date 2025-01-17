package com.roomType.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.model.HotelVO;
import com.roomTypeImg.model.RoomTypeImgVO;
import com.price.model.PriceVO;
import com.room.model.RoomVO;
import com.roomInventory.model.RoomInventoryVO;
import com.roomTypeFacility.model.RoomTypeFacilityVO;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "room_type")
public class RoomTypeVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private Integer roomTypeId;

    // hotel_id INT NOT NULL
    // 多對一: 多個 RoomType 對應 一個 Hotel
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelVO hotel;

    // room_name VARCHAR(50) NOT NULL
    @NotBlank(message = "房型名稱不可為空")
    @Size(max = 50, message = "房型名稱最多 50 字")
    @Column(name = "room_name", length = 50, nullable = false)
    private String roomName;

    // max_person INT NOT NULL
    @NotNull(message = "最多可入住人數不可為空")
    @Min(value = 1, message = "最多可入住人數至少為 1")
    @Column(name = "max_person", nullable = false)
    private Integer maxPerson;

    // room_num INT NOT NULL
    @NotNull(message = "房型總數量不可為空")
    @Min(value = 1, message = "房型至少要有 1 間")
    @Column(name = "room_num", nullable = false)
    private Integer roomNum;

    // breakfast TINYINT NOT NULL COMMENT '0=無, 1=有'
    @NotNull(message = "是否附早餐不可為空")
    @Column(name = "breakfast", nullable = false)
    private Byte breakfast;  // 0=無, 1=有

    // status TINYINT NOT NULL COMMENT '0=待審核, 1=審核通過, 2=審核不通過'
    @NotNull(message = "狀態不可為空")
    @Column(name = "status", nullable = false)
    private Byte status;     // 0=待審核, 1=審核通過, 2=審核不通過

    // review_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    @Column(name = "review_time", insertable = false)
    private Timestamp reviewTime;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL
    @Column(name = "update_time", insertable = false, updatable = false)
    private Timestamp updateTime;


    // -------------------------------------------------------------------
    // 一對多關係 (可選) - 若想在 RoomTypeVO 端同時管理子表，可加入以下欄位
    // -------------------------------------------------------------------

    /**
     * room 資料表 (room_type_id 外鍵)
     * 如果在 room 裏的屬性命名為 "roomType"，則此處 mappedBy="roomType"。
     */
    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomVO> rooms;

    /**
     * price 資料表 (room_type_id 外鍵)
     */
    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PriceVO> prices;

    /**
     * room_inventory 資料表 (room_type_id 外鍵)
     */
    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomInventoryVO> roomInventories;

    /**
     * room_type_facility (中繼表, room_type_id 外鍵)
     * 若你有對應的實體叫 RoomTypeFacilityVO，則可在這裡做一對多
     */
    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomTypeFacilityVO> roomTypeFacilities;

    /**
     * room_type_img (room_type_id 外鍵)
     */
    @JsonIgnore
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomTypeImgVO> roomTypeImgs;

    /**
     * order_detail (room_type_id 外鍵)
     * 若需要雙向關聯，可在 orderDetail 裏設計 @ManyToOne
     */
//    方便設計暫時先拿掉
//    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<OrderDetailVO> orderDetails;


    // -------------------------------------------------------------------
    // 建構子、Getter / Setter
    // -------------------------------------------------------------------
    public RoomTypeVO() {
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public HotelVO getHotel() {
        return hotel;
    }

    public void setHotel(HotelVO hotel) {
        this.hotel = hotel;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(Integer maxPerson) {
        this.maxPerson = maxPerson;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(Integer roomNum) {
        this.roomNum = roomNum;
    }

    public Byte getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(Byte breakfast) {
        this.breakfast = breakfast;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Timestamp getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Timestamp reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public List<RoomVO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomVO> rooms) {
        this.rooms = rooms;
    }

    public List<PriceVO> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceVO> prices) {
        this.prices = prices;
    }

    public List<RoomInventoryVO> getRoomInventories() {
        return roomInventories;
    }

    public void setRoomInventories(List<RoomInventoryVO> roomInventories) {
        this.roomInventories = roomInventories;
    }

    public List<RoomTypeFacilityVO> getRoomTypeFacilities() {
        return roomTypeFacilities;
    }

    public void setRoomTypeFacilities(List<RoomTypeFacilityVO> roomTypeFacilities) {
        this.roomTypeFacilities = roomTypeFacilities;
    }

    public List<RoomTypeImgVO> getRoomTypeImgs() {
        return roomTypeImgs;
    }

    public void setRoomTypeImgs(List<RoomTypeImgVO> roomTypeImgs) {
        this.roomTypeImgs = roomTypeImgs;
    }

//    public List<OrderDetailVO> getOrderDetails() {
//        return orderDetails;
//    }
//
//    public void setOrderDetails(List<OrderDetailVO> orderDetails) {
//        this.orderDetails = orderDetails;
//    }

}
