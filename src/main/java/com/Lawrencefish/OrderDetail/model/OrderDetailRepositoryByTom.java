package com.Lawrencefish.OrderDetail.model;

import com.order.model.OrderVO;
import com.orderDetail.model.OrderDetailVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepositoryByTom extends JpaRepository<OrderDetailVO, Integer> {
    List<OrderDetailVO> findByOrder(OrderVO order);}
