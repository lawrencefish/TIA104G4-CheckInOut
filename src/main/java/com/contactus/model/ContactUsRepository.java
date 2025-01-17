package com.contactus.model;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactUsRepository extends JpaRepository<ContactUsVO, Integer>{

//	List<ContactUsVO> findByStatus(String status);
//    List<ContactUsVO> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
