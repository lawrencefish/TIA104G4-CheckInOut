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

import com.order.dto.CommentDTO;

@Service("OrderService")
public class OrderService {

	@Autowired
	OrderRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void addOrder(OrderVO orderVO) {
		repository.save(orderVO);
	}
	
	@Transactional
	public void updateOrder(OrderVO orderVO) {
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
	    
		Pageable pageable = PageRequest.of(page, size,Sort.unsorted()); // 分頁並加入排序
	    return orderRepository.findCommentsByFilters(clientName, hotelName, orderId, pageable);
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




}
