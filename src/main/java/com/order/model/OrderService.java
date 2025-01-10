package com.order.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	public List<CommentDTO> getFilteredComments(String clientName, String hotelName, String sort) {
	    Sort sorting;
	    if ("stars_desc".equals(sort)) {
	        sorting = Sort.by(Sort.Direction.DESC, "rating");
	    } else if ("stars_asc".equals(sort)) {
	        sorting = Sort.by(Sort.Direction.ASC, "rating");
	    } else {
	        sorting = Sort.unsorted();
	    }

	    return orderRepository.findCommentsByFilters(clientName, hotelName, sorting);
	}

}
