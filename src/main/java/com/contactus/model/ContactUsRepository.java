package com.contactus.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.member.model.MemberVO;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUsVO, Integer>{
	
	List<ContactUsVO> findByMemberOrderByCreateTimeDesc(MemberVO member);
    Page<ContactUsVO> findAll(Pageable pageable);

	List<ContactUsVO> findByReviewStatus(Byte reviewStatus);
	
}
