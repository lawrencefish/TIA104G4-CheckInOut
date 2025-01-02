package com.member.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<MemberVO, Integer> {

	@Transactional
	@Modifying
	@Query(value="delete from member where member_id = ?1",nativeQuery = true)
	void deleteByMemberID(int memberId);
	
}
