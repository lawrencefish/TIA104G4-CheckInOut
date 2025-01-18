package com.roomInventory.model;

import java.time.LocalDate;

public class HotelRoomInventoryDTO {
	// roomInventory物件
	private Integer inventoryId;
	private LocalDate date;
	private Integer availableQuantity;
	// hotel物件
	private Integer hotelId;
	private String name;
	// roomType物件
	private Integer roomTypeId;
	private String roomName;
	private Integer maxPerson;
	private Byte breakfast;
	
	public HotelRoomInventoryDTO(Integer inventoryId, LocalDate date, Integer availableQuantity, Integer hotelId,
			String name, Integer roomTypeId, String roomName, Integer maxPerson, Byte breakfast) {
		super();
		this.inventoryId = inventoryId;
		this.date = date;
		this.availableQuantity = availableQuantity;
		this.hotelId = hotelId;
		this.name = name;
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

}