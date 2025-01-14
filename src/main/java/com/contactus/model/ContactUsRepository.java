package com.contactus.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUsVO, Integer>{

	List<ContactUsVO> findByReviewStatus(Byte reviewStatus);
	
}
