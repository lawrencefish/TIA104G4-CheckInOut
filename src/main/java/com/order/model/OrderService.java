package com.order.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.member.model.MemberRepository;
import com.member.model.MemberVO;
import com.membercoupon.model.MemberCouponRepository;
import com.membercoupon.model.MemberCouponVO;
import com.order.dto.AvgRatingsAndCommentDTO;
import com.order.dto.CommentDTO;
import com.orderDetail.model.OrderDetailDTO;
import com.orderDetail.model.OrderDetailRepository;
import com.price.model.PriceService;
import com.price.model.PriceVO;
import com.roomInventory.model.RoomInventoryRepository;

@Service("OrderService")
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	PriceService priceService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberCouponRepository mCRepository;
	@Autowired
	RoomInventoryRepository riRepository;

	@Transactional
	public void addOrder(OrderVO orderVO) {
		orderRepository.save(orderVO);
	}
	@Transactional
	public void updateOrder(OrderVO orderVO) {
		 orderRepository.save(orderVO);
	}

	public OrderVO queryOrder(Integer orderId) {
		Optional<OrderVO> optional = orderRepository.findById(orderId);
		return optional.orElse(null);
	}

	public List<OrderVO> getAll() {
		return orderRepository.findAll();
	}

	public List<CommentDTO> getAllComments() {
		return orderRepository.findAllComments();
	}

	public Page<CommentDTO> getFilteredComments(String clientName, String hotelName, Integer orderId, int page,
			int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.unsorted()); // 分頁並加入排序
		return orderRepository.findCommentsByFilters(clientName, hotelName, orderId, pageable);
	}

	public List<OrderVO> findByHotelId(Integer HotelId) {
		return orderRepository.findByHotelHotelIdAndRatingIsNotNullAndCommentContentIsNotNull(HotelId);

	}

	public CommentDTO getCommentById(Integer orderId) {
		return orderRepository.findCommentByOrderId(orderId).orElseThrow(() -> new RuntimeException("找不到該評論"));
	}

	@Transactional
	public void saveReply(Integer orderId, String commentReply) {
		// 檢查訂單是否存在
		Optional<OrderVO> orderOptional = orderRepository.findById(orderId);
		if (orderOptional.isEmpty()) {
			throw new RuntimeException("找不到對應的訂單，無法儲存回覆");
		}

		// 獲取訂單實體
		OrderVO order = orderOptional.get();

		// 更新回覆內容和時間
		order.setCommentReply(commentReply);

		// 儲存更新後的訂單
		orderRepository.save(order);
	}

	public AvgRatingsAndCommentDTO getAvgRatingAndCommentCounts(Integer orderId) {
		Optional<AvgRatingsAndCommentDTO> optionalStats = orderRepository.findRatingAndCommentByOrderId(orderId);
		AvgRatingsAndCommentDTO stats = optionalStats.orElse(new AvgRatingsAndCommentDTO(0L, 0.0));
		return stats;
	}
	
	public void cancelOrder (Integer orderId){
		OrderVO order = orderRepository.getById(orderId);
		Byte cancelStatus = 3;
		order.setStatus(cancelStatus);
		
		orderRepository.save(order);
	}
		

	public List<MemberVO> findClientsByHotel(String hotelName) {
		return orderRepository.findClientsByHotelName(hotelName);
	}

	public List<MemberVO> searchClients(Integer clientId, String clientName, String clientMail, String clientPhone) {
		return orderRepository.searchClients(clientId, clientName, clientMail, clientPhone);
	}

	public List<OrderDTO> getOrdersWithDetailsByMemberId(Integer memberId) {
		List<OrderDTO> orders = orderRepository.findOrdersByMemberId(memberId);

		for (OrderDTO order : orders) {
			List<OrderDetailDTO> details = orderDetailRepository.findOrderDetailsByOrderId(order.getOrderId());
			if (order.getMemberCouponId() != null) {
				MemberCouponVO coupon = mCRepository.getById(order.getMemberCouponId());
				order.setDiscount(coupon.getCoupon().getDiscountAmount());
			}
			Integer totalPriceAdd = 0;
			for (OrderDetailDTO detail : details) {
				LocalDate checkInDate = new Date(order.getCheckInDate().getTime()).toLocalDate();
				LocalDate checkOutDate = new Date(order.getCheckOutDate().getTime()).toLocalDate();
				Integer totalPrice = 0;
				Integer totalBreakfastPrice = 0;
				for (LocalDate date = checkInDate; !date.isEqual(checkOutDate); date = date.plusDays(1)) {
					PriceVO price = priceService.getPriceOfDay(detail.getRoomTypeId(), date);
					totalPrice += price.getPrice() * detail.getRoomNum();
					if (detail.getBreakfast() != 0) {
						totalBreakfastPrice += price.getBreakfastPrice() * detail.getGuestNum();
						totalPrice += price.getBreakfastPrice() * detail.getGuestNum();
					}
				}
				totalPriceAdd += totalPrice;
				detail.setTotalPrice(totalPrice);
				detail.setTotalBreakfastPrice(totalBreakfastPrice);
				order.setOrderDetails(details);
			}
		}
		return orders;
	}

	////// 聊天室的東西

	public MemberVO getMemberId(Integer memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("找不到該客戶"));
	}

	public void updateMember(MemberVO updatedClient) {
		MemberVO existingMember = memberRepository.findById(updatedClient.getMemberId())
				.orElseThrow(() -> new IllegalArgumentException("無法找到對應的客戶"));
		existingMember.setLastName(updatedClient.getLastName());
		existingMember.setFirstName(updatedClient.getFirstName());
		existingMember.setGender(updatedClient.getGender());
		existingMember.setBirthday(updatedClient.getBirthday());
		existingMember.setPhoneNumber(updatedClient.getPhoneNumber());
		existingMember.setAccount(updatedClient.getAccount());
		memberRepository.save(existingMember); // 儲存更新後的資料
	}

}
