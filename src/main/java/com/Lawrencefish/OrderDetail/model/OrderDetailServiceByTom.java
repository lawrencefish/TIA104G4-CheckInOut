package com.Lawrencefish.OrderDetail.model;

import com.orderDetail.model.OrderDetailVO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceByTom {

	@Autowired
	OrderDetailRepositoryByTom repository;

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
