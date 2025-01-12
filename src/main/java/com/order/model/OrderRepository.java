package com.order.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("SELECT new com.order.dto.CommentDTO(" +
		       "o.orderId, " +
		       "COALESCE(o.guestLastName, ''), " +
		       "COALESCE(o.guestFirstName, ''), " +
		       "COALESCE(h.name, ''), " +
		       "o.commentCreateTime, " +
		       "COALESCE(o.rating, 0)) " +  // 預設 rating 為 0
		       "FROM OrderVO o " +
		       "LEFT JOIN o.hotel h " +  // 如果 hotel 可能為 null，改用 LEFT JOIN
		       "WHERE (:clientName IS NULL OR CONCAT(COALESCE(o.guestLastName, ''), COALESCE(o.guestFirstName, '')) LIKE %:clientName%) " +
		       "AND (:hotelName IS NULL OR COALESCE(h.name, '') LIKE %:hotelName%)")
	Page<CommentDTO> findCommentsByFilters(
		    @Param("clientName") String clientName,
		    @Param("hotelName") String hotelName,
		    Pageable pageable
		);


	}
