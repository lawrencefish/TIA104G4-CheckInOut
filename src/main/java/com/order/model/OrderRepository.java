package com.order.model;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.order.dto.*;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

	@Query("SELECT new com.order.dto.CommentDTO(o.orderId, o.guestLastName, o.guestFirstName, h.name, o.commentCreateTime, o.rating) " +
		       "FROM OrderVO o " +
		       "JOIN o.hotel h " +
		       "WHERE o.commentContent IS NOT NULL")
		List<CommentDTO> findAllComments();

	@Query("SELECT new com.order.dto.CommentDTO(o.orderId, o.guestLastName, o.guestFirstName, h.name, o.commentCreateTime, o.rating) " +
		       "FROM OrderVO o " +
		       "JOIN o.hotel h " +
		       "WHERE (:clientName IS NULL OR CONCAT(o.guestLastName, o.guestFirstName) LIKE %:clientName%) " +
		       "AND (:hotelName IS NULL OR h.name LIKE %:hotelName%)")
		List<CommentDTO> findCommentsByFilters(
		    @Param("clientName") String clientName,
		    @Param("hotelName") String hotelName,
		    Sort sort
		);

	}
