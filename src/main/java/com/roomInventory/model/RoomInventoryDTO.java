package com.roomInventory.model;

import java.time.LocalDate;

public class RoomInventoryDTO {
	//roomInventory物件
    private Integer inventoryId;
	private LocalDate date;
    private Integer availableQuantity;
    //hotel物件
    private Integer hotelId;
    private String name;
    private String city;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
    //roomType物件
    private Integer roomTypeId;
    private String roomName;
    private Integer maxPerson;
    private Byte breakfast;
    
    public RoomInventoryDTO(
            Integer inventoryId,
            LocalDate date,
            Integer availableQuantity,
            Integer hotelId,
            String name,
            String city,
            String district,
            String address,
            Double latitude,
            Double longitude,
            Integer roomTypeId,
            String roomName,
            Integer maxPerson,
            Byte breakfast) {
        this.inventoryId = inventoryId;
        this.date = date;
        this.availableQuantity = availableQuantity;
        this.hotelId = hotelId;
        this.name = name;
        this.city = city;
        this.district = district;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roomTypeId = roomTypeId;
        this.roomName = roomName;
        this.maxPerson = maxPerson;
        this.breakfast = breakfast;
    }
    
	public Integer getInventoryId() {
		return inventoryId;
	}
	public LocalDate getDate() {
		return date;
	}
	public Integer getAvailableQuantity() {
		return availableQuantity;
	}
	public Integer getHotelId() {
		return hotelId;
	}
	public String getName() {
		return name;
	}
	public String getCity() {
		return city;
	}
	public String getDistrict() {
		return district;
	}
	public String getAddress() {
		return address;
	}
	public Double getLatitude() {
		return latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public Integer getRoomTypeId() {
		return roomTypeId;
	}
	public String getRoomName() {
		return roomName;
	}
	public Integer getMaxPerson() {
		return maxPerson;
	}
	public Byte getBreakfast() {
		return breakfast;
	}
	public void setInventoryId(Integer inventoryId) {
		this.inventoryId = inventoryId;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public void setAvailableQuantity(Integer availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public void setRoomTypeId(Integer roomTypeId) {
		this.roomTypeId = roomTypeId;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public void setMaxPerson(Integer maxPerson) {
		this.maxPerson = maxPerson;
	}
	public void setBreakfast(Byte breakfast) {
		this.breakfast = breakfast;
	}
	
	@Override
	public String toString() {
		return "RoomInventoryDTO [inventoryId=" + inventoryId + ", date=" + date + ", availableQuantity="
				+ availableQuantity + ", hotelId=" + hotelId + ", name=" + name + ", city=" + city + ", district="
				+ district + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", roomTypeId=" + roomTypeId + ", roomName=" + roomName + ", maxPerson=" + maxPerson + ", breakfast="
				+ breakfast + "]";
	}
	
}
