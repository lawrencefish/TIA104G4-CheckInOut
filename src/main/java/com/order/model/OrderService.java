package com.order.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("OrderService")
public class OrderService {

	@Autowired
	OrderRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addOrder(OrderVO orderVO) {
		repository.save(orderVO);
	}
	
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
	
}
