package com.comment.repository;

import com.comment.model.Comment;
import com.report.model.Reports;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	@Query("SELECT c.commentContent FROM Comment c WHERE c.hotelId = :hotelId AND c.memberId = :memberId")
	List<String> findCommentsByHotelIdAndMemberId(@Param("hotelId") Integer hotelId, @Param("memberId") Integer memberId);
}
