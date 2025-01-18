package com.order.model;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.member.model.MemberVO;
import com.order.dto.*;

public interface OrderRepository extends JpaRepository<OrderVO, Integer> {

	@Query("SELECT new com.order.dto.CommentDTO(o.orderId, o.guestLastName, o.guestFirstName, h.name, o.commentCreateTime, o.rating, o.commentContent) " +
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
		       "COALESCE(o.rating, 0), " +  // 修正 rating 預設值
		       "COALESCE(o.commentContent, '')) " +  // 修正 commentContent 的位置
		       "FROM OrderVO o " +
		       "LEFT JOIN o.hotel h " +  // 保留 LEFT JOIN 處理 hotel 為 null 的情況
		       "WHERE (:clientName IS NULL OR CONCAT(COALESCE(o.guestLastName, ''), COALESCE(o.guestFirstName, '')) LIKE CONCAT('%', :clientName, '%')) " +
		       "AND (:hotelName IS NULL OR COALESCE(h.name, '') LIKE CONCAT('%', :hotelName, '%'))" +
			   "AND (:orderId IS NULL OR o.orderId = :orderId)")
		Page<CommentDTO> findCommentsByFilters(
		    @Param("clientName") String clientName,
		    @Param("hotelName") String hotelName,
		    @Param("orderId") Integer orderId,
		    Pageable pageable
		);

	 @Query("SELECT new com.order.dto.CommentDTO(" +
	           "o.orderId, " +
	           "COALESCE(o.guestLastName, ''), " +
	           "COALESCE(o.guestFirstName, ''), " +
	           "COALESCE(h.name, ''), " +
	           "o.commentCreateTime, " +
	           "COALESCE(o.rating, 0), " +
	           "COALESCE(o.commentContent, '')) " +
	           "FROM OrderVO o " +
	           "LEFT JOIN o.hotel h " +
	           "WHERE o.orderId = :orderId")
	    Optional<CommentDTO> findCommentByOrderId(@Param("orderId") Integer orderId);
	 
	    @Query("SELECT new com.order.dto.AvgRatingsAndCommentDTO(" +
	            "COUNT(o.commentContent), AVG(o.rating)) " +
	            "FROM OrderVO o " +
	            "WHERE o.orderId = :orderId")
	     Optional<AvgRatingsAndCommentDTO> findRatingAndCommentByOrderId(@Param("orderId") Integer orderId);

	    @Query("SELECT o.member FROM OrderVO o WHERE o.hotel.name = :hotelName GROUP BY o.member.memberId ORDER BY o.member.memberId ASC")
	    List<MemberVO> findClientsByHotelName(@Param("hotelName") String hotelName);

	    @Query("SELECT m FROM MemberVO m WHERE "
	            + "(:clientId IS NULL OR m.memberId = :clientId) AND "
	            + "(:clientName IS NULL OR CONCAT(m.lastName, m.firstName) LIKE %:clientName%) AND "
	            + "(:clientMail IS NULL OR m.account LIKE %:clientMail%) AND "
	            + "(:clientPhone IS NULL OR m.phoneNumber LIKE %:clientPhone%)")
	    List<MemberVO> searchClients(
	            @Param("clientId") Integer clientId,
	            @Param("clientName") String clientName,
	            @Param("clientMail") String clientMail,
	            @Param("clientPhone") String clientPhone);

		List<OrderVO> findByHotelHotelIdAndRatingIsNotNullAndCommentContentIsNotNull(Integer hotelId);

	@Query(value = """
		SELECT COUNT(*)
		FROM orders
		WHERE order_id = :orderId
		  AND member_id = :memberId
	""", nativeQuery = true)
	long countByOrderIdAndMemberId(@Param("orderId") Integer orderId, @Param("memberId") Integer memberId);

}
