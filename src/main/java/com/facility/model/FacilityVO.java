package com.hotel.model;


import com.hotelFacility.model.HotelFacilityVO;
import com.roomTypeFacility.model.RoomTypeFacilityVO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "facility")
public class FacilityVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id")
    private Integer facilityId;

    // facility_type TINYINT NOT NULL
    @NotNull(message = "設施類型不可為空")
    @Column(name = "facility_type", nullable = false)
    private Byte facilityType; // 1=旅館, 2=房型

    // facility_name VARCHAR(20) NOT NULL UNIQUE
    @NotBlank(message = "設施名稱不可為空")
    @Size(max = 20, message = "設施名稱不可超過 20 字")
    @Column(name = "facility_name", length = 20, nullable = false, unique = true)
    private String facilityName;

    // 若需要雙向關聯:
    //   1) 對 hotel_facility (中繼表) → 多對一
    //   2) 對 room_type_facility (中繼表) → 多對一
    //   不同需求可以自行增添
    // 例：hotel_facility (一對多)
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelFacilityVO> hotelFacilities;

    // 例：room_type_facility (一對多)
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomTypeFacilityVO> roomTypeFacilities;

    // ----------------------------------------
    // Constructors, Getters, Setters
    // ----------------------------------------
    public FacilityVO() {
    }

    public Integer getFacilityId() {
        return facilityId;
    }
    public void setFacilityId(Integer facilityId) {
        this.facilityId = facilityId;
    }

    public Byte getFacilityType() {
        return facilityType;
    }
    public void setFacilityType(Byte facilityType) {
        this.facilityType = facilityType;
    }

    public String getFacilityName() {
        return facilityName;
    }
    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public List<HotelFacilityVO> getHotelFacilities() {
        return hotelFacilities;
    }
    public void setHotelFacilities(List<HotelFacilityVO> hotelFacilities) {
        this.hotelFacilities = hotelFacilities;
    }

    public List<RoomTypeFacilityVO> getRoomTypeFacilities() {
        return roomTypeFacilities;
    }
    public void setRoomTypeFacilities(List<RoomTypeFacilityVO> roomTypeFacilities) {
        this.roomTypeFacilities = roomTypeFacilities;
    }
}
