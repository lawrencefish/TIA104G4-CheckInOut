package com.creditcard.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CreditcardRepository extends JpaRepository<CreditcardVO, Integer> {
	
	List<CreditcardVO> findByMemberMemberId(Integer memberId);
}