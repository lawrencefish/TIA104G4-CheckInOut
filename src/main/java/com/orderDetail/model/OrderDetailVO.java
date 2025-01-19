package com.orderDetail.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.order.model.OrderVO;

@Entity
@Table(name = "order_detail")
public class OrderDetailVO implements java.io.Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_detail_id")
	private Integer orderDetailId;
	@Column(name = "room_type_id")
	private Integer roomTypeId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id", nullable = false)
	@NotNull(message = "訂單不可為空")
	private OrderVO order;
	@Column(name = "guest_num")
	@Min(value = 1, message = "至少一人入住")
	private Integer guestNum;
	@Min(value = 1, message = "至少一間入住")
	@Column(name = "room_num")
	private Integer roomNum;
	@Column(name = "breakfast")
	private Byte breakfast;

	public OrderDetailVO(){
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public Integer getRoomTypeId() {
		return roomTypeId;
	}

	public OrderVO getOrder() {
		return order;
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

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public void setRoomTypeId(Integer roomTypeId) {
		this.roomTypeId = roomTypeId;
	}

	public void setOrder(OrderVO order) {
		this.order = order;
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
	
}
