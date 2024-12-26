package com.roomTypeFacility.model;

import com.hotel.model.FacilityVO;
import com.roomType.model.RoomTypeVO;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "room_type_facility")
public class RoomTypeFacilityVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_facility_id")
    private Integer roomTypeFacilityId;

    // 多對一：對應到 room_type 表 (room_type_id)
    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false)
    @NotNull(message = "房型不可為空")
    private RoomTypeVO roomType;

    // 多對一：對應到 facility 表 (facility_id)
    @ManyToOne
    @JoinColumn(name = "facility_id", nullable = false)
    @NotNull(message = "設施不可為空")
    private FacilityVO facility;

    // 若需要額外欄位(如: create_time, 其他屬性) 可在此補充
    // 例如:
    // @Column(name = "create_time", insertable = false, updatable = false)
    // private LocalDateTime createTime;

    // -------------------------
    // Constructor, Getter, Setter
    // -------------------------
    public RoomTypeFacilityVO() {
    }

    public Integer getRoomTypeFacilityId() {
        return roomTypeFacilityId;
    }

    public void setRoomTypeFacilityId(Integer roomTypeFacilityId) {
        this.roomTypeFacilityId = roomTypeFacilityId;
    }

    public RoomTypeVO getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomTypeVO roomType) {
        this.roomType = roomType;
    }

    public FacilityVO getFacility() {
        return facility;
    }

    public void setFacility(FacilityVO facility) {
        this.facility = facility;
    }
}
