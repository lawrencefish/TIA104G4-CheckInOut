package com.hotel.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import com.employee.model.EmployeeVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotelFacility.model.HotelFacilityVO;
import com.hotelImg.model.HotelImgVO;
import com.roomType.model.RoomTypeVO;

@Entity
@Table(name = "hotel")
public class HotelVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Integer hotelId;

    // tax_id VARCHAR(8) NOT NULL UNIQUE
    @NotBlank(message = "統一編號不可為空")             // 非空白
    @Size(max = 8, message = "統一編號最長 8 碼")
    @Column(name = "tax_id", length = 8, nullable = false, unique = true)
    private String taxId;

    // password VARCHAR(20) NOT NULL
    @NotBlank(message = "密碼不可為空")
    @Size(max = 20, message = "密碼長度不可超過 20")
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    // name VARCHAR(100) NOT NULL UNIQUE
    @NotBlank(message = "旅館名稱不可為空")
    @Size(max = 100, message = "旅館名稱長度不可超過 100")
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    // city VARCHAR(20) NOT NULL
    @NotBlank(message = "城市不可為空")
    @Size(max = 20, message = "城市長度不可超過 20")
    @Column(name = "city", length = 20, nullable = false)
    private String city;

    // district VARCHAR(20) NOT NULL
    @NotBlank(message = "行政區不可為空")
    @Size(max = 20, message = "行政區長度不可超過 20")
    @Column(name = "district", length = 20, nullable = false)
    private String district;

    // address VARCHAR(255) NOT NULL UNIQUE
    @NotBlank(message = "地址不可為空")
    @Size(max = 255, message = "地址長度不可超過 255")
    @Column(name = "address", length = 255, nullable = false, unique = true)
    private String address;

    // phone_number VARCHAR(15) NOT NULL UNIQUE
    @NotBlank(message = "電話不可為空")
    @Size(max = 15, message = "電話長度不可超過 15")
    @Column(name = "phone_number", length = 15, nullable = false, unique = true)
    private String phoneNumber;

    // email VARCHAR(100) NOT NULL UNIQUE
    @NotBlank(message = "Email 不可為空")
    @Email(message = "Email 格式不正確")
    @Size(max = 100, message = "Email 長度不可超過 100")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    // status TINYINT NOT NULL COMMENT '0=未審核, 1=啟動, 2=審核沒通過'
    @Column(name = "status", nullable = false, insertable = false)
    private Integer status; // 可以用 Byte 或 Integer 來對應 TINYINT

    // create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    @Column(name = "create_time", nullable = false, insertable = false, updatable = false)
    private Timestamp createTime;

    // owner VARCHAR(50) NOT NULL
    @NotBlank(message = "負責人姓名不可為空")
    @Size(max = 50, message = "負責人姓名不可超過 50 字")
    @Column(name = "owner", length = 50, nullable = false)
    private String owner;

    // id_front MEDIUMBLOB
    // id_back MEDIUMBLOB
    // license MEDIUMBLOB
    // 這些 BLOB 資料可以用 byte[] 或 Blob 形式，在 JPA 中常用 byte[] + @Lob
    @Lob
    @Column(name = "id_front")
    private byte[] idFront;

    @Lob
    @Column(name = "id_back")
    private byte[] idBack;

    @Lob
    @Column(name = "license")
    private byte[] license;

    // info_text TEXT NOT NULL
    @NotBlank(message = "旅館資訊不可為空")
    @Column(name = "info_text", columnDefinition = "TEXT", nullable = false)
    private String infoText;

    // review_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    @Column(name = "review_time", insertable = false)
    private Timestamp reviewTime;

    // update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    @Column(name = "update_time", insertable = false, updatable = false)
    private Timestamp updateTime;

    // latitude DOUBLE COMMENT '緯度'
    @Column(name = "latitude", nullable = true)
    private Double latitude;

    // longitude DOUBLE COMMENT '經度'
    @Column(name = "longitude", nullable = true)
    private Double longitude;

    // ----------------------------------
    // 其他表與 hotel 的一對多關係 (示例)
    // ----------------------------------

    // Employee (多) -> Hotel (一) :
    //   在 employee 資料表中有 hotel_id 做為外鍵
    //   -> 這裡可用 mappedBy="hotel" (對應 EmployeeVO 的 "hotel")
    //   -> fetch 與 cascade 可依需求自行調整
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmployeeVO> employees;

    // Hotel_img (多) -> Hotel (一)
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelImgVO> hotelImgs;

    // Hotel_facility (多) -> Hotel (一)
    //   多對多關係其實是 Hotel 透過中間表 hotel_facility 與 facility 關聯
    //   但若你有中繼表 HotelFacilityVO，這裡就是一對多
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HotelFacilityVO> hotelFacilities;

    // Room_type (多) -> Hotel (一)
    @JsonIgnore
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomTypeVO> roomTypes;

    // (其他表格 like Orders, Favorite, 也可能參考 hotel_id ，可同樣使用一對多)

    // ----------------------------------
    // Constructors, Getters, Setters
    // ----------------------------------

    public HotelVO() {
    }

    // 可自行新增方便的建構子、Getter/Setter或使用 Lombok 省略

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public byte[] getIdFront() {
        return idFront;
    }

    public void setIdFront(byte[] idFront) {
        this.idFront = idFront;
    }

    public byte[] getIdBack() {
        return idBack;
    }

    public void setIdBack(byte[] idBack) {
        this.idBack = idBack;
    }

    public byte[] getLicense() {
        return license;
    }

    public void setLicense(byte[] license) {
        this.license = license;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
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

    // Getter and Setter for latitude
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    // Getter and Setter for longitude
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<EmployeeVO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeVO> employees) {
        this.employees = employees;
    }

    public List<HotelImgVO> getHotelImgs() {
        return hotelImgs;
    }

    public void setHotelImgs(List<HotelImgVO> hotelImgs) {
        this.hotelImgs = hotelImgs;
    }

    public List<HotelFacilityVO> getHotelFacilities() {
        return hotelFacilities;
    }

    public void setHotelFacilities(List<HotelFacilityVO> hotelFacilities) {
        this.hotelFacilities = hotelFacilities;
    }

    public List<RoomTypeVO> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<RoomTypeVO> roomTypes) {
        this.roomTypes = roomTypes;
    }

}
