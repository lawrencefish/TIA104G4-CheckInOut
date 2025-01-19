package com.order.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.member.model.MemberRepository;
import com.member.model.MemberVO;
import com.order.dto.AvgRatingsAndCommentDTO;
import com.order.dto.CommentDTO;

@Service("OrderService")
public class OrderService {

	@Autowired
	OrderRepository repository;

 	@Autowired
 	MemberRepository memberRepository;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Transactional
	public void addOrder(OrderVO orderVO) {
		repository.save(orderVO);
	}

	public OrderVO queryOrder(Integer orderId) {
		Optional<OrderVO> optional = repository.findById(orderId);
		return optional.orElse(null);
	}

	public List<OrderVO> getAll() {
		return repository.findAll();
	}

	public List<CommentDTO> getAllComments() {
		return repository.findAllComments();
	}

	@Autowired
	private OrderRepository orderRepository;

	public Page<CommentDTO> getFilteredComments(String clientName, String hotelName, Integer orderId, int page, int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.unsorted()); // 分頁並加入排序
		return orderRepository.findCommentsByFilters(clientName, hotelName, orderId, pageable);
	}

    
    public List<OrderVO> findByHotelId(Integer HotelId){
		return  orderRepository.findByHotelHotelIdAndRatingIsNotNullAndCommentContentIsNotNull(HotelId);
    	
    }

    public CommentDTO getCommentById(Integer orderId) {
		return orderRepository.findCommentByOrderId(orderId)
				.orElseThrow(() -> new RuntimeException("找不到該評論"));
	}

	@Transactional
	public void saveReply(Integer orderId, String commentReply) {
		// 檢查訂單是否存在
		Optional<OrderVO> orderOptional = repository.findById(orderId);
		if (orderOptional.isEmpty()) {
			throw new RuntimeException("找不到對應的訂單，無法儲存回覆");
		}

		// 獲取訂單實體
		OrderVO order = orderOptional.get();

		// 更新回覆內容和時間
		order.setCommentReply(commentReply);

		// 儲存更新後的訂單
		repository.save(order);
	}

	public AvgRatingsAndCommentDTO getAvgRatingAndCommentCounts(Integer orderId) {
		Optional<AvgRatingsAndCommentDTO> optionalStats = orderRepository.findRatingAndCommentByOrderId(orderId);
		AvgRatingsAndCommentDTO stats = optionalStats.orElse(new AvgRatingsAndCommentDTO(0L, 0.0));
		return stats;
	}
	
	public List<OrderVO> findByMemberId(Integer memberId){
		return orderRepository.findTop2ByMemberMemberId(memberId);
	}
 	
	public OrderVO findById(Integer orderId) {
		// TODO Auto-generated method stub
		return null;
	}

 	public List<MemberVO> findClientsByHotel(String hotelName) {
         return orderRepository.findClientsByHotelName(hotelName);
     }
	
 	public List<MemberVO> searchClients(Integer clientId, String clientName, String clientMail, String clientPhone) {
         return orderRepository.searchClients(clientId, clientName, clientMail, clientPhone);
     }
	
 	public MemberVO getMemberId(Integer memberId) {
         return memberRepository.findById(memberId)
                 .orElseThrow(() -> new RuntimeException("找不到該客戶"));
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

