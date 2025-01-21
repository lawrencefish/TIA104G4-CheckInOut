package com.orderDetail.model;

import javax.persistence.Transient;

public class OrderDetailDTO {

	private Integer orderDetailId;
	private Integer guestNum;
	private Integer roomNum;
	private Byte breakfast;
	//roomType
	private Integer roomTypeId;
    private String roomName;
    private Integer maxPerson;
    //price
    @Transient
    private Integer totalPrice;
    @Transient
    private Integer totalBreakfastPrice;

    
	public OrderDetailDTO(Integer orderDetailId, Integer guestNum, Integer roomNum, Byte breakfast, Integer roomTypeId,
			String roomName, Integer maxPerson) {
		super();
		this.orderDetailId = orderDetailId;
		this.guestNum = guestNum;
		this.roomNum = roomNum;
		this.breakfast = breakfast;
		this.roomTypeId = roomTypeId;
		this.roomName = roomName;
		this.maxPerson = maxPerson;
		this.totalPrice = 0;
		this.totalBreakfastPrice = 0;
	}
	
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public Integer getGuestNum() {
		return guestNum;
	}
	public Integer getRoomNum() {
		return roomNum;
	}
	public Byte getBreakfast() {
		return breakfast;
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
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public void setGuestNum(Integer guestNum) {
		this.guestNum = guestNum;
	}
	public void setRoomNum(Integer roomNum) {
		this.roomNum = roomNum;
	}
	public void setBreakfast(Byte breakfast) {
		this.breakfast = breakfast;
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

	public Integer getTotalPrice() {
		return totalPrice;
	}

	public Integer getTotalBreakfastPrice() {
		return totalBreakfastPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public void setTotalBreakfastPrice(Integer totalBreakfastPrice) {
		this.totalBreakfastPrice = totalBreakfastPrice;
	}

}
