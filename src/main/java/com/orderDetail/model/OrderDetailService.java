package com.orderDetail.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("OrderDetailService")
public class OrderDetailService {

	@Autowired
	OrderDetailRepository repository;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void addMember(OrderDetailVO orderDetailVO) {
		repository.save(orderDetailVO);
	}

	@Transactional
	public void updateMember(OrderDetailVO orderDetailVO) {
		repository.save(orderDetailVO);
	}

	public OrderDetailVO queryOrder(Integer orderDetailId) {
		Optional<OrderDetailVO> optional = repository.findById(orderDetailId);
		return optional.orElse(null);  
	}
  
	public List<OrderDetailVO> getAll() {
		return repository.findAll();
	}

}
